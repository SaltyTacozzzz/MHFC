package mhfc.net.common.ai.entity.monsters.deviljho;

import mhfc.net.common.ai.entity.EntityAIMethods;
import mhfc.net.common.ai.general.AIUtils;
import mhfc.net.common.ai.general.SelectionUtils;
import mhfc.net.common.ai.general.actions.JumpAction;
import mhfc.net.common.ai.general.provider.adapters.AnimationAdapter;
import mhfc.net.common.ai.general.provider.adapters.AttackTargetAdapter;
import mhfc.net.common.ai.general.provider.adapters.DamageAdapter;
import mhfc.net.common.ai.general.provider.adapters.JumpAdapter;
import mhfc.net.common.ai.general.provider.adapters.JumpTimingAdapter;
import mhfc.net.common.ai.general.provider.composite.IAnimationProvider;
import mhfc.net.common.ai.general.provider.composite.IJumpProvider;
import mhfc.net.common.ai.general.provider.impl.IHasJumpProvider;
import mhfc.net.common.ai.general.provider.simple.IDamageCalculator;
import mhfc.net.common.ai.general.provider.simple.IDamageProvider;
import mhfc.net.common.ai.general.provider.simple.IJumpParameterProvider;
import mhfc.net.common.ai.general.provider.simple.IJumpTimingProvider;
import mhfc.net.common.core.registry.MHFCSoundRegistry;
import mhfc.net.common.entity.monster.EntityDeviljho;
import mhfc.net.common.util.world.WorldHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class Jump extends JumpAction<EntityDeviljho> implements IHasJumpProvider<EntityDeviljho> {

	private static final int FRAMES = 60;
	private static final String ANIMATION_LOCATION = "mhfc:models/Deviljho/DeviljhoJump.mcanm";
	private static final int JUMPFRAME = 20;

	private static final float TURNRATE = 14;
	private static final float JUMPDURATION = 13.5f;

	private static final IDamageCalculator DAMAGEBASE = AIUtils.defaultDamageCalc(105f, 2000f, 999999F);
	private static final double DISTANCEMAX = 15F;
	private static final float WEIGHT = 1f;

	private final IJumpProvider<EntityDeviljho> JUMP_PROVIDER;
	{
		IAnimationProvider ANIMATION = new AnimationAdapter(this, ANIMATION_LOCATION, FRAMES);
		IDamageProvider DAMAGE = new DamageAdapter(DAMAGEBASE);
		IJumpParameterProvider<EntityDeviljho> PARAMETERS = new AttackTargetAdapter<>(JUMPDURATION);
		IJumpTimingProvider<EntityDeviljho> TIMING_PARAMS = new JumpTimingAdapter<>(JUMPFRAME, TURNRATE, 0);
		JUMP_PROVIDER = new JumpAdapter<>(ANIMATION, DAMAGE, PARAMETERS, TIMING_PARAMS);
	}

	private boolean thrown = false;

	public Jump() {}

	@Override
	protected float computeSelectionWeight() {
		EntityDeviljho entity = this.getEntity();
		target = entity.getAttackTarget();
		if (SelectionUtils.isIdle(entity)) {
			return DONT_SELECT;
		}
		Vec3d toTarget = WorldHelper.getVectorToTarget(entity, target);
		double dist = toTarget.lengthVector();
		if (dist > DISTANCEMAX) {
			return DONT_SELECT;
		}
		return WEIGHT;
	}

	@Override
	public IJumpProvider<EntityDeviljho> getJumpProvider() {
		return JUMP_PROVIDER;
	}

	@Override
	public void onUpdate() {
		EntityDeviljho entity = this.getEntity();
		target = entity.getAttackTarget();
		if (this.getCurrentFrame() == 5) {
			entity.playSound(MHFCSoundRegistry.getRegistry().deviljhoLeap, 2.0F, 1.0F);
		}
		if (!entity.onGround || thrown || this.getCurrentFrame() < 302) {
			return;
		}
		EntityAIMethods.stompCracks(entity, 200);
		if(target instanceof EntityPlayer){
		EntityAIMethods.camShake(entity, target, 10F, 40F);
		}
		thrown = true;
	}
}