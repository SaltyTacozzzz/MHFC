package mhfc.net.common.ai.entity.tigrex;

import mhfc.net.common.ai.general.actions.AIGeneralWander;
import mhfc.net.common.ai.general.provider.IAnimationProvider;
import mhfc.net.common.ai.general.provider.IMoveParameterProvider;
import mhfc.net.common.ai.general.provider.IWeightProvider;
import mhfc.net.common.entity.monster.EntityTigrex;

public class TigrexWander extends AIGeneralWander<EntityTigrex> {

	private static final IAnimationProvider animationProvider = new IAnimationProvider.AnimationAdapter(
		"mhfc:models/Tigrex/walk.mcanm", 40);
	private static final IWeightProvider<EntityTigrex> weightProvider = new IWeightProvider.SimpleWeightAdapter<EntityTigrex>(
		50);
	private static final IMoveParameterProvider parameterProvider = new IMoveParameterProvider.MoveParameterAdapter(
		10f, 1f);

	public TigrexWander() {
		super(animationProvider, weightProvider, parameterProvider);
	}

	@Override
	protected void beginExecution() {
		super.beginExecution();
		// MHFCMain.logger.info("Wander selected");
	}

}