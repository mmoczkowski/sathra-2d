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

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.Scriptable;
import eu.sathra.scene.SceneNode;

public class RhinoContext implements AIContext {

	private Context mContext;
	private Scriptable mScope;
	private SceneNode mOwner;

	public RhinoContext(SceneNode owner) {
		mOwner = owner;
		mContext = Context.enter();
		//Global global = new Global(mContext);
		/*
		 * Unfortunately evaluateString will throw exception without this
		 */
		mContext.setOptimizationLevel(9);
		
		mScope = mContext.initStandardObjects();//new Global(mContext);//mContext.initStandardObjects();
		//Context.exit();
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
		mScope.put(name, mScope, Context.javaToJS(var, mScope));
	}

	@Override
	public Object getVariable(String name) {
		Object jsObject = mScope.get(name, mScope);

		return Scriptable.NOT_FOUND.equals(jsObject) ? null : Context.jsToJava(
				jsObject, Object.class);
	}
	
	public <T> T getVariable(String name, Class<T> type) {
		try {
			T myObject = (T) getVariable(name);
			return myObject;
		} catch(ClassCastException e) {
			return null;
		}
	}

	@Override
	public Object eval(String expression) throws ExpressionException {
		try {
			mContext = Context.enter();
			mContext.setOptimizationLevel(-1);
			return mContext.evaluateString(mScope, expression, "<1>", 1, null);
		} catch (EcmaError e) {
			throw new ExpressionException(e.getMessage());
		} finally {
			Context.exit();
		}
	}

}
