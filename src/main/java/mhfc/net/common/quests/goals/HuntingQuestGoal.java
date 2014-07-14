package mhfc.net.common.quests.goals;

import mhfc.net.common.eventhandler.quests.QuestGoalEventHandler;
import mhfc.net.common.quests.QuestGoalSocket;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class HuntingQuestGoal extends QuestGoal
		implements
			NotifyableQuestGoal<LivingDeathEvent> {

	public HuntingQuestGoal(QuestGoalSocket socket, Class<?> goalClass,
			int goalNumber) {
		super(socket);
		this.goalClass = goalClass;
		this.goalNumber = goalNumber;
		this.currentNumber = 0;
		goalHandler = new QuestGoalEventHandler<LivingDeathEvent>(this);
	}

	private int goalNumber;
	private int currentNumber;
	private Class<?> goalClass;
	private QuestGoalEventHandler<LivingDeathEvent> goalHandler;

	@Override
	public boolean isFulfilled() {
		return currentNumber >= goalNumber;
	}

	@Override
	public boolean isFailed() {
		return false;
	}

	@Override
	public void reset() {
		currentNumber = 0;
	}

	@Override
	public void notifyOfEvent(LivingDeathEvent event) {
		if (goalClass.isAssignableFrom(event.entityLiving.getClass())) {
			++currentNumber;
			notifyOfStatus(isFulfilled(), isFailed());
		}
	}

	@Override
	public void setActive(boolean newActive) {
		goalHandler.setActive(newActive);
	}

}
