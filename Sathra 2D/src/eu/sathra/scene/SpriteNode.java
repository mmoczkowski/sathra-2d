package eu.sathra.scene;

import javax.microedition.khronos.opengles.GL10;

import eu.sathra.ai.Task;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.physics.Body;
import eu.sathra.scene.animation.Animation;
import eu.sathra.video.opengl.Sprite;

public class SpriteNode extends SceneNode {

	private Sprite mSprite;

	public SpriteNode(Sprite sprite) {
		this(null, sprite, new Transform(), true, null, null, null, null);
	}

	@Deserialize({ "id", "sprite", "transform", "is_visible", "animation",
			"children", "body", "ai" })
	@Defaults({ Deserialize.NULL, Deserialize.NULL, Deserialize.NULL, "true",
			Deserialize.NULL, Deserialize.NULL, Deserialize.NULL,
			Deserialize.NULL })
	public SpriteNode(String id, Sprite sprite, Transform t, boolean isVisible,
			Animation animation, SceneNode[] children, Body body, Task ai) {
		super(id, t, isVisible, animation, children, body, ai);

		mSprite = sprite;
	}

	@Override
	protected void draw(GL10 gl, long time, long delta) {
		mSprite.setRotation(this.getTransform().getRotation());
		mSprite.draw(gl);
	}

}
