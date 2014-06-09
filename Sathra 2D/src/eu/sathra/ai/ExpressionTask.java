package eu.sathra.ai;

import eu.sathra.ai.context.AIContext;
import eu.sathra.ai.context.ExpressionException;
import eu.sathra.io.annotations.Deserialize;

public class ExpressionTask extends Task {

	private String mExpression;
	
	@Deserialize("expression")
	public ExpressionTask(String expression) {
		mExpression = expression;
	}
	
	@Override
	public TaskResult execute(AIContext context, long time, long delta) {
		
		try {
			context.eval(mExpression);
			return TaskResult.TRUE;
		} catch (ExpressionException e) {
			return TaskResult.FALSE;
		}

		
	}

}
