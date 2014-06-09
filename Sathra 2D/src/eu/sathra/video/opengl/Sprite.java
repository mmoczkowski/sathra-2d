package eu.sathra.video.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Rect;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.resources.Texture;

public class Sprite extends Plane {

	public enum FitMode {
		REPEAT, STRETCH, STRETCH_HORIZONTALY, STRECH_VERTICALY
	}

	private Texture mTexture;
	private FloatBuffer mUV;

	public Sprite(Texture texture) {
		this(texture, texture.getWidth(), texture.getHeight(), new Rect(0, 0,
				texture.getWidth(), texture.getHeight()), 0.5f, 0.5f,
				FitMode.REPEAT);
	}

	public Sprite(Texture texture, Rect clip) {
		this(texture, 0, 0, clip, 0.5f,
				0.5f, FitMode.REPEAT);
	}

	public Sprite(Texture texture, Rect clip, float anchorX, float anchorY) {
		this(texture, clip.width(), clip.height(), clip, anchorX, anchorY,
				FitMode.REPEAT);
	}

	public Sprite(Texture texture, int width, int height, float anchorX,
			float anchorY, FitMode mode) {
		this(texture, width, height, new Rect(0, 0, texture.getWidth(),
				texture.getHeight()), anchorX, anchorY, mode);
	}

	public Sprite(Texture texture, int width, int height, FitMode mode) {
		this(texture, width, height, new Rect(0, 0, width, height), 0.5f, 0.5f,
				mode);
	}

	@Deserialize({ "texture", "width", "height", "clip", "pivot_x", "pivot_y",
			"fit" })
	@Defaults({ Deserialize.NULL, "0", "0",
			Deserialize.NULL, "0.5", "0.5", "REPEAT" })
	public Sprite(Texture texture, int width, int height, Rect clip,
			float pivotX, float pivotY, FitMode mode) {

		mTexture = texture;
		
		/*
		 * If explicitly specified w or h are equal 0, get dimensions from clip.
		 * If clip null, get set dimensions of a texture.
		 */
		if (width == 0 || height == 0) {
			if(clip != null) {
				setSize(width == 0? clip.width() : width, height == 0? clip.height() : height);
			} else {
				setSize(width == 0? texture.getWidth() : width, height == 0? texture.getHeight() : height);
			}
		} else {
			setSize(width, height);
		}
		
		if(clip == null)
			clip = new Rect(0, 0, texture.getWidth(), texture.getHeight());

		 switch (mode) {
			 case REPEAT:
				 clip.set(clip.left, clip.top, clip.left+getWidth(), clip.top+getHeight());
			 break;
			 
			 case STRETCH:
				 clip.set(clip.left, clip.top,clip.left+ texture.getWidth(),
						 clip.top+texture.getHeight());
			 break;
			 
			 case STRETCH_HORIZONTALY:
				 clip.set(clip.left, clip.top, texture.getWidth(),
				 height);
			 break;
			 
			 case STRECH_VERTICALY:
				 clip.set(clip.left, clip.top, width,
				 texture.getHeight());
			 break;
		 }
		 
		float coordinates[] = new float[8];
		
		float[] srcX = { clip.left, clip.left, clip.right, clip.right };
		float[] srcY = { clip.top, clip.bottom, clip.bottom, clip.top };
		
		for (int i = 0; i < 4; i++) {
			coordinates[i * 2] = (srcX[i] / texture.getWidth());
			coordinates[i * 2 + 1] = (srcY[i] / texture.getHeight());
		}
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(coordinates.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		mUV = byteBuf.asFloatBuffer();
		mUV.put(coordinates);
		mUV.position(0);
		
		setPivot(pivotX, pivotY);
	}

	@Override
	public void draw(GL10 gl) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		// Enable the texture state
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		// Point to our buffers
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mUV);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture.getHandle());

		//gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT); // Repeat on X axis
        //gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);  // Stretch on Y axis
		
		super.draw(gl);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}

	public void drawToSprite() {
		
	}
}
