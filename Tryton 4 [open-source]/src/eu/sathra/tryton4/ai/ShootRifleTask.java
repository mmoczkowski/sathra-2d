/*******************************************************************************
 * Copyright 2014 SATHRA Milosz Moczkowski, milosz.moczkowski@sathra.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
