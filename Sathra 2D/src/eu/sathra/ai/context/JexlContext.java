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

import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.Script;

import eu.sathra.scene.SceneNode;

public class JexlContext implements AIContext {
	
	private static final JexlEngine sJexl = new JexlEngine();
	private MapContext mContext = new MapContext();
	private SceneNode mOwner;
	
	
	static {
		sJexl.setCache(512);
		sJexl.setLenient(false);
		sJexl.setSilent(false);
	}
	
	public JexlContext() {
		// noop
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
		mContext.set(name, var);
	}

	@Override
	public Object getVariable(String name) {
		return mContext.get(name);
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
		Script e = sJexl.createScript(expression);
		return e.execute(mContext);
	}

}

