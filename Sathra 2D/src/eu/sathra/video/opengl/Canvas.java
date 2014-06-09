package eu.sathra.video.opengl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import eu.sathra.resources.ResourceManager;
import eu.sathra.resources.Texture;
import eu.sathra.scene.SceneNode;
import eu.sathra.video.opengl.Sprite.FitMode;

public class Canvas extends SceneNode {

	private Sprite mSprite;
	private int buf[] = new int[1];
	private int tex[] = new int[1];
	private Sprite mTest;
	
	public Canvas(int width, int height) {
		GLES20.glGenFramebuffers(1, buf, 0);
		GLES20.glGenTextures(1, tex, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex[0]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		IntBuffer tmp = ByteBuffer.allocateDirect(width * height * 4)
				.order(ByteOrder.nativeOrder()).asIntBuffer();
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width,
				height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_SHORT_4_4_4_4,
				tmp);

		mSprite = new Sprite(new Texture(tex[0], width, height));
		mSprite.setPivot(0.5f, 0.5f);
		
		clear();
		
		try {
			mTest = new Sprite(ResourceManager.getInstance().getHandler(
					"android.png", Texture.class),
					200, 200, FitMode.REPEAT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void drawSprite(Sprite sprite) {
		//mSpritesToBeDrawn.push(sprite);
	}

	public void clear() {

		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, buf[0]);
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
				GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, tex[0], 0);

		GLES20.glClearColor(0, 0, 1, .5f);
		GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Switch back to screen buffer
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
	}

	@Override
	protected void draw(GL10 gl, long time, long delta) {
		mSprite.draw(gl);
		// Switch to shadow buffer
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, buf[0]);
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
				GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
				tex[0], 0);

		//gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		mTest.setRotation(getTransform().getRotation());
		mTest.draw(gl);

		// Restore previous blend
		//gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		// Switch back to screen buffer
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

		
	}
}
