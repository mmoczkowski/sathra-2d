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
