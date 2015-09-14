package mhfc.net.common.ai.general.provider;

import mhfc.net.common.ai.IExecutableAction;
import net.minecraft.entity.EntityLiving;

public interface IContinuationPredicate<EntityT extends EntityLiving> {

	public boolean shouldContinueAction(IExecutableAction<EntityT> attack,
		EntityT actor);

	public static class HasTargetAdapter<EntityT extends EntityLiving>
		implements
			IContinuationPredicate<EntityT> {

		@Override
		public boolean shouldContinueAction(IExecutableAction<EntityT> attack,
			EntityT actor) {
			return actor.getAttackTarget() != null;
		}

	}

	public static class HasNoTargetAdapter<EntityT extends EntityLiving>
		implements
			IContinuationPredicate<EntityT> {

		@Override
		public boolean shouldContinueAction(IExecutableAction<EntityT> attack,
			EntityT actor) {
			return actor.getAttackTarget() == null;
		}

	}

}
