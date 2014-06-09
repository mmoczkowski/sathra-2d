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
