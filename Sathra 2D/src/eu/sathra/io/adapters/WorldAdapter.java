package eu.sathra.io.adapters;

import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;
import org.json.JSONException;
import org.json.JSONObject;

public class WorldAdapter implements TypeAdapter<World> {
	
	private static final String PARAM_GRAVITY_X = "gravity_x";  
	private static final String PARAM_GRAVITY_Y = "gravity_y";  
	
	@Override
	public World load(String param, JSONObject parent) throws JSONException {
		
		JSONObject jObj = parent.getJSONObject(param);
		
		float gravity_x = (float) jObj.optDouble(PARAM_GRAVITY_X, 0);
		float gravity_y = (float) jObj.optDouble(PARAM_GRAVITY_Y, 0);
		
		World myWorld = new World();
		myWorld.setGravity(new Vector2(gravity_x, gravity_y));
		
		return myWorld;
	}

}
