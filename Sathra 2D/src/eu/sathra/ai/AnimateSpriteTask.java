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
