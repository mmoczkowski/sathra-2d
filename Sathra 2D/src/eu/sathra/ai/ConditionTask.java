package eu.sathra.ai;

import eu.sathra.ai.context.AIContext;
import eu.sathra.ai.context.ExpressionException;
import eu.sathra.io.annotations.Deserialize;

public class ConditionTask extends Task {

	private String mExpression;
	
	@Deserialize("expression")
	public ConditionTask(String expression) {
		mExpression = expression;
	}
	
	@Override
	public TaskResult execute(AIContext context, long time, long delta) {
		
		try {
			Object result = context.eval(mExpression);
			return Boolean.TRUE.equals(result) ? TaskResult.TRUE : TaskResult.FALSE;
		} catch (ExpressionException e) {
			return TaskResult.FALSE;
		}

		
	}

}
