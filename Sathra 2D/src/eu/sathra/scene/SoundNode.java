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
package eu.sathra.scene;

import javax.microedition.khronos.opengles.GL10;

import eu.sathra.ai.Task;
import eu.sathra.physics.Body;
import eu.sathra.resources.Sound;
import eu.sathra.resources.SoundManager;
import eu.sathra.scene.animation.Animation;

public class SoundNode extends SceneNode {

	public enum Falloff {
		LINEAR, LOGARITHMIC, PARABOLIC
	}

	public class Properties {

		public Properties(float volume, float range, float pitch,
				Falloff falloff, Sound clip) {

			this.volume = volume;
			this.range = range;
			this.pitch = pitch;
			this.falloff = falloff;
			this.clip = clip;
		}

		public float volume;
		public float range;
		public float pitch;
		public Falloff falloff;
		public Sound clip;
		// TODO: doppler, effects, loop, count
	}

	private Properties mProperties;

	public SoundNode(String id, Properties properties, Transform transform,
			boolean isVisible, Animation animation, SceneNode[] children,
			Body body, Task ai) {

		super(id, transform, isVisible, animation, children, body, ai);
		setProperties(properties);
	}

	public void setProperties(Properties properties) {
		mProperties = properties;
	}

	public Properties getProperties() {
		return mProperties;
	}

	public void play() {
		if (mProperties.clip != null)
			SoundManager.getInstance().play(mProperties.clip.getHandle(), 1, 1,
					1, 1, 1);
	}

	@Override
	protected void draw(GL10 gl, long time, long delta) {

	}
}
