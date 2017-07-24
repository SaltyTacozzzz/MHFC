package mhfc.net.common.ai.entity.monsters.deviljho;

import mhfc.net.common.ai.general.SelectionUtils;
import mhfc.net.common.ai.general.actions.AnimatedAction;
import mhfc.net.common.ai.general.provider.adapters.AnimationAdapter;
import mhfc.net.common.ai.general.provider.composite.IAnimationProvider;
import mhfc.net.common.ai.general.provider.impl.IHasAnimationProvider;
import mhfc.net.common.entity.monster.EntityDeviljho;
import mhfc.net.common.entity.projectile.EntityBeam;

public class LaserBeam extends AnimatedAction<EntityDeviljho> implements IHasAnimationProvider {

	protected String animLocation;
	protected int animLength;
	protected float maxDistance;
	private EntityBeam solarBeam;
	protected float weight;

	public LaserBeam(String animLocation, int animLength, float maxDistance, float weight) {
		this.animLocation = animLocation;
		this.animLength = animLength;
		this.maxDistance = maxDistance;
		this.weight = weight;

	}


	@Override
	public IAnimationProvider getAnimProvider() {
		return new AnimationAdapter(this, "mhfc:models/deviljho/deviljhofrontalbreathe.mcanm", 80);
	}

	@Override
	protected float computeSelectionWeight() {
		EntityDeviljho entity = this.getEntity();
		target = entity.getAttackTarget();
		if (SelectionUtils.isIdle(entity)) {
			return DONT_SELECT;
		}
		if (!SelectionUtils.isInDistance(0, 15, entity, target)) {
			return DONT_SELECT;
		}
		return 25;
	}

	@Override
	protected void onUpdate() {
		EntityDeviljho entity = this.getEntity();
		target = entity.getAttackTarget();
		float radius1 = 1.7f;
		if (target != null) {
			entity.getTurnHelper().updateTurnSpeed(30F);
			entity.getTurnHelper().updateTargetPoint(target);
		}
		if (this.getCurrentFrame() == 40 && entity.getAttackTarget() != null && !entity.world.isRemote) {

			solarBeam = new EntityBeam(
					entity.world,
					entity,
					entity.posX + radius1 * Math.sin(-entity.rotationYaw * Math.PI / 180),
					entity.posY + 6F,
					entity.posZ + radius1 * Math.cos(-entity.rotationYaw * Math.PI / 180),
					(float) ((entity.rotationYawHead + 90) * Math.PI / 180),
					(float) (-entity.rotationPitch * Math.PI / 180),
					55);
			entity.world.spawnEntity(solarBeam);
			}
		}
	}

