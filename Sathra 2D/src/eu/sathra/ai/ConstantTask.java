package eu.sathra.ai;

import eu.sathra.ai.context.AIContext;
import eu.sathra.io.annotations.Deserialize;

/**
 * Returns predefined value. Useful as a placeholder or for debugging purposes.
 * 
 * @author mmoczkowski
 * 
 */
public class ConstantTask extends Task {

	private TaskResult mResult;
	
	@Deserialize("result")
	public ConstantTask(TaskResult result) {
		mResult = result;
	}

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {
		return mResult;
	}

}
