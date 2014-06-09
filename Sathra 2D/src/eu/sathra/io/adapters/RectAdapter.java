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
