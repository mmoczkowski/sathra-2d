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
package eu.sathra.tryton4.ai;

import eu.sathra.ai.Task;
import eu.sathra.ai.context.AIContext;
import eu.sathra.tryton4.UIMediator;

public abstract class PlayerTask extends Task {

	private UIMediator mMediator;

	@Override
	public void onAttach(AIContext context) {
		mMediator = context.getVariable(ContextKeys.KEY_UI_MEDIATOR,
				UIMediator.class);
	}
	
	protected UIMediator getUIMediator() {
		return mMediator;
	}
}
