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
