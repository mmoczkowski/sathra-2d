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
 * Base class for AI task. A task is a leaf in behaviour tree. Each task can
 * have indefinite number of children.
 * 
 * @author Milosz Moczkowski
 * 
 */
public abstract class Task extends TreeNode<Task> {

	public Task() {
		this(null);
	}
	
	@Deserialize("children")
	@Defaults(Deserialize.NULL)
	public Task(Task[] children) {
		if (children != null)
			addChildren(children);
		
		setValue(this);
	}

	/**
	 * Called by owning SceneNode. Do not call directly!
	 * @param context
	 */
	public void onAttach(AIContext context) { 
		// noop 
	}

	/**
	 * Subclasses should implement this method.
	 * 
	 * @param context
	 * @param time
	 * @param delta
	 * @return
	 */
	public abstract TaskResult execute(AIContext context, long time, long delta);
}
