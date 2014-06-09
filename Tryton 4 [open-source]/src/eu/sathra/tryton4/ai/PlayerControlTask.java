package eu.sathra.tryton4.ai;

import eu.sathra.ai.TaskResult;
import eu.sathra.ai.context.AIContext;
import eu.sathra.scene.Transform;

public class PlayerControlTask extends PlayerTask {

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {
		
		Transform myTransform = context.getOwner().getTransform();
		myTransform.setRotation(getUIMediator().getRotation()-90);
		
		context.getOwner().setTransform(myTransform);
		
		return TaskResult.TRUE;
	}

}
