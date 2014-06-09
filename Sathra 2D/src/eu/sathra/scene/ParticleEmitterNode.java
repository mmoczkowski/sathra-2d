package eu.sathra.scene;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.view.animation.LinearInterpolator;
import eu.sathra.ai.Task;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.physics.Body;
import eu.sathra.scene.animation.Animation;
import eu.sathra.video.opengl.Sprite;

public class ParticleEmitterNode extends SceneNode {

	/**
	 * Class describing the properties of generated particles.
	 * 
	 * @author Milosz Moczkowski
	 * 
	 */
	public static class EmitParameters {

		/**
		 * Sprite of particle.
		 */
		public Sprite particle;

		/**
		 * The minimum amount of particles to be spawned every second.
		 */
		public float minEmission;

		/**
		 * The maximum amount of particles to be spawned every second.
		 */
		public float maxEmission;

		/**
		 * The minimum lifetime of spawned particle every second.
		 */
		public long minLifetime;

		/**
		 * The maximum lifetime of spawned particle every second.
		 */
		public long maxLifetime;
		public Transform direction = new Transform();
		public Transform variance = new Transform();
		public float width;
		public float height;
		public boolean isEmitting;

		@Deserialize({ "particle", "is_emitting", "min_emision", "max_emision", "min_life",
				"max_life", "direction", "variance", "width", "height" })
		@Defaults({ "0", "true", "0", "0", "0", "0", Deserialize.NULL,
				Deserialize.NULL, "0", "0" })
		public EmitParameters(Sprite particle, boolean isEmitting, float minEmission,
				float maxEmission, long minLife, long maxLife,
				Transform direction, Transform variance, float width,
				float height) {

			this.particle = particle;
			this.minEmission = minEmission;
			this.maxEmission = maxEmission;
			this.minLifetime = minLife;
			this.maxLifetime = maxLife;
			this.direction = direction;
			this.variance = variance;
			this.width = width;
			this.height = height;
			this.isEmitting = isEmitting;
		}

	}

	// "Now, I am become Death, the destroyer of worlds."
	private class NodeDestroyer implements Animation.Listener {

		private SceneNode mChild;

		public NodeDestroyer(SceneNode child) {
			mChild = child;
		}

		@Override
		public void onAnimationStopped(Animation anim) {
			removeChild(mChild);
		}
	}

	private EmitParameters mParams;
	private int mParticlesToEmit;

	// Minimum time between consecutive emits (in millis)
	private float mMinEmitDelay;
	// Maximum time between consecutive emits (in millis)
	private float mMaxEmitDelay;
	// App time when next emit will occour
	private float mNextEmit;
	private Random mRandom = new Random();

	@Deserialize({ "id", "transform", "is_visible", "animation", "children",
			"body", "ai", "params" })
	@Defaults({ Deserialize.NULL, Deserialize.NULL, "true", Deserialize.NULL,
			Deserialize.NULL, Deserialize.NULL, Deserialize.NULL,
			Deserialize.NULL })
	public ParticleEmitterNode(String id, Transform t, boolean isVisible,
			Animation animation, SceneNode[] children, Body body, Task ai,
			EmitParameters params) {

		super(id, t, isVisible, animation, children, body, ai);

		setEmitParameters(params);
	}

	/**
	 * Emits given number of particles.
	 * @param count Number of particles to be emitted.
	 */
	public void emit(int count) {
		emit(true);
		mParticlesToEmit = count;
	}
	
	public void emit(boolean emit) {
		mParams.isEmitting = emit;
		mNextEmit = 0;
		mParticlesToEmit = -1;
	}

	public boolean isEmitting() {
		
		return mParams.isEmitting && mParticlesToEmit != 0;
	}

	/***
	 * 
	 * @param min
	 *            The minimum amount of particles to be spawned every second.
	 * @param max
	 *            The maximum amount of particles to be spawned every second.
	 */
	public void setParticleEmmision(float min, float max) {
		if (max < min)
			throw new IllegalArgumentException("max<min");

		mParams.minEmission = min;
		mParams.maxEmission = max;

		mMinEmitDelay = 1000 / mParams.minEmission;
		mMaxEmitDelay = 1000 / mParams.maxEmission;
	}

	/**
	 * 
	 * @param min
	 *            The minimum lifetime of spawned particle in milliseconds.
	 * @param max
	 *            The maximum lifetime of spawned particle in milliseconds.
	 */
	public void setParticleLifetime(long min, long max) {
		if (max < min)
			throw new IllegalArgumentException("max<min");

		mParams.minLifetime = min;
		mParams.maxLifetime = max;
	}

	// public void setParticleSize(float min, float max) {
	// if (max < min)
	// throw new IllegalArgumentException("max<min");
	//
	// mParams.minSize = min;
	// mParams.maxSize = max;
	// }

	public void setEmitParameters(EmitParameters params) {
		mParams = params;
		mMinEmitDelay = 1000 / mParams.minEmission;
		mMaxEmitDelay = 1000 / mParams.maxEmission;
		emit(params.isEmitting);
	}

	public EmitParameters getEmitParameters() {
		return mParams;
	}

	public void setParticle(Sprite particle) {
		mParams.particle = particle;
	}

	protected synchronized void draw(GL10 gl, long time, long delta) {
		
		if (isEmitting() && time > mNextEmit) {
			// time to emit
			final long lifespan = (long) (mParams.minLifetime + (mParams.maxLifetime - mParams.minLifetime)
					* mRandom.nextFloat());

			float scaleVariance = nextFloat();

			float scaleX = mParams.direction.getScaleX()
					+ mParams.variance.getScaleX() * scaleVariance;// scaleX*scaleVariance;
			float scaleY = mParams.direction.getScaleY()
					+ mParams.variance.getScaleY() * scaleVariance;

			// Calculate spawn point
			float spawnX = mParams.width * mRandom.nextFloat();
			float spawnY = mParams.height * mRandom.nextFloat();

			float destX = spawnX + mParams.direction.getX()
					+ mParams.variance.getX() * nextFloat();
			float destY = spawnY + mParams.direction.getY()
					+ mParams.variance.getY() * nextFloat();

			float destRot = mParams.direction.getRotation()
					+ mParams.variance.getRotation() * nextFloat();

			Transform from = new Transform(spawnX, spawnY,
					mParams.direction.getRotation(), scaleX, scaleY);
			Transform to = new Transform(destX, destY,
					destRot, scaleX, scaleY);

			Animation animation = new Animation(from, to,
					new LinearInterpolator(), lifespan, 1, false);

			SpriteNode particleNode = new SpriteNode(null, mParams.particle,
					null, true, animation, null, null, null);

			animation.setListener(new NodeDestroyer(particleNode));

			addChild(particleNode);

			// calc next emit time
			mNextEmit = time + mMinEmitDelay + (mMaxEmitDelay - mMinEmitDelay)
					* mRandom.nextFloat();
			
			--mParticlesToEmit;
		}
	}

	private float nextFloat() {
		return mRandom.nextFloat() * 2 - 1;
	}
}
