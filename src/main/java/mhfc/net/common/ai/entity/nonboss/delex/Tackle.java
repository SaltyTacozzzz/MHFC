/**
 *
 */
package mhfc.net.common.ai.entity.nonboss.delex;

import mhfc.net.common.ai.general.AIUtils;
import mhfc.net.common.ai.general.actions.DamagingAction;
import mhfc.net.common.ai.general.provider.adapters.AnimationAdapter;
import mhfc.net.common.ai.general.provider.adapters.AttackAdapter;
import mhfc.net.common.ai.general.provider.adapters.DamageAdapter;
import mhfc.net.common.ai.general.provider.composite.IAnimationProvider;
import mhfc.net.common.ai.general.provider.composite.IAttackProvider;
import mhfc.net.common.ai.general.provider.impl.IHasAttackProvider;
import mhfc.net.common.entity.monster.EntityDelex;
import mhfc.net.common.util.world.WorldHelper;
import net.minecraft.util.math.Vec3d;

/**
 * @author WorldSEnder
 *
 */
public class Tackle extends DamagingAction<EntityDelex> implements IHasAttackProvider {


	private final IAttackProvider ATTACK;
	{
		final IAnimationProvider ANIMATION = new AnimationAdapter(this, "mhfc:models/delex/tackle.mcanm", 25);
		ATTACK = new AttackAdapter(ANIMATION, new DamageAdapter(AIUtils.defaultDamageCalc(30f, 50F, 9999999f)));
	}

	public Tackle() {}

	@Override
	protected float computeSelectionWeight() {
		EntityDelex entity = this.getEntity();
		target = entity.getAttackTarget();

		if (this.getCurrentAnimation() != null) {
		if (target == null) {
			return DONT_SELECT;
		}
		Vec3d toTarget = WorldHelper.getVectorToTarget(entity, target);
		double dist = toTarget.lengthVector();
		if (dist >= entity.getDistanceToEntity(target) + 5) {
			return DONT_SELECT;
		}
		}
		return 5F;
	}

	@Override
	public IAttackProvider getAttackProvider() {
		return ATTACK;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (isMoveForwardFrame(getCurrentFrame())) {
			EntityDelex ent = getEntity();
			ent.moveForward(1, true);
		}

	}

	private static boolean isMoveForwardFrame(int frame) {
		return (frame > 14 && frame < 20);
	}
}