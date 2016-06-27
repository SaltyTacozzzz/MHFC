package mhfc.net.common.ai.entity.boss.barroth;

import mhfc.net.common.ai.ActionAdapter;
import mhfc.net.common.ai.entity.AIGameplayComposition;
import mhfc.net.common.ai.general.AIUtils;
import mhfc.net.common.ai.general.AIUtils.IDamageCalculator;
import mhfc.net.common.entity.monster.EntityBarroth;
import mhfc.net.common.util.world.WorldHelper;
import net.minecraft.util.Vec3;


public class BarrothRamRun extends ActionAdapter<EntityBarroth> {
	private static final int LAST_FRAME = 130;
	private static final IDamageCalculator damageCalc = AIUtils.defaultDamageCalc(65f, 50F, 9999999f);

	private static final double MAX_DIST = 20F;
	private static final float WEIGHT = 4;

	public BarrothRamRun() {
		setAnimation("mhfc:models/Barroth/BarrothRamRun.mcanm");
		setLastFrame(LAST_FRAME);
	}

	@Override
	protected void beginExecution() {}

	@Override
	public float getWeight() {
		EntityBarroth entity = this.getEntity();
		target = entity.getAttackTarget();
		if (target == null) {
			return DONT_SELECT;
		}
		Vec3 toTarget = WorldHelper.getVectorToTarget(entity, target);
		double dist = toTarget.lengthVector();
		if (dist > MAX_DIST) {
			return DONT_SELECT;
		}
		return WEIGHT;
	}

	@Override
	public void update() {
		EntityBarroth entity = getEntity();
		AIUtils.damageCollidingEntities(getEntity(), damageCalc);
		if (this.getCurrentFrame() == 20) {
			getEntity().playSound("mhfc:barroth.ram", 3.0F, 1.0F);
			getEntity().getTurnHelper().updateTargetPoint(entity.getAttackTarget());
			AIGameplayComposition.AIChargeGameplay(entity, 1.9F, false);
		}
		if(this.getCurrentFrame() == 85){
			AIGameplayComposition.AILaunchGameply(entity, 1.0D, 1.5D, 1.0D);
		}
		if (isMoveForwardFrame(getCurrentFrame())) {
			entity.moveForward(1, false);
		}
	}

	private boolean isMoveForwardFrame(int frame) {
		return (frame > 20 && frame < 30);
	}
}