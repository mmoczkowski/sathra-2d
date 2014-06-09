package eu.sathra.tryton4.scene;

import javax.microedition.khronos.opengles.GL10;

import eu.sathra.scene.SceneNode;
import eu.sathra.util.Log;

public class AlienSpawner extends SceneNode {

	private static final float COEFFICIENT = 100000;

	@Override
	public void onDraw(GL10 gl, long time, long delta) {
		float spawnChance = (float) (2*Math.atan(time/COEFFICIENT)/Math.PI);
		Log.error("SPAWN: "+spawnChance);
	}
}
