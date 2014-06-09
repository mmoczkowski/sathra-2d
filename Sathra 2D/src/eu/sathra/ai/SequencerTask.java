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
