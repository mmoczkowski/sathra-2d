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

import java.io.IOException;

import eu.sathra.ai.context.AIContext;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.resources.Sound;
import eu.sathra.resources.SoundManager;

public class PlaySoundTask extends Task {

	private Sound mSound;

	@Deserialize("sound")
	public PlaySoundTask(String soundPath) throws IOException {
		mSound = SoundManager.getInstance().load(soundPath);
	}

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {
		SoundManager.getInstance().play(mSound.getHandle(), 1, 1, 0, 0, 1);
		return TaskResult.TRUE;
	}

}
