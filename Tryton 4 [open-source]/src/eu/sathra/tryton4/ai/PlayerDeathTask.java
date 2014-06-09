package eu.sathra.tryton4.ai;

import eu.sathra.SathraActivity;
import eu.sathra.ai.Task;
import eu.sathra.ai.TaskResult;
import eu.sathra.ai.context.AIContext;
import eu.sathra.scene.SceneNode;

public class PlayerDeathTask extends Task {

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {
		
		SceneNode owner = context.getOwner();
		SathraActivity.getCurrentSathra().setTimeScale(0);
		owner.setAIEnabled(false);
		
		
		return TaskResult.TRUE;
	}

}
