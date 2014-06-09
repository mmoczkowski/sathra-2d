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
package eu.sathra.physics;

import java.util.List;

import android.graphics.Rect;
import eu.sathra.physics.shapes.Collider;
import eu.sathra.scene.SceneNode;
import eu.sathra.scene.Transform;

public interface Body extends Cloneable {
	void setSceneNode(SceneNode node); // TODO: circular reference
	SceneNode getSceneNode(); // TODO: circular reference
	void setCollisionMask(int mask);
	int getCollisionMask();
	void setTransform(Transform t);
	Transform getTransform();
	void setPosition(float x, float y);
	float getX();
	float getY();
	void setCollider(Collider collider);
	void setMass(float mass);
	void applyForce(float x, float y);
	float getXForce();
	float getYForce();
	void setImpulse(float x, float y);
	void setVelocity(float x, float y);
	void setYVelocity(float y);
	void setXVelocity(float x);
	float getXVelocity();
	float getYVelocity();
	Rect getBounds();
	boolean isColliding();
	List<Body> getCollidedBodies();
	void setIsEnabled(boolean enabled);
	boolean isEnabled();
}
