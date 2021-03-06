package mhfc.net.common.ai.entity.monsters.nargacuga;

import mhfc.net.common.ai.general.actions.AnimatedAction;
import mhfc.net.common.ai.general.provider.adapters.AnimationAdapter;
import mhfc.net.common.ai.general.provider.composite.IAnimationProvider;
import mhfc.net.common.ai.general.provider.impl.IHasAnimationProvider;
import mhfc.net.common.entity.monster.EntityNargacuga;
import mhfc.net.common.util.world.WorldHelper;
import net.minecraft.util.math.Vec3d;

public class ProwlerStance extends AnimatedAction<EntityNargacuga> implements IHasAnimationProvider {




	public ProwlerStance() {}


	@Override
	protected float computeSelectionWeight() {
		EntityNargacuga e = this.getEntity();
		target = e.getAttackTarget();
		if (target == null) {
			return DONT_SELECT;
		}
		Vec3d toTarget = WorldHelper.getVectorToTarget(e, target);
		double dist = toTarget.lengthVector();
		if (dist > 25) {
			return DONT_SELECT;
		}
		return 15;
	}

	@Override
	public IAnimationProvider getAnimProvider() {
		return new AnimationAdapter(this, "mhfc:models/Nargacuga/Pounce.mcanm", 18);
	}

	@Override
	protected void onUpdate() {
		EntityNargacuga entity = getEntity();
		target = entity.getAttackTarget();

		if (target != null) {
			entity.getTurnHelper().updateTurnSpeed(8F);
			entity.getTurnHelper().updateTargetPoint(target);
		}
	}
}
