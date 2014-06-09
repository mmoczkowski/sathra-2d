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
