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
