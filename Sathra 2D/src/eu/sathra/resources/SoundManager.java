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
package eu.sathra.resources;

import java.io.IOException;

import eu.sathra.SathraActivity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager extends SoundPool implements ResourceLoader<Sound> {

	private static final int MAX_STREAMS = 32;

	private static SoundManager sInstance;

	public static SoundManager getInstance() {
		if (sInstance == null)
			sInstance = new SoundManager();

		return sInstance;
	}

	private SoundManager() {
		super(MAX_STREAMS, AudioManager.STREAM_MUSIC, 1);

	}

	@Override
	public Sound load(String path) throws IOException {
		AssetFileDescriptor descriptor = SathraActivity.getCurrentSathra()
				.getAssets().openFd(path);

		try {

			return new Sound(load(descriptor, 1), 0, 0);
		} finally {
			descriptor.close();
		}
	}

	@Override
	public void unload(Sound resource) {
		this.unload(resource.getHandle());
	}

}
