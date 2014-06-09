package eu.sathra.physics.shapes;

import eu.sathra.io.annotations.Deserialize;

public class Collider {

	private float[][] mVertices;
	private boolean mIsSensor;
	
	@Deserialize("vertices")
	public Collider(float[][] vertices, boolean isSensor) {
		mVertices = vertices;
		mIsSensor = isSensor;
	}
	
	public void setVertices(float[][] vertices) {
		mVertices = vertices;
	}
	
	public float[][] getVertices() {
		return mVertices;
	}
	
	public boolean isSensor() {
		return mIsSensor;
	}
}
