package eu.sathra.ai;

import java.util.Random;

import eu.sathra.ai.context.AIContext;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;

/**
 * Runs randomly selected child task and returns it's status. If a child returns
 * <code>TaskResult.RUNNING</code>, it will be selected next frame.
 * 
 * @author Milosz Moczkowski
 * 
 */
public class RandomTask extends Task {

	private Task mCurrentTask;
	private Random mRandom = new Random();

	@Deserialize("children")
	@Defaults(Deserialize.NULL)
	public RandomTask(Task[] children) {
		super(children);
	}

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {
		Task myTask;

		if (mCurrentTask != null)
			myTask = mCurrentTask;
		else {
			int max = this.childCount();
			int random = mRandom.nextInt(max);
			myTask = getChild(random).getValue();
		}

		TaskResult result = myTask.execute(context, time, delta);

		if (result == TaskResult.RUNNING)
			mCurrentTask = myTask;
		else 
			mCurrentTask = null;

		return result;
	}

}
