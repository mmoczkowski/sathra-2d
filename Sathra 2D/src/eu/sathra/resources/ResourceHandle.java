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

/**
 * A resource handle is a POJO containing a unique integer resource id
 * recognizable by the desired resource subsystem. It does not contain resource
 * data (e.g. bitmap or sound) as it is only supposed to "point" to the data. 
 * 
 * @tag Do not create ResourceHandles yourself. Use ResourceManager.
 * 
 * @author Milosz Moczkowski
 * 
 */
public class ResourceHandle {

	private int mHandle;
	private int mReferences;

	public ResourceHandle(int handle) {
		mHandle = handle;
		mReferences = 1;
	}

	public int getHandle() {
		return mHandle;
	}
	
	public void grab() {
		++mReferences;
	}
	
	public void drop() {
		--mReferences;
	}
}
