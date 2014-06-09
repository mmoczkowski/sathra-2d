package eu.sathra.physics;

import android.graphics.Rect;

public interface Physics {

	public static final int MASK_ALL = Integer.MAX_VALUE;
	public static final int MASK_NONE = 0;
	
	void setGravity(int x, int y);

	/**
	 * Checks if any of the bodies overlap specified bounding rectangle.
	 * @param bound
	 * @return 
	 */
	boolean overlaps(Rect bound); // TODO colision mask

}
