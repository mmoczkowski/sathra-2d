package eu.sathra.scene;

import javax.microedition.khronos.opengles.GL10;

import eu.sathra.SathraActivity;
import eu.sathra.ai.Task;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.physics.Body;
import eu.sathra.scene.animation.Animation;

// TODO: aspect ratio
public class CameraNode extends SceneNode {

	private static CameraNode sActive = null;

	private SathraActivity mSathra;

	public static CameraNode getActiveCamera() {
		return sActive;
	}

	@Deserialize({ Deserialize.NULL, "id", "transform", "is_visible",
			"animation", "children", "body", "ai" })
	@Defaults({ Deserialize.NULL, Deserialize.NULL, Deserialize.NULL, "true",
			Deserialize.NULL, Deserialize.NULL, Deserialize.NULL,
			Deserialize.NULL })
	public CameraNode(SathraActivity sathra, String id, Transform t,
			boolean isVisible, Animation animation, SceneNode[] children,
			Body body, Task ai) {
		super(id, t, isVisible, animation, children, body, ai);

		mSathra = sathra;

	}

	@Override
	protected void draw(GL10 gl, long time, long delta) {

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glScalef(getAbsoluteScaleX(), getAbsoluteScaleY(), 1);
		gl.glTranslatef(-getAbsoluteX() / (mSathra.getResolutionWidth() * 0.5f)
				+ 1f, -getAbsoluteY() / (mSathra.getResolutionHeight() * 0.5f)
				+ 1, 0);

		gl.glOrthof(0, mSathra.getResolutionWidth(), 0,
				mSathra.getResolutionHeight(), -1, 1);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
	}

	@Override
	public void setVisible(boolean isVisible) {
		super.setVisible(isVisible);

		if (!isVisible && sActive == this)
			sActive = null;

		if (isVisible) {
			// There can be only one!!!
			if (sActive != null)
				sActive.setVisible(false);

			sActive = this;
		}

	}

}
