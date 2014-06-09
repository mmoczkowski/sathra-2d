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

import java.util.ArrayList;
import java.util.List;

import eu.sathra.ai.context.AIContext;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.scene.SceneNode;
import eu.sathra.scene.animation.Animation;
import eu.sathra.util.Log;

// TODO: infinite animations
public class AnimateTask extends Task {

	private SceneNode mNode;
	private String mNodeId;
	private boolean mAnimateChildren;
	private boolean mPlayed = false;
	private List<Animation> mAnimations;

	@Deserialize({ "node", "animate_children"})
	public AnimateTask(String nodeId, boolean animateChildren) {
		mNodeId = nodeId;
		mAnimateChildren = animateChildren;
		mAnimations = new ArrayList<Animation>();
	}

	public void onAttach(AIContext context) {
		mNode = context.getOwner().findChildById(mNodeId);
	}

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {

		if(mNode == null)
			return TaskResult.FALSE;
		
		if(areAnimationsPlaying()) { 
			return TaskResult.RUNNING;
		} else {
			Log.error(mPlayed);
			if(mPlayed) {
				mPlayed = false;
				return TaskResult.TRUE;
			} else {
				mPlayed = true;
				startAnimations(mNode);
				return TaskResult.RUNNING;
			}
		}
	}
	
	private void startAnimations(SceneNode node) {
		startAnimation(node);
		
		if(mAnimateChildren) {
			for(SceneNode child : node.getChildren()) {
				startAnimations(child);
				//startAnimation(child);
			}
		}
	}
	
	private void startAnimation(SceneNode node) {
		Animation animation = node.getAnimation();
		if(animation != null) {
			node.startAnimation();
			mAnimations.add(animation);
		}
	}

	private boolean areAnimationsPlaying() {
		for(Animation animation : mAnimations) {
			if(animation.isPlaying()) return true;
		}
		
		return false;
	}
}
