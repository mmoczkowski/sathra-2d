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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import eu.sathra.ai.Task;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.physics.Body;
import eu.sathra.scene.animation.Animation;
import eu.sathra.video.opengl.Sprite;

public class AnimatedSpriteNode extends SceneNode {

	private static final long DEFAULT_DURATION = 300;

	private long mDuration = DEFAULT_DURATION;
	private float mFrameDuration;
	private boolean mIsPlaying;
	private long mTotalDuration;
	private long mTimer;
	private List<Sprite> mFrames = new ArrayList<Sprite>();

	@Deserialize({ "id", "transform", "is_visible", "frames", "duration",
			"is_playing", "animation", "children", "body", "ai" })
	@Defaults({ Deserialize.NULL, Deserialize.NULL, "true", Deserialize.NULL, "0",
			"true", Deserialize.NULL, Deserialize.NULL, Deserialize.NULL,
			Deserialize.NULL })
	public AnimatedSpriteNode(String id, Transform t, boolean isVisible,
			Sprite[] frames, long duration, boolean isPlaying,
			Animation animation, SceneNode[] children, Body body, Task ai) {
		super(id, t, isVisible, animation, children, body, ai);
		setFrames(frames);
		setDuration(duration);
		if(isPlaying) play();
	}

	@Override
	protected void draw(GL10 gl, long time, long delta) {

		if(mTimer <= mTotalDuration || mTotalDuration == -1) {
			int frame = (int) (mTimer / mFrameDuration) % mFrames.size();
			mFrames.get(frame).setRotation(this.getTransform().getRotation());
			mFrames.get(frame).draw(gl);
			mTimer += delta;
		} else {
			mIsPlaying = false;
		}
	}

	public void addFrame(Sprite frame) {
		mFrames.add(frame);
		mFrameDuration = mDuration / mFrames.size();
	}

	public void setFrames(Sprite[] frames) {
		mFrames.addAll(Arrays.asList(frames));
		mFrameDuration = mDuration / mFrames.size();
	}

	public void setDuration(long duration) {
		mDuration = duration;
		mFrameDuration = mDuration / mFrames.size();
	}

	public long getDuration() {
		return mDuration;
	}

	public long play() {
		mIsPlaying = true;
		mTotalDuration = -1;
		mTimer = 0;
		
		return mDuration;
	}
	
	public long play(int count) {
		mIsPlaying = true;
		mTotalDuration = getDuration()*count;
		mTimer = 0;
		
		return mTotalDuration;
	}

	public void stop() {
		mIsPlaying = false;
	}
	
	public boolean isPlaying() {
		return mIsPlaying;
	}
	
	public long getTotalDuration() {
		return mTotalDuration;
	}

}
