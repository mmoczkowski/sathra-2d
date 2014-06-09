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
