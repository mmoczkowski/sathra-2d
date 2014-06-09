package eu.sathra.physics.shapes;

import android.graphics.Rect;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;

public class Rectangle extends Collider {

	@Deserialize( { "bounds", "is_sensor" } )
	@Defaults( { Deserialize.NULL, "false" } )
	public Rectangle(Rect bounds, boolean isSensor) {
		super(null, isSensor);
		
		float[] vertices = {
				bounds.left, bounds.top, 
				bounds.left+bounds.width(), bounds.top, 
				bounds.left+bounds.width(), bounds.top+bounds.height(),
				bounds.left, bounds.top+bounds.height()};
		
		setVertices(new float[][] {vertices});
	}
}
