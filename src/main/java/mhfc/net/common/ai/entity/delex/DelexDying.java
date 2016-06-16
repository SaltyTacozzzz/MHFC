package mhfc.net.common.ai.entity.delex;

import mhfc.net.common.ai.general.actions.AIGeneralDeath;
import mhfc.net.common.entity.monster.EntityDelex;

public class DelexDying extends AIGeneralDeath<EntityDelex> {

	private static final String ANIMATION_LOCATION = "mhfc:models/Delex/DelexHurt.mcanm";
//git
	public DelexDying() {
		super(ANIMATION_LOCATION);
	}

}
