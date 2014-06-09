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

import eu.sathra.ai.context.AIContext;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.util.TreeNode;

/**
 * Iterates through children until one of them returns
 * <code>TaskResult.FALSE</code> or it reachs last subtask. If a child returns
 * <code>TaskResult.RUNNING</code> it will be picked up next frame. Returns
 * <code>TaskResult.TRUE</code> if all children succeeded.
 * 
 * @author Milosz Moczkowski
 * 
 */
public class SequencerTask extends Task {

	private int mCurrentTask;

	@Deserialize("children")
	@Defaults(Deserialize.NULL)
	public SequencerTask(Task[] children) {
		super(children);
	}

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {

		for (int c = (mCurrentTask != -1 ? mCurrentTask : 0); c < childCount(); ++c) {

			TreeNode<Task> node = getChild(c);
			
			TaskResult result = node.getValue().execute(context, time, delta);

			switch (result) {
				case RUNNING:
					mCurrentTask = c;
					return result;
	
				case FALSE:
					mCurrentTask = -1;
					return result;
	
				case TRUE:
					mCurrentTask = -1;
	
					break;
			}
		}

		// all children executed
		return TaskResult.TRUE;
	}
}
