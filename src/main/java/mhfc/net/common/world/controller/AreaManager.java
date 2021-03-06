package mhfc.net.common.world.controller;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.Message;

import com.sk89q.worldedit.function.operation.Operation;

import mhfc.net.MHFCMain;
import mhfc.net.common.eventhandler.MHFCTickHandler;
import mhfc.net.common.eventhandler.TickPhase;
import mhfc.net.common.quests.world.QuestFlair;
import mhfc.net.common.util.Operations;
import mhfc.net.common.world.MHFCWorldData;
import mhfc.net.common.world.MHFCWorldData.AreaInformation;
import mhfc.net.common.world.area.ActiveAreaAdapter;
import mhfc.net.common.world.area.AreaConfiguration;
import mhfc.net.common.world.area.IActiveArea;
import mhfc.net.common.world.area.IArea;
import mhfc.net.common.world.area.IAreaType;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class AreaManager implements IAreaManager {

	private class Active extends ActiveAreaAdapter {
		private final IArea area;
		private final IAreaType type;
		private final QuestFlair flair;

		public Active(IArea area, IAreaType type, QuestFlair flair) {
			this.area = Objects.requireNonNull(area);
			this.type = Objects.requireNonNull(type);
			this.flair = Objects.requireNonNull(flair);
			this.area.onAcquire();
		}

		@Override
		public IArea getArea() {
			return area;
		}

		@Override
		public IAreaType getType() {
			return type;
		}

		@Override
		public QuestFlair getFlair() {
			return flair;
		}

		@Override
		protected void onEngage() {}

		@Override
		protected void onDismiss() {
			this.area.onDismiss();
			AreaManager.this.dismiss(this);
		}
	}

	protected Map<IAreaType, List<IArea>> nonactiveAreas = new HashMap<>();
	private MHFCWorldData saveData;
	protected final WeakReference<World> worldRef;
	protected Ticket loadingTicket;
	private final QuestFlair managedFlair;

	public AreaManager(World world, MHFCWorldData saveData, QuestFlair managedFlair) {
		this.worldRef = new WeakReference<>(Objects.requireNonNull(world));
		this.saveData = Objects.requireNonNull(saveData);
		this.managedFlair = Objects.requireNonNull(managedFlair);
	}

	private World getWorld() {
		return worldRef.get();
	}

	private QuestFlair getOwnQuestFlair() {
		return managedFlair;
	}

	private Ticket getLoadingTicket() {
		if (loadingTicket == null) {
			loadingTicket = ForgeChunkManager.requestTicket(MHFCMain.instance(), getWorld(), Type.NORMAL);
		}
		return loadingTicket;
	}

	private void dismiss(IActiveArea active) {
		this.nonactiveAreas.get(active.getType()).add(active.getArea());
	}

	@Override
	public void onLoaded() {
		Collection<AreaInformation> loadedAreas = this.saveData.getAllSpawnedAreas();
		for (AreaInformation info : loadedAreas) {
			IArea loadingArea = info.type.provideForLoading(getWorld(), info.config);
			this.nonactiveAreas.computeIfAbsent(info.type, (k) -> new ArrayList<>()).add(loadingArea);
		}
	}

	@Override
	public CompletionStage<IActiveArea> getUnusedInstance(IAreaType type) {
		Optional<IArea> chosen = nonactiveAreas.computeIfAbsent(type, (k) -> new ArrayList<>()).stream()
				.filter(a -> !a.isUnusable()).findFirst();
		if (chosen.isPresent()) {
			Active active = this.new Active(chosen.get(), type, getOwnQuestFlair());
			nonactiveAreas.get(type).remove(active.getArea());
			// FIXME: we can hope that whoever waits for the area also closes it, we just don't make sure for now
			return CompletableFuture.completedFuture(active);
		}
		return createNewInstance(type);
	}

	private CompletionStage<IActiveArea> createNewInstance(IAreaType type) {
		AreaInformation info = newArea(type);
		AreaConfiguration config = info.config;
		CornerPosition position = config.getPosition();

		final Operation plan = type.populate(getWorld(), config);
		final ChunkPos chunkPos = new ChunkPos(position.posX, position.posY);
		ForgeChunkManager.forceChunk(getLoadingTicket(), chunkPos);
		final Operation operation = Operations.timingOperation(plan, 20);

		return MHFCTickHandler.registerOperation(TickPhase.SERVER_PRE, operation).whenComplete((r, ex) -> {
			ForgeChunkManager.unforceChunk(getLoadingTicket(), chunkPos);
			if (ex == null) {
				onAreaCompleted(info);
				MHFCMain.logger().debug("Area of type {} completed", type);
			} else {
				onAreaCanceled(info);
				Message message = MHFCMain.logger().getMessageFactory().newMessage("Area of type {} cancelled", type);
				MHFCMain.logger().log(Level.DEBUG, message, ex);
			}
		}).thenApply(v -> {
			return this.new Active(type.provideForLoading(getWorld(), config), type, getOwnQuestFlair());
		});
	}

	private AreaInformation newArea(IAreaType type) {
		return saveData.newArea(type, type.configForNewArea());
	}

	private void onAreaCompleted(AreaInformation info) {
		saveData.onAreaFullyGenerated(info);
	}

	private void onAreaCanceled(AreaInformation info) {
		saveData.onAreaCanceled(info);
	}
}
