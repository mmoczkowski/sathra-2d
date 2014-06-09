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

