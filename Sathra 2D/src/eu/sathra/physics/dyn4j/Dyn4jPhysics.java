package eu.sathra.physics.dyn4j;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.dynamics.RaycastResult;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Vector2;

import android.graphics.Rect;
import eu.sathra.physics.Body;
import eu.sathra.physics.Physics;

public class Dyn4jPhysics implements Physics {

	public static final float PHYSICS_TO_PIXEL = 10;
	private static Dyn4jPhysics sInstance;

	private World mWorld;

	public static Dyn4jPhysics getInstance() {
		if (sInstance == null)
			sInstance = new Dyn4jPhysics();

		return sInstance;
	}

	private Dyn4jPhysics() {
		mWorld = new World();
		mWorld.setGravity(new Vector2(0, 0));
	}

	@Override
	public void setGravity(int x, int y) {
		mWorld.setGravity(new Vector2(x, y));
	}

	public World getWorld() {
		return mWorld;
	}

	public List<eu.sathra.physics.Body> rayCast(float fromX, float fromY,
			float toX, float toY) {
		return rayCast(fromX, fromY, toX, toY, Physics.MASK_ALL);
	}

	public List<eu.sathra.physics.Body> rayCast(float fromX, float fromY,
			float toX, float toY, int mask) {
		List<RaycastResult> results = new ArrayList<RaycastResult>();
		List<eu.sathra.physics.Body> hits = new ArrayList<eu.sathra.physics.Body>();

		try {
			mWorld.raycast(new Vector2(fromX / PHYSICS_TO_PIXEL, fromY
					/ PHYSICS_TO_PIXEL), new Vector2(toX / PHYSICS_TO_PIXEL,
					toY / PHYSICS_TO_PIXEL), true, true, results);
		} catch (IllegalArgumentException e) {
			return hits;
		}

		for (RaycastResult result : results) {
			Body myBody = (Body) result.getBody().getUserData();
			if ((myBody.getCollisionMask() & mask) != 0) {
				hits.add((Body) result.getBody().getUserData());
			}
		}

		return hits;
	}

	public List<eu.sathra.physics.Body> sphereCast(float x, float y,
			float radius) {

		Circle myCircle = new Circle(radius / PHYSICS_TO_PIXEL);
		myCircle.translate(x / PHYSICS_TO_PIXEL, y / PHYSICS_TO_PIXEL);

		List<org.dyn4j.dynamics.Body> bodies = mWorld.detect(myCircle);
		List<eu.sathra.physics.Body> hits = new ArrayList<eu.sathra.physics.Body>();

		for (org.dyn4j.dynamics.Body result : bodies) {
			hits.add((Body) result.getUserData());
		}

		return hits;
	}

	@Override
	public boolean overlaps(Rect bound) {

		int minX = Math.min(bound.left, bound.right);
		int maxX = Math.max(bound.left, bound.right);
		int minY = Math.min(bound.top, bound.bottom);
		int maxY = Math.max(bound.top, bound.bottom);

		return !mWorld.detect(new AABB(minX, minY, maxX, maxY)).isEmpty();
	}

}
