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
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.scene.AnimatedSpriteNode;

public class AnimateSpriteTask extends Task {

	private AnimatedSpriteNode mNode;
	private String mNodeId;
	private int mCount;
	private boolean mPlayed = false;

	@Deserialize({ "node", "count" })
	public AnimateSpriteTask(String nodeId, int count) {
		mNodeId = nodeId;
		mCount = count;
	}

	public void onAttach(AIContext context) {
		mNode = (AnimatedSpriteNode) context.getOwner().findChildById(mNodeId);
	}

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {
		
		
		if(mNode == null)
			return TaskResult.FALSE;
		
		if(mNode.isPlaying()) { 
			if(mNode.getTotalDuration() == -1)
				return TaskResult.TRUE;
			else {
				return TaskResult.RUNNING;
			}
		} else {
			
			if(mPlayed) {
				mPlayed = false;
				return TaskResult.TRUE;
			} else {
				mPlayed = true;
				mNode.play(mCount);
				return TaskResult.RUNNING;
			}
		}
	}
}
