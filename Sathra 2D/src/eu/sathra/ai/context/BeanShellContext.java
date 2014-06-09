package eu.sathra.ai.context;

import bsh.EvalError;
import bsh.Interpreter;
import eu.sathra.scene.SceneNode;

public class BeanShellContext implements AIContext {

	private SceneNode mOwner;
	private Interpreter mInterpreter;
	
	public BeanShellContext() {
		mInterpreter = new Interpreter();
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
		try {
			mInterpreter.set(name, var);
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Object getVariable(String name) {
		try {
			return mInterpreter.get(name);
		} catch (EvalError e) {
			return null;
		}
	}

	@Override
	public <T> T getVariable(String name, Class<T> type) {
		try {
			return (T) mInterpreter.get(name);
		} catch (EvalError e) {
			return null;
		}
	}

	@Override
	public Object eval(String expression) throws ExpressionException {
		try {
			return mInterpreter.eval(expression);
		} catch (EvalError e) {
			throw new ExpressionException();
		}
	}

}
