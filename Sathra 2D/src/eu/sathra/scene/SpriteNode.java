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
