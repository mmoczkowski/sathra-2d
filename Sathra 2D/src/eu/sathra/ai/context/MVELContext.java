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
package eu.sathra.ai.context;

import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

import eu.sathra.scene.SceneNode;

public class MVELContext implements AIContext {

	private SceneNode mOwner;
	private Map<String, Object> mVariables; 
	
	public MVELContext() {
		mVariables = new HashMap<String, Object>();
	}
	
	@Override
	public SceneNode getOwner() {
		return mOwner;
	}

	@Override
	public void setOwner(SceneNode owner) {
		mOwner = owner;
	}

	@Override
	public void setVariable(String name, Object var) {
		mVariables.put(name, var);
	}

	@Override
	public Object getVariable(String name) {
		return mVariables.get(name);
	}

	public <T> T getVariable(String name, Class<T> type) {
		try {
			return (T) getVariable(name);
		} catch (ClassCastException e) {
			return null;
		}
	}

	@Override
	public Object eval(String expression) throws ExpressionException {
		return MVEL.eval(expression, mVariables);
	}

}

