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
package eu.sathra;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ConcurrentModificationException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import eu.sathra.Parameters.Orientation;
import eu.sathra.io.IO;
import eu.sathra.io.adapters.TypeAdapter;
import eu.sathra.physics.dyn4j.Dyn4jPhysics;
import eu.sathra.resources.ResourceManager;
import eu.sathra.resources.Texture;
import eu.sathra.scene.CameraNode;
import eu.sathra.scene.SceneNode;
import eu.sathra.util.Log;
import eu.sathra.util.Util;
import eu.sathra.video.opengl.Sprite;

public abstract class SathraActivity extends Activity implements
		android.opengl.GLSurfaceView.Renderer {

	private class SathraAdapter implements TypeAdapter<SathraActivity> {

		@Override
		public SathraActivity load(String param, JSONObject parent)
				throws JSONException {
			return SathraActivity.this;
		}
	}

	private class FPSCounterUpdater implements Runnable {

		@Override
		public void run() {
			mFPSCounterView.setText(String.format(FPS_TEXT_FORMAT, mFPS));
			if (mIsRunning)
				mFPSCounterView.postDelayed(this, 1000);
		}
	}

	private static final String KEY_WAS_ENGINE_INITIATED = "KEY_WAS_ENGINE_INITIATED";
	private static final String SURFACE_CREATE_MSG_FORMAT = "Surface created: [%d, %d]; Resolution: [%d, %d];";
	private static final float MILISECONDS_TO_SECONDS = 0.001F;
	private static final float FPS_VIEW_TEXT_SIZE = 20;
	private static final String FPS_TEXT_FORMAT = "FPS: %.0f";

	private static SathraActivity mCurrentSathra;

	private GLSurfaceView mSurfaceView;
	private Parameters mParams;
	private SceneNode mRootNode = new SceneNode();
	private long mLastDrawTimestamp;
	private long mTime;
	private long mVirtualTime;
	private long mTimeDelta;
	private long mVirtualTimeDelta;
	private TextView mFPSCounterView;
	private float mFPS = 0;
	private float mTimeScale;
	private boolean mIsRunning = false;
	private boolean mWasInitiated;
	private FPSCounterUpdater mFPSCounterUpdater = new FPSCounterUpdater();

	public static SathraActivity getCurrentSathra() {
		return mCurrentSathra;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCurrentSathra = this;
		mWasInitiated = false;

		ResourceManager.getInstance().unloadAll();
		
		mParams = getParameters();

		if (mParams.fullscreen) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		if (mParams.layout == 0) {
			mSurfaceView = new GLSurfaceView(this);
			setContentView(mSurfaceView);
		} else {
			setContentView(mParams.layout);
			mSurfaceView = (GLSurfaceView) findViewById(R.id.surface);
		}

		mSurfaceView.setRenderer(this);
		mSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		mSurfaceView.setPreserveEGLContextOnPause(true);

		// resize view to forced resolution
		if (mParams.width != 0 && mParams.height != 0)
			mSurfaceView.getHolder()
					.setFixedSize(mParams.width, mParams.height);

		SathraAdapter adapter = new SathraAdapter();

		IO.getInstance().registerAdapter(SathraActivity.class, adapter);
		IO.getInstance().registerAdapter(Context.class, adapter);

		mIsRunning = true;

		// setup FPS view

		showFPS(mParams.showFPS);

		setTimeScale(1);

		setAmbientColor(mParams.ambientColor);

		setRequestedOrientation(mParams.orientation == Orientation.VERTICAL ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
				: ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// new CameraNode(this, null, null, true, null, null, null, null);
		// mRootNode.addChild(CameraNode.getActiveCamera());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if(mCurrentSathra == this) {
			mCurrentSathra = null;
		}
		
		mSurfaceView.setVisibility(View.GONE);
	}

	@Override
	public void onStart() {
		super.onStart();
		mIsRunning = true;
	}

	@Override
	public void onStop() {
		super.onStop();
		mIsRunning = false;
	}

	@Override
	public void onResume() {
		super.onResume();
		// mSurfaceView.setVisibility(View.VISIBLE);
		mSurfaceView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		// mSurfaceView.setVisibility(View.GONE);
		mSurfaceView.onPause();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		if (!mWasInitiated) {
			// Enable Smooth Shading, default not really needed.
			gl.glShadeModel(GL10.GL_SMOOTH);
			// Depth buffer setup.
			gl.glClearDepthf(1.0f);
			// Enables depth testing.
			gl.glEnable(GL10.GL_DEPTH_TEST);
			// The type of depth testing to do.
			gl.glDepthFunc(GL10.GL_LEQUAL);
			// Really nice perspective calculations.
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

			gl.glEnable(GL10.GL_DITHER);
			gl.glEnable(GL10.GL_MULTISAMPLE);
			gl.glEnable(GL10.GL_BLEND);
			gl.glDisable(GL10.GL_CULL_FACE);

			mLastDrawTimestamp = System.currentTimeMillis();
		}

		Log.debug("Surface created");
	}

	// TODO: clean this
	public static int buf[] = new int[1];
	public static int tex[] = new int[1];
	Sprite shad;

	@SuppressLint("WrongCall")
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glViewport(0, 0, this.getScreenWidth(), this.getScreenHeight());
		// Update time variables
		mTimeDelta = System.currentTimeMillis() - mLastDrawTimestamp;
		mLastDrawTimestamp = System.currentTimeMillis();
		mTime += mTimeDelta;
		mVirtualTimeDelta = (long) (mTimeDelta * getTimeScale());
		mVirtualTime += mVirtualTimeDelta;

		// Setup defaults
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

		// Clear BG
		float r = (float) Color.red(mParams.bgColor) / 255;
		float g = (float) Color.green(mParams.bgColor) / 255;
		float b = (float) Color.blue(mParams.bgColor) / 255;
		float a = (float) Color.alpha(mParams.bgColor) / 255;
		gl.glClearColor(r, g, b, a);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);

		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		// gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

		mRootNode.onDraw(gl, mVirtualTime, mVirtualTimeDelta);

		// Draw lights and shadows
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glScalef(1, -1, 1); // Frame Buffers are upside down
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		CameraNode activeCam = CameraNode.getActiveCamera();

		if (activeCam != null) {
			shad.setPosition(0, 0);// this.getResolutionWidth()/2,
									// this.getResolutionHeight()/2);//activeCam.getAbsoluteX(),
									// activeCam.getAbsoluteY());
		}

		gl.glBlendFunc(GL10.GL_DST_COLOR, GL10.GL_ONE_MINUS_SRC_ALPHA);

		shad.draw(gl);
		
		gl.glViewport(0, 0, this.getResolutionWidth(), this.getResolutionHeight()); // SDfDSFSDFS
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, buf[0]);
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
				GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, tex[0], 0);

		// Clear shadow
		r = (float) Color.red(mParams.ambientColor) / 255;
		g = (float) Color.green(mParams.ambientColor) / 255;
		b = (float) Color.blue(mParams.ambientColor) / 255;
		a = (float) Color.alpha(mParams.ambientColor) / 255;

		gl.glClearColor(r, g, b, a);
		GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		
		// TODO: CONCURENT MOD EXCEPTION
		try {
			Dyn4jPhysics.getInstance().getWorld()
			.updatev(mVirtualTimeDelta * MILISECONDS_TO_SECONDS);
		} catch(ConcurrentModificationException e) {
			
		}
		

		// if (mIsRunning)
		onUpdate(mVirtualTime, mVirtualTimeDelta);

		// Update fps counter
		mFPS = 1000f / mTimeDelta;

		// mDebugView.postInvalidate();

		// System.gc();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (mParams.width == 0 && mParams.height == 0) {
			mParams.width = width;
			mParams.height = height;
		} else if (mParams.width == 0) {
			float screenRatio = width / (float) height;
			mParams.width = (int) (mParams.height * screenRatio);
		} else if (mParams.height == 0) {
			float screenRatio = height / (float) width;
			mParams.height = (int) (mParams.width * screenRatio);
		}

		gl.glViewport(0, 0, width, height);// mParams.width, mParams.height);

		Log.debug(String.format(SURFACE_CREATE_MSG_FORMAT, width, height, mParams.width, mParams.height));

		if (!mWasInitiated) {
			GLES20.glGenFramebuffers(1, buf, 0);
			GLES20.glGenTextures(1, tex, 0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex[0]);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			IntBuffer tmp = ByteBuffer
					.allocateDirect(mParams.width * mParams.height * 4)
					.order(ByteOrder.nativeOrder()).asIntBuffer();
			GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,
					mParams.width, mParams.height, 0, GLES20.GL_RGBA,
					GLES20.GL_UNSIGNED_SHORT_4_4_4_4, tmp);

			shad = new Sprite(
					new Texture(tex[0], mParams.width, mParams.height));
			shad.setPivot(0.5f, 0.5f);

			onEngineInitiated();

			mWasInitiated = true;
		}
	}

	/**
	 * Adds a SceneNode to root container
	 * 
	 * @param child
	 *            SceneNode to be added
	 */
	public void addNode(SceneNode child) {
		mRootNode.addChild(child);
	}

	public void addNodes(SceneNode[] children) {
		mRootNode.addChildren(children);
	}

	public void removeNode(SceneNode child) {
		mRootNode.removeChild(child);
	}

	public float getFPS() {
		return 1000f / mTimeDelta;
	}

	public void setAmbientColor(int light) {
		mParams.ambientColor = light;
	}

	/**
	 * Convenient method for for loading scene nodes from file.
	 * 
	 * @param filename
	 *            Path to a file located in assets folder
	 * @return Root scene node from file
	 * @throws Exception
	 */
	public SceneNode loadScene(String filename) throws Exception {
		JSONObject jObj = new JSONObject(Util.readFile(this, filename));
		SceneNode rootNode = IO.getInstance().load(jObj, SceneNode.class);
		// addNode(rootNode);

		return rootNode;
	}

	/**
	 * Hides or shows FPS counter in the top left of the screen.
	 * 
	 * @param show
	 *            If true, FPS counter will be shown
	 */
	public void showFPS(boolean show) {
		if (mFPSCounterView == null) {
			mFPSCounterView = new TextView(this);
			mFPSCounterView.setTextSize(FPS_VIEW_TEXT_SIZE);
			mFPSCounterView.setTextColor(Color.WHITE);
			((ViewGroup) findViewById(android.R.id.content))
					.addView(mFPSCounterView);
		}

		if (show)
			mFPSCounterView.post(mFPSCounterUpdater);

		mFPSCounterView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
	}

	public boolean isShowingFPS() {
		if (mFPSCounterView.getVisibility() == View.VISIBLE)
			return true;

		return false;
	}

	protected abstract Parameters getParameters();

	protected abstract void onEngineInitiated();

	protected abstract void onUpdate(long time, long timeDelta);

	// public float getMaxFPS() {
	// return SECONDS_TO_MILISECONDS/mMinimumFrameDuration;
	// }
	//
	// public void setMaxFPS(float limit) {
	// mMinimumFrameDuration = (long) (SECONDS_TO_MILISECONDS/limit);
	// }

	public float getTimeScale() {
		return mTimeScale;
	}

	public void setTimeScale(float scale) {
		mTimeScale = scale;
	}

	/**
	 * Returns time the application has been active
	 * 
	 * @return Uptime in milliseconds
	 */
	public long getTime() {
		return mTime;
	}

	/**
	 * Returns time affected by time scale
	 * 
	 * @return Scaled time in milliseconds
	 */
	public long getVirtualTime() {
		return mVirtualTime;
	}

	/**
	 * Returns duration of last frame
	 * 
	 * @return Duration of last frame in milliseconds
	 */
	public long getTimeDelta() {
		return mTimeDelta;
	}

	/**
	 * Returns the time the world has been advanced in last frame.
	 * 
	 * @return Scaled time in milliseconds
	 */
	public long getVirtualTimeDelta() {
		return mVirtualTimeDelta;
	}

	public int getScreenWidth() {
		return mSurfaceView.getWidth();
	}

	public int getScreenHeight() {
		return mSurfaceView.getHeight();
	}

	public int getResolutionWidth() {
		return mParams.width;
	}

	public int getResolutionHeight() {
		return mParams.height;
	}
}
