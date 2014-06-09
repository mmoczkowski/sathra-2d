package eu.sathra.tryton4.ai;

import java.util.List;
import java.util.Random;

import eu.sathra.ai.TaskResult;
import eu.sathra.ai.context.AIContext;
import eu.sathra.physics.Body;
import eu.sathra.physics.dyn4j.Dyn4jPhysics;
import eu.sathra.tryton4.C;

public class ShootRifleTask extends PlayerTask {

	private static final int RIFLE_DAMAGE = 10;
	private static final int DELAY = 100;
	private long mLastShootTime;
	private Random mRandom = new Random();

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {

		if (time <= mLastShootTime + DELAY) {
			// not yet
			return TaskResult.TRUE;
		}

		float angle = context.getOwner().getTransform().getRotation() - 90;
		float x = -(float) Math.sin(Math.toRadians(angle)) * 1000;
		float y = (float) Math.cos(Math.toRadians(angle)) * 1000;
		List<Body> hits = Dyn4jPhysics.getInstance().rayCast(0, 0, x, y,
				C.MASK_ALIEN);

		for (Body myBody : hits) {
			myBody.getSceneNode().getAIContext()
					.setVariable(ContextKeys.KEY_DAMAGE, RIFLE_DAMAGE + mRandom.nextInt(10));
		}

		mLastShootTime = time;

		return TaskResult.TRUE;

	}

}
