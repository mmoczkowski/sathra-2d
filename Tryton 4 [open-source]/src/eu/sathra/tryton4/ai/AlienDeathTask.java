package eu.sathra.tryton4.ai;

import eu.sathra.ai.Task;
import eu.sathra.ai.TaskResult;
import eu.sathra.ai.context.AIContext;
import eu.sathra.physics.Body;
import eu.sathra.physics.Physics;
import eu.sathra.scene.SceneNode;

public class AlienDeathTask extends Task {

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {
		
		SceneNode owner = context.getOwner();
		owner.setAIEnabled(false);//.setAI(null);
		Body myBody = owner.getBody();
		myBody.setCollisionMask(Physics.MASK_NONE);
		myBody.setVelocity(0, 0);
		myBody.setIsEnabled(false);
		
		
		return TaskResult.TRUE;
	}

}
