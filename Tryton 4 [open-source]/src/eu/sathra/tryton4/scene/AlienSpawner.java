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
package eu.sathra.tryton4.scene;

import javax.microedition.khronos.opengles.GL10;

import eu.sathra.scene.SceneNode;
import eu.sathra.util.Log;

public class AlienSpawner extends SceneNode {

	private static final float COEFFICIENT = 100000;

	@Override
	public void onDraw(GL10 gl, long time, long delta) {
		float spawnChance = (float) (2*Math.atan(time/COEFFICIENT)/Math.PI);
		Log.error("SPAWN: "+spawnChance);
	}
}
