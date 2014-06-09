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

import android.graphics.Rect;
import eu.sathra.ai.Task;
import eu.sathra.ai.TaskResult;
import eu.sathra.ai.context.AIContext;
import eu.sathra.physics.Body;
import eu.sathra.physics.dyn4j.Dyn4jPhysics;
import eu.sathra.scene.Transform;

public class MoveTask extends Task {

	private static final float AHEAD_DISTANCE = 300;
	private static final float MAX_STEERING_FORCE = 400;

	private float mDestinationX;
	private float mDestinationY;
	private float mSpeed;

	public MoveTask() {
		mDestinationX = 0;
		mDestinationY = 0;
		mSpeed = 10;
	}

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {

		Body myBody = context.getOwner().getBody();		
		Rect bodyBounds = myBody.getBounds();

		// Calculate thrust force
		float forceX = mDestinationX - bodyBounds.centerX();
		float forceY = mDestinationY - bodyBounds.centerY();

		// normalize
		float distance = (float) Math.sqrt(forceX * forceX + forceY * forceY);

		if(distance < 126) {
			myBody.setVelocity(0, 0);
			return TaskResult.FALSE;
		
		}
		forceX /= distance;
		forceY /= distance;
		forceX *= mSpeed;
		forceY *= mSpeed;

		List<Body> obstacles = Dyn4jPhysics.getInstance().sphereCast(
				bodyBounds.centerX(), bodyBounds.centerY(), AHEAD_DISTANCE);

		for (Body obstacle : obstacles) {
			if (obstacle == context.getOwner().getBody())
				continue;

			Rect obstacleBounds = obstacle.getBounds();
			
			float repulsionX = (bodyBounds.centerX() - obstacleBounds.centerX());// - (bodyBounds.width()/2 + obstacleBounds.width()/2);
			float repulsionY = (bodyBounds.centerY() - obstacleBounds.centerY());// - (bodyBounds.height()/2 + obstacleBounds.height()/2);

			distance = (float) Math.sqrt(repulsionX * repulsionX + repulsionY
					* repulsionY);
			
			repulsionX /= distance;
			repulsionY /= distance;

			float forceRatio = (float) (1/Math.max(Math.pow(distance, 2), 1));
			
			forceX += repulsionX * forceRatio * 2000;
			forceY += repulsionY * forceRatio * 2000;
		}
		

		// add some randomness
//		forceX += (mRandom.nextFloat()*2-1)*10;
//		forceY += (mRandom.nextFloat()*2 -1)*10;
		
		myBody.setVelocity(forceX, forceY);
		
		Transform tmp = myBody.getTransform();
		tmp.setRotation(-(float) Math.toDegrees(Math.atan2(bodyBounds.centerX(), bodyBounds.centerY())) - 90);
		myBody.setTransform(tmp);
		
		return TaskResult.TRUE;
	}
}
