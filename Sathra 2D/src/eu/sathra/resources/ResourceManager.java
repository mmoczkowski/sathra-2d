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
package eu.sathra.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.SparseArray;
import eu.sathra.io.adapters.TypeAdapter;

public class ResourceManager implements TypeAdapter<Texture> {

	private static final String KEY_FILENAME = "filename";
	// private static final String KEY_FILENAME = "filename";

	private static ResourceManager sInstance;

	private Map<Class<?>, ResourceLoader<?>> mLoaders;
	private SparseArray<ResourceHandle> mResources;

	public static ResourceManager getInstance() {
		if (sInstance == null)
			sInstance = new ResourceManager();

		return sInstance;
	}

	private ResourceManager() {
		mLoaders = new HashMap<Class<?>, ResourceLoader<?>>();
		mResources = new SparseArray<ResourceHandle>();

		mLoaders.put(Texture.class, new TextureLoader());
		mLoaders.put(Sound.class, SoundManager.getInstance());
	}

	@SuppressWarnings("unchecked")
	public <T extends ResourceHandle> T getHandler(String path, Class<T> type) throws IOException {
		int hash = path.hashCode();
		ResourceHandle handle = mResources.get(hash);

		if (handle != null) {
			handle.grab();
			return (T) handle;
		} else {
			ResourceLoader<T> loader = (ResourceLoader<T>) mLoaders.get(type);

			if (loader == null)
				throw new UnsupportedOperationException(
						"Resource type not supported " + type.getSimpleName());
			
			handle = loader.load(path);
			mResources.put(hash, handle);
			
			return (T) handle;
		}
	}

//	public void dropHandler(int handler) {
//		ResourceDescriptor descriptor = mResources.get(handler);
//
//		if (--descriptor.referenceCount <= 0) {
//			mLoaders.get(key).(descriptor.handler);
//		}
//	}

	@Override
	public Texture load(String param, JSONObject parent) throws JSONException, IOException {
		JSONObject jObj = parent.getJSONObject(param);
		jObj.getString(KEY_FILENAME);

		return getHandler(jObj.getString(KEY_FILENAME), Texture.class);
	}
	
	public void unloadAll() {
		mResources.clear();
	}

}
