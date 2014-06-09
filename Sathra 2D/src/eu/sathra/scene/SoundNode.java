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
