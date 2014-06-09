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

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Rect;

public class RectAdapter implements TypeAdapter<Rect> {

	private static final String PARAM_LEFT = "x";
	private static final String PARAM_TOP = "y";
	private static final String PARAM_WIDTH = "w";
	private static final String PARAM_HEIGHT = "h";
	
	@Override
	public Rect load(String param, JSONObject parent) throws JSONException {

		try {
			JSONObject jObj = parent.getJSONObject(param);
			
			int top = jObj.optInt(PARAM_TOP, 0);
			int left = jObj.optInt(PARAM_LEFT, 0);
			int width = jObj.optInt(PARAM_WIDTH, 0);
			int height = jObj.optInt(PARAM_HEIGHT, 0);

			return new Rect(left, top, left+width, top+height);
		} catch(JSONException e) {
			return null;
		}
	}

}
