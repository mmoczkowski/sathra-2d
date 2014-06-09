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
