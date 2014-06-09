package eu.sathra.ai;

import eu.sathra.ai.context.AIContext;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;

/**
 * This task will iterate through each of it's children until one of them
 * returns <code>TaskResult.TRUE</code> or all of children will return
 * <code>TaskResult.FALSE</code>. If one of the children returns
 * <code>TaskResult.TRUE</code> or <code>TaskResult.RUNNING</code>, Selector
 * will also return this value. If all of children return TaskResult.FALSE,
 * selector also returns false.
 * 
 * @author Milosz Moczkowski
 * 
 */
public class SelectorTask extends Task {

	private int mCurrentNode = 0;

	@Deserialize("children")
	@Defaults(Deserialize.NULL)
	public SelectorTask(Task[] children) {
		super(children);
	}
	
	@Override
	public TaskResult execute(AIContext context, long time, long delta) {
		TaskResult myResult = TaskResult.FALSE;
		
		for (; mCurrentNode < childCount(); ++mCurrentNode) {
			Task myTask = getChild(mCurrentNode).getValue();
			myResult = myTask.execute(context, time, delta);

			switch (myResult) {
				case TRUE:
					mCurrentNode = 0;
				case RUNNING:
					return myResult;
				case FALSE:
					// noop
				break;
			}
		}

		mCurrentNode = 0;
		return myResult;
	}
}
