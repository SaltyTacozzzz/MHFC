package mhfc.net.common.ai.rathalos;

import mhfc.net.common.ai.ActionAdapter;
import mhfc.net.common.entity.mob.EntityRathalos;

public class BiteAttack extends ActionAdapter<EntityRathalos> {

	public static final int LAST_FRAME = 40;
	private static float WEIGHT = 3.0f;

	public BiteAttack() {
		setAnimation("mhfc:models/Rathalos/RathalosBiteLeft.mcanm");
		setLastFrame(LAST_FRAME);
	}

	@Override
	public void update() {
	}

	@Override
	public float getWeight() {
		if (getEntity().getAttackManager().getCurrentStance() != EntityRathalos.Stances.GROUND)
			return DONT_SELECT;
		return WEIGHT;
	}

}
