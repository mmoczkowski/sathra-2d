package eu.sathra.io.adapters;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

// TODO
public class InterpolatorAdapter implements TypeAdapter<Interpolator> {

	private static final String INTERPOLATOR_LINEAR = "linear";

	private Map<String, Interpolator> mInterpolators = new HashMap<String, Interpolator>();

	public InterpolatorAdapter() {
		mInterpolators.put(INTERPOLATOR_LINEAR, new LinearInterpolator());
	}

	@Override
	public Interpolator load(String param, JSONObject parent)
			throws JSONException {
		
		String interpolator = parent.getString(param);

		return mInterpolators.get(interpolator);
	}
}
