package eu.sathra.tryton4.ai;

import java.util.Random;

import eu.sathra.ai.Task;
import eu.sathra.ai.TaskResult;
import eu.sathra.ai.context.AIContext;
import eu.sathra.scene.SceneNode;

public class AlienAttackTask extends Task {

	private static int ALIEN_DAMAGE = 5;
	private static int ALIEN_DAMAGE_VARIANCE = 5;
	
	private static Random sRandom = new Random();
	
	@Override
	public TaskResult execute(AIContext context, long time, long delta) {
		
		SceneNode playerNode = (SceneNode) context.getVariable(ContextKeys.KEY_PLAYER);
		AIContext playersContext = playerNode.getAIContext();
		int damage = (Integer) playersContext.getVariable(ContextKeys.KEY_DAMAGE);
		damage += ALIEN_DAMAGE + sRandom.nextInt(ALIEN_DAMAGE_VARIANCE);
		playersContext.setVariable(ContextKeys.KEY_DAMAGE, damage);
		
		return TaskResult.TRUE;
	}

}
