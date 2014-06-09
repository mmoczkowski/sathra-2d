package eu.sathra.scene;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import eu.sathra.SathraActivity;
import eu.sathra.ai.Task;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.physics.Body;
import eu.sathra.scene.animation.Animation;
import eu.sathra.video.opengl.Sprite;

public class LightNode extends SceneNode {
	private Sprite mSprite;

	public LightNode(Sprite sprite) {
		this(null, sprite, new Transform(), true, null, null, null, null);
	}

	@Deserialize({ "id", "sprite", "transform", "is_visible", "animation",
			"children", "body", "ai" })
	@Defaults({ Deserialize.NULL, Deserialize.NULL, Deserialize.NULL, "true",
			Deserialize.NULL, Deserialize.NULL, Deserialize.NULL,
			Deserialize.NULL })
	public LightNode(String id, Sprite sprite, Transform t, boolean isVisible,
			Animation animation, SceneNode[] children, Body body, Task ai) {
		super(id, t, isVisible, animation, children, body, ai);
		mSprite = sprite;
	}

	@Override
	protected void draw(GL10 gl, long time, long delta) {
		gl.glViewport(0, 0, SathraActivity.getCurrentSathra().getResolutionWidth(), SathraActivity.getCurrentSathra().getResolutionHeight()); // SDfDSFSDFS
		// Switch to shadow buffer
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, SathraActivity.buf[0]);
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
				GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
				SathraActivity.tex[0], 0);

// get and save current blend func
//		int blend[] = new int[2];
//		GLES20.glGetIntegerv(GLES20.GL_BLEND_SRC_ALPHA, blend, 0);
//		GLES20.glGetIntegerv(GLES20.GL_BLEND_DST_ALPHA, blend, 1);

		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		mSprite.setRotation(getTransform().getRotation());
		mSprite.draw(gl);

		// Restore previous blend
		//gl.glBlendFunc(blend[0], blend[1]);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		// Switch back to screen buffer
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		gl.glViewport(0, 0, SathraActivity.getCurrentSathra().getScreenWidth(), SathraActivity.getCurrentSathra().getScreenHeight());
	}
}
