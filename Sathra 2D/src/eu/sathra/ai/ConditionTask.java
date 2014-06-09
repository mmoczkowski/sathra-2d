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
