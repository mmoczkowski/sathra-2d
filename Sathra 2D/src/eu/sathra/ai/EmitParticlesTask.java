package eu.sathra.ai;

import eu.sathra.ai.context.AIContext;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.scene.ParticleEmitterNode;

public class EmitParticlesTask extends Task {

	private ParticleEmitterNode mNode;
	private String mNodeId;
	private int mCount;
	
	@Deserialize({ "node_id", "count" })
	public EmitParticlesTask(String nodeId, int count) {
		mNodeId = nodeId;
		mCount = count;
		
	}

	public void onAttach(AIContext context) {
		mNode = (ParticleEmitterNode) context.getOwner().findChildById(mNodeId);
	}
	
	@Override
	public TaskResult execute(AIContext context, long time, long delta) {

		if(mNode == null)
			return TaskResult.FALSE;
		
		mNode.emit(mCount);

		return TaskResult.TRUE;
	}
}
