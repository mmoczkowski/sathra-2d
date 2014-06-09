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
package eu.sathra.ai;

import java.util.Random;

import eu.sathra.ai.context.AIContext;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;

/**
 * Runs randomly selected child task and returns it's status. If a child returns
 * <code>TaskResult.RUNNING</code>, it will be selected next frame.
 * 
 * @author Milosz Moczkowski
 * 
 */
public class RandomTask extends Task {

	private Task mCurrentTask;
	private Random mRandom = new Random();

	@Deserialize("children")
	@Defaults(Deserialize.NULL)
	public RandomTask(Task[] children) {
		super(children);
	}

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {
		Task myTask;

		if (mCurrentTask != null)
			myTask = mCurrentTask;
		else {
			int max = this.childCount();
			int random = mRandom.nextInt(max);
			myTask = getChild(random).getValue();
		}

		TaskResult result = myTask.execute(context, time, delta);

		if (result == TaskResult.RUNNING)
			mCurrentTask = myTask;
		else 
			mCurrentTask = null;

		return result;
	}

}
