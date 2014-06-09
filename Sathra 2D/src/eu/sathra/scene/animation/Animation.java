package eu.sathra.scene.animation;

import android.view.animation.Interpolator;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.scene.Transform;

/**
 * Base class for SceneNode animations.
 * 
 * @author Milosz Moczkowski
 * 
 */
public class Animation {

	public interface Listener {
		public void onAnimationStopped(Animation anim);
	}
	
	private Interpolator mInterpolator;
	private Transform mFrom;
	private Transform mTo;
	private long mDuration;
	private long mCount;
	private boolean mReverse;
	private long mStartTime = -1;
	private boolean mIsPlaying;
	private Listener mListener;

	/**
	 * 
	 * @param interpolator
	 * @param from Start transform Transformation of this animation
	 * @param to Destination transform for this animation
	 * @param duration
	 *            How long each animation cycle will last in milliseconds.
	 * @param count
	 *            How many times this animation should be repeated. Pass -1 for
	 *            infinite loop.
	 * @param reverse
	 *            Should the animation be repeated in reverse order or start
	 *            from the beginning.
	 */
	@Deserialize({ "from", "to", "interpolator", "duration", "count", "reverse" })
	@Defaults({ Deserialize.NULL, Deserialize.NULL, "linear", "300", "-1",
			"true" })
	public Animation(Transform from, Transform to,
			Interpolator interpolator, long duration, int count, boolean reverse) {
		mInterpolator = interpolator;
		mFrom = from;
		mTo = to;
		mDuration = duration;
		mCount = count;
		mReverse = reverse;
		mIsPlaying = true;
	}

	public boolean animate(long delta, long time, Transform out) {
		if(!mIsPlaying) return false;
		
		if(mIsPlaying && mStartTime == -1) {
			mStartTime = time;
		}
		
		long elapsedTime = time - mStartTime; // how long is the amination running in milis
		long cyclesCompleted = elapsedTime/mDuration; // how many cycles have been completed
		
		if(mCount != -1 && cyclesCompleted >= mCount) {
			stop();
			return false;
		}
		
		float interpolatedTime = (elapsedTime%mDuration) / (float)mDuration;// - cyclesCompleted; // time of this cycle in [0, 1]
		
		if(cyclesCompleted%2 == 1 && mReverse) // should I reverse this cycle?
			interpolatedTime = 1 - interpolatedTime;
		
		float interpolatedValue = mInterpolator.getInterpolation(interpolatedTime);
		
		// TODO: optimize this!
		float x = mFrom.getX() + (mTo.getX()  - mFrom.getX()) * interpolatedValue;
		float y = mFrom.getY() + (mTo.getY()  - mFrom.getY())* interpolatedValue;
		float scaleX = mFrom.getScaleX() + (mTo.getScaleX()  - mFrom.getScaleX())* interpolatedValue;
		float scaleY = mFrom.getScaleY() + (mTo.getScaleY()  - mFrom.getScaleY())* interpolatedValue;
		float rotation = mFrom.getRotation() + (mTo.getRotation()  - mFrom.getRotation())* interpolatedValue;
		//Log.error(scaleX);
		out.set(x, y, rotation, scaleX, scaleY);
		
		return true;
	}
	
	public void start() {
		mIsPlaying = true;
	}
	
	public void stop() {
		mIsPlaying = false;
		mStartTime = -1;
		
		if(mListener != null)
			mListener.onAnimationStopped(this);
	}
	
	public void pause() {
		mIsPlaying = false;
	}
	
	public boolean isPlaying() {
		return mIsPlaying;
	}

	public Listener getListener() {
		return mListener;
	}

	public void setListener(Listener listener) {
		mListener = listener;
	}
	
	/**
	 * Returns the duration of a one cycle.
	 * @return Duration of one cycle in milliseconds
	 */
	public long getDuration() {
		return mDuration;
	}
	
	/**
	 * Returns total duration of this animation (count * cycle duration).
	 * @return Total duration of animation or -1 if infinite.
	 */
	public long getTotalDuration() {
		return mCount == -1? -1 : mDuration * mCount;
	}
}
