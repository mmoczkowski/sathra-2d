package eu.sathra.ai;

import eu.sathra.ai.context.AIContext;
import eu.sathra.io.annotations.Deserialize;

/**
 * WaitTask waits and returns <code>RUNNING</code> until the timer expires, then
 * returns <code>SUCCESS</code>. WaitTask never returns FAILURE.
 * 
 * @author Milosz Moczkowski
 * 
 */
public class WaitTask extends Task {

	private long mDuration;
	private long mStartTime = -1;

	@Deserialize("duration")
	public WaitTask(long duration) {
		mDuration = duration;
	}

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {
		if (mStartTime == -1) {
			mStartTime = time;
			return TaskResult.RUNNING;
		} else if (time - mStartTime <= mDuration) {
			return TaskResult.RUNNING;
		} else {
			mStartTime = -1;
			return TaskResult.TRUE;
		}
	}

}
