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
package eu.sathra.physics.dyn4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dyn4j.collision.Fixture;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.Step;
import org.dyn4j.dynamics.StepListener;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Vector2;

import android.graphics.Rect;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.physics.Physics;
import eu.sathra.physics.shapes.Collider;
import eu.sathra.scene.SceneNode;
import eu.sathra.scene.Transform;

public class Dyn4jBody extends CollisionAdapter implements
		eu.sathra.physics.Body, StepListener {

	private Body mBody;
	private int mMask = Physics.MASK_ALL;
	private Transform mTransform;
	private Set<Body> mCollidedBodies = new HashSet<Body>();

	public Dyn4jBody() {
		mBody = new Body();
		mBody.setUserData(this);
		mTransform = new Transform();
		Dyn4jPhysics.getInstance().getWorld().addBody(mBody);
		setMass(0);
	}
	
	public Dyn4jBody(Body body) {
		mBody = body;
		mBody.setUserData(this);
		mTransform = new Transform();
		Dyn4jPhysics.getInstance().getWorld().addBody(mBody);
	}

	@Deserialize({ "mass", "mask", "shape" })
	@Defaults({ "0", "255", Deserialize.NULL })
	public Dyn4jBody(float mass, int mask, Collider collider) {
		this();

		if (collider != null)
			setCollider(collider);

		setMass(mass);
		setCollisionMask(mask);

		World myWorld = Dyn4jPhysics.getInstance().getWorld();
		myWorld.addListener(this);
	}

	@Override
	public float getX() {
		return (float) mBody.getTransform().getTranslationX()
				* Dyn4jPhysics.PHYSICS_TO_PIXEL;
	}

	@Override
	public float getY() {
		return (float) mBody.getTransform().getTranslationY()
				* Dyn4jPhysics.PHYSICS_TO_PIXEL;
	}

	@Override
	public void setPosition(float x, float y) {
		mBody.getTransform().setTranslation(x / Dyn4jPhysics.PHYSICS_TO_PIXEL,
				y / Dyn4jPhysics.PHYSICS_TO_PIXEL);
	}

	public void setCollider() {
		// mBody.getMass().
	}

	@Override
	public void setMass(float mass) {
		Mass myMass = mBody.getMass();
		mBody.setMass(new Mass(myMass.getCenter(), mass, myMass.getInertia()));
	}

	@Override
	public void setCollider(Collider shape) {
		mBody.removeAllFixtures();

		for (float convex[] : shape.getVertices()) {
			List<Vector2> myVertices = new ArrayList<Vector2>();

			for (int c = 0; c < convex.length; c += 2) {
				myVertices.add(new Vector2(convex[c]
						/ Dyn4jPhysics.PHYSICS_TO_PIXEL, convex[c + 1]
						/ Dyn4jPhysics.PHYSICS_TO_PIXEL));
			}

			Polygon myPolygon = Geometry.createPolygon(myVertices
					.toArray(new Vector2[myVertices.size()]));

			Fixture myFixture = mBody.addFixture(myPolygon);
			myFixture.setSensor(shape.isSensor());

		}
	}

	@Override
	public void applyForce(float x, float y) {
		mBody.applyForce(new Vector2(x, y));
	}

	@Override
	public void setVelocity(float x, float y) {
		mBody.setLinearVelocity(x, y);
	}

	@Override
	public void setYVelocity(float y) {
		mBody.setLinearVelocity(mBody.getLinearVelocity().x, y);
	}

	@Override
	public void setXVelocity(float x) {
		mBody.setLinearVelocity(x, mBody.getLinearVelocity().y);
	}

	@Override
	public float getXVelocity() {
		return (float) mBody.getLinearVelocity().x;
	}

	@Override
	public float getYVelocity() {
		return (float) mBody.getLinearVelocity().y;
	}

	@Override
	public float getXForce() {
		return (float) mBody.getForce().x;
	}

	@Override
	public float getYForce() {
		return (float) mBody.getForce().y;
	}

	@Override
	public void setImpulse(float x, float y) {
		mBody.applyImpulse(new Vector2(x, y));
	}

	@Override
	public boolean collision(Body body1, BodyFixture fixture1, Body body2,
			BodyFixture fixture2, Penetration penetration) {
		if (body1 == mBody || body2 == mBody) {
			Body other = (mBody == body1 ? body2 : body1);
			mCollidedBodies.add(other);
		}

		return true;
	}

	@Override
	public boolean isColliding() {
		return !mCollidedBodies.isEmpty();
	}

	@Override
	public List<eu.sathra.physics.Body> getCollidedBodies() {
		List<eu.sathra.physics.Body> collided = new ArrayList<eu.sathra.physics.Body>();

		for (Body other : mCollidedBodies)
			collided.add((eu.sathra.physics.Body) other.getUserData());

		return collided;
	}

	@Override
	public void begin(Step step, World world) {
		List<Body> bodiesToRemove = new ArrayList<Body>();

		// update list of collisions
		for (Body other : mCollidedBodies)
			if (!other.isInContact(mBody))
				bodiesToRemove.add(other);

		mCollidedBodies.removeAll(bodiesToRemove);
	}

	@Override
	public void end(Step step, World world) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePerformed(Step step, World world) {

	}

	@Override
	public void setTransform(Transform transform) {
		mBody.getTransform().setTranslationX(
				transform.getX() / Dyn4jPhysics.PHYSICS_TO_PIXEL);
		mBody.getTransform().setTranslationY(
				transform.getY() / Dyn4jPhysics.PHYSICS_TO_PIXEL);
		mBody.rotateAboutCenter(Math.toRadians(transform.getRotation())
				- mBody.getTransform().getRotation());
	}

	@Override
	public Transform getTransform() {
		org.dyn4j.geometry.Transform dyn4jTransform = mBody.getTransform();
		
		mTransform.set(
				(float) dyn4jTransform.getTranslationX() * Dyn4jPhysics.PHYSICS_TO_PIXEL, 
				(float) dyn4jTransform.getTranslationY() * Dyn4jPhysics.PHYSICS_TO_PIXEL, 
				(float) Math.toDegrees(dyn4jTransform.getRotation()), 1, 1);
		
		return mTransform;
	}

	@Override
	public String toString() {
		return mBody.getId().toString();
	}

	@Override
	public Rect getBounds() {
		AABB aabb = mBody.createAABB();
		Rect bounds = new Rect(
				(int) (aabb.getMinX() * Dyn4jPhysics.PHYSICS_TO_PIXEL),
				(int) (aabb.getMinY() * Dyn4jPhysics.PHYSICS_TO_PIXEL),
				(int) (aabb.getMaxX() * Dyn4jPhysics.PHYSICS_TO_PIXEL),
				(int) (aabb.getMaxY() * Dyn4jPhysics.PHYSICS_TO_PIXEL));

		return bounds;
	}

	private SceneNode mNode;

	@Override
	public void setSceneNode(SceneNode node) {
		mNode = node;
	}

	@Override
	public SceneNode getSceneNode() {
		return mNode;
	}

	@Override
	public void setCollisionMask(int mask) {
		mMask = mask;
	}

	@Override
	public int getCollisionMask() {
		return mMask;
	}
	
	@Override
	public eu.sathra.physics.Body clone() throws CloneNotSupportedException {
		Body clone = new Body();
		
		for(BodyFixture fixture : mBody.getFixtures()) {
			clone.addFixture(fixture);
		}
		
		clone.setMass(mBody.getMass());
		
		return new Dyn4jBody(clone);
	}

	@Override
	public void setIsEnabled(boolean enabled) {
		if(enabled) {
			Dyn4jPhysics.getInstance().getWorld().addBody(mBody);
		} else {
			Dyn4jPhysics.getInstance().getWorld().removeBody(mBody);
		}
		
		
	}

	@Override
	public boolean isEnabled() {
		return Dyn4jPhysics.getInstance().getWorld().containsBody(mBody);
	}
}
