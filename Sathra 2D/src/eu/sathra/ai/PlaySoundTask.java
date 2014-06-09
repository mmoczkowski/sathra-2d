package eu.sathra.ai;

import java.io.IOException;

import eu.sathra.ai.context.AIContext;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.resources.Sound;
import eu.sathra.resources.SoundManager;

public class PlaySoundTask extends Task {

	private Sound mSound;

	@Deserialize("sound")
	public PlaySoundTask(String soundPath) throws IOException {
		mSound = SoundManager.getInstance().load(soundPath);
	}

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {
		SoundManager.getInstance().play(mSound.getHandle(), 1, 1, 0, 0, 1);
		return TaskResult.TRUE;
	}

}
