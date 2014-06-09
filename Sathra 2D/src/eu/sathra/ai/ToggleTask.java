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
import eu.sathra.scene.SceneNode;

public class ToggleTask extends Task {

	private String mNodeId;
	private boolean mToggleSiblings;
	private SceneNode mNode;
	
	@Deserialize({"node_id", "toggle_siblings"})
	@Defaults({Deserialize.NULL, "false"})
	public ToggleTask(String nodeId, boolean toggleSiblings) {
		mNodeId = nodeId;
		mToggleSiblings = toggleSiblings;
	}
	
	@Override
	public void onAttach(AIContext context) {
		super.onAttach(context);

		mNode = context.getOwner().findChildById(mNodeId);
	}
	
	@Override
	public TaskResult execute(AIContext context, long time, long delta) {

		if(mNode == null)
			return TaskResult.FALSE;
		
		if(mNode.isVisible()) // Don't toggle twice
			return TaskResult.TRUE;

		if(mToggleSiblings) {
			for(SceneNode node : mNode.getParent().getChildren()) {
				if(node != mNode)
					node.setVisible(false);
			}
		}
		
		mNode.setVisible(true);
		
		return TaskResult.TRUE;
	}

}
