/*******************************************************************************
 * Copyright 2014 SATHRA Milosz Moczkowski, milosz.moczkowski@sathra.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
