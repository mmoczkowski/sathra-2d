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
