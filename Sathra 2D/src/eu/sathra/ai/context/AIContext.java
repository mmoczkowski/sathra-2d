package eu.sathra.ai.context;

import eu.sathra.scene.SceneNode;

/**
 * Class for exchanging and persisting state information in AI.
 * @author Milosz Moczkowski
 *
 */
public interface AIContext {

	public SceneNode getOwner();
	
	public void setOwner(SceneNode owner);
	
	public void setVariable(String name, Object var);
	
	public Object getVariable(String name);
	
	public <T> T getVariable(String name, Class<T> type);
	
	public Object eval(String expression) throws ExpressionException;
}
