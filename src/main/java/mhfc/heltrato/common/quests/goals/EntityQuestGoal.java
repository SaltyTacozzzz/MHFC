package mhfc.heltrato.common.quests.goals;

import mhfc.heltrato.common.eventhandler.quests.NotifyableQuestGoal;
import mhfc.heltrato.common.eventhandler.quests.QuestGoalEventHandler;
import mhfc.heltrato.common.quests.QuestGoalSocket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

/**
 * A quest goal that is based on killing a specific entity.
 */
public class EntityQuestGoal extends QuestGoal
		implements
			NotifyableQuestGoal<LivingDeathEvent> {
	private EntityLivingBase entity;
	private boolean died;
	private NBTTagCompound nbt;
	private QuestGoalEventHandler<LivingDeathEvent> eventHandler;

	/**
	 * Constructs an {@link EntityQuestGoal} with the given entity. If it is
	 * null, an {@link IllegalArgumentException} is thrown
	 */
	public EntityQuestGoal(QuestGoalSocket socket, EntityLivingBase entity) {
		super(socket);
		if (entity == null)
			throw new IllegalArgumentException(
					"The goal of an EntityQuestGoal can not be null");
		this.entity = entity;
		died = !entity.isEntityAlive();
		eventHandler = new QuestGoalEventHandler<LivingDeathEvent>(this);
		nbt = new NBTTagCompound();
		entity.writeToNBT(nbt);
		MinecraftForge.EVENT_BUS.register(eventHandler);
	}

	@Override
	public boolean isFulfilled() {
		return died;
	}

	@Override
	public boolean isFailed() {
		return false;
	}

	@Override
	public void reset() {
		entity.readFromNBT(nbt);
		died = !entity.isEntityAlive();
	}

	@Override
	public void questGoalFinalize() {
		MinecraftForge.EVENT_BUS.unregister(eventHandler);
	}

	@Override
	public void setActive(boolean newActive) {
		if (newActive)
			died = entity.isEntityAlive();
		eventHandler.setActive(newActive);
	}

	@Override
	public void notifyOfEvent(LivingDeathEvent event) {
		if (event.entityLiving == entity) {
			died = true;
			notifyOfStatus(true, false);
		}
	}

}
