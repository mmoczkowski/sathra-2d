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
