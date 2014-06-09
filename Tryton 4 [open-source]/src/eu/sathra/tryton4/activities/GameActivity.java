package eu.sathra.tryton4.activities;

import java.util.LinkedList;
import java.util.Queue;

import android.media.MediaPlayer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import eu.sathra.Parameters;
import eu.sathra.SathraActivity;
import eu.sathra.ai.context.AIContext;
import eu.sathra.scene.SceneNode;
import eu.sathra.tryton4.R;
import eu.sathra.tryton4.UIMediator;
import eu.sathra.tryton4.Weapon;
import eu.sathra.tryton4.ai.ContextKeys;
import eu.sathra.tryton4.fragments.PauseDialog;
import eu.sathra.util.Log;
import eu.sathra.view.DPadView;

public class GameActivity extends SathraActivity implements OnClickListener {

	/**
	 * This class constructs new alien and adds it to the scene.
	 * 
	 * @author mmoczkowski
	 * 
	 */
	private class SpawnAlienRunnable implements Runnable {

		@Override
		public void run() {
			try {
				// Create random position
				double angle = (Math.PI * 2 * Math.random());
				float x = (float) (SPAWN_AREA_RADIUS * Math.cos(angle));
				float y = (float) (SPAWN_AREA_RADIUS * Math.sin(angle));

				SceneNode alienNode = loadScene(ASSET_ALIEN);

				AIContext context = alienNode.getAIContext();
				context.setVariable(ContextKeys.KEY_UI_MEDIATOR, mMediator);
				context.setVariable(ContextKeys.KEY_HEALTH, 100);
				context.setVariable(ContextKeys.KEY_DAMAGE, 0);
				context.setVariable(ContextKeys.KEY_PLAYER, mPlayerNode);
				alienNode.setAIContext(context);
				alienNode.setPosition(x, y);
				mAliensContainerNode.addChild(alienNode);
				mAliens.add(alienNode);

				if (mAliens.size() >= ALIENS_CAP) {
					SceneNode nodeToRemove = mAliens.remove();
					mAliensContainerNode.removeChild(nodeToRemove);
					nodeToRemove.getBody().setIsEnabled(false);
					nodeToRemove = null;
				}
			} catch (Exception e) {
				Log.error("Exception while spawning alien. This should not happen!");
			}

		}

	}

	private static final float COEFFICIENT = 100000;
	private static final float SPAWN_PERIOD = 1000;
	private static final float SPAWN_AREA_RADIUS = 800;
	private static final int ALIENS_CAP = 100;
	private static final String ASSET_PLAYER = "player.json";
	private static final String ASSET_ALIEN = "alien.json";
	private static final String ASSET_MAP = "map.json";

	private SpawnAlienRunnable mSpawnAlienRunnable = new SpawnAlienRunnable();
	private SceneNode mPlayerNode;
	private SceneNode mAliensContainerNode;
	private UIMediator mMediator;
	private MediaPlayer mMediaPlayer;
	private long mLastSpawnTime;
	private long mRoundStartedTime;
	private DPadView mDPadView;
	private Queue<SceneNode> mAliens = new LinkedList<SceneNode>();

	@Override
	protected Parameters getParameters() {
		Parameters myParameters = new Parameters();
		myParameters.fullscreen = true;
		myParameters.ambientColor = 0x44000000;
		myParameters.layout = R.layout.activity_game;
		myParameters.showFPS = false;
		myParameters.drawDebug = true;
		myParameters.width = 0;
		myParameters.height = 800;

		return myParameters;
	}

	@Override
	protected void onEngineInitiated() {

		try {

			SceneNode mapNode = loadScene(ASSET_MAP);
			addNode(mapNode);
			mAliensContainerNode = mapNode.findChildById("aliens");

			// GUI
			View pauseView = findViewById(R.id.hud_pause);
			pauseView.setOnClickListener(this);

			mDPadView = (DPadView) findViewById(R.id.hud_dpad);
			View shootButton = findViewById(R.id.hud_a);
			View reloadButton = findViewById(R.id.hud_b);
			TextView healthText = (TextView) findViewById(R.id.hud_health);
			TextView ammoText = (TextView) findViewById(R.id.hud_ammo);
			TextView scoreText = (TextView) findViewById(R.id.hud_score);

			mMediator = new UIMediator(this, shootButton, reloadButton, mDPadView,
					healthText, ammoText, scoreText);

			// Load player
			mPlayerNode = loadScene(ASSET_PLAYER);
			setupPlayer();
			mapNode.findChildById("player").addChild(mPlayerNode);

			// Reset time
			mRoundStartedTime = 0;
		} catch (Exception e) {
			Log.error("Exception in onEngineInitiated()");
			e.printStackTrace();
		}
	}

	@Override
	protected void onUpdate(long time, long timeDelta) {

		long timeSinceRoundStarted = time - mRoundStartedTime;

		/**
		 * Each SPAWN_PERIOD we try to spawn an alien according to the
		 * probability function: f(t) = (2*atan(t+5000)/COEFFICIENT)/PI, where t
		 * = time since round started.
		 */
		if (timeSinceRoundStarted - mLastSpawnTime > SPAWN_PERIOD) {

			float spawnChance = (float) (2 * Math
					.atan((timeSinceRoundStarted + 5000) / COEFFICIENT) / Math.PI);

			try {
				if (Math.random() <= spawnChance) {
					new Thread(mSpawnAlienRunnable).start();
				}

			} catch (Exception e) {
				// TODO
			}

			mLastSpawnTime = timeSinceRoundStarted;
		}
	}

	private void setupPlayer() {
		AIContext context = mPlayerNode.getAIContext();
		context.setVariable(ContextKeys.KEY_UI_MEDIATOR, mMediator);
		context.setVariable(ContextKeys.KEY_CURRENT_WEAPON, Weapon.Type.FLAMER);
		context.setVariable(ContextKeys.KEY_RIFLE_AMMO, 30);
		context.setVariable(ContextKeys.KEY_RIFLE_CAPACITY, 30);
		context.setVariable(ContextKeys.KEY_DAMAGE, 0);
		context.setVariable(ContextKeys.KEY_HEALTH, 100);
		mPlayerNode.setAIContext(context);
		mPlayerNode.setAIEnabled(true);
	}

	public void restart() {
		setupPlayer();

		while (!mAliens.isEmpty()) {
			mAliensContainerNode.removeChild(mAliens.remove());
		}

		mAliens.clear();

		// Reset time
		mLastSpawnTime = 0;
		mRoundStartedTime = getVirtualTime();
		mMediator.resetScore();
		setTimeScale(1);
	}

	@Override
	public void onStart() {
		mMediaPlayer = MediaPlayer.create(this, R.raw.track_game);
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		mMediaPlayer.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMediaPlayer.pause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMediaPlayer.release();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hud_pause:
			pauseGame();
			break;
		}
	}

	public void pauseGame() {
		mDPadView.setEnabled(false);
		PauseDialog pauseDialog = new PauseDialog();
		mMediaPlayer.pause();
		pauseDialog.show(getFragmentManager(), null);
		setTimeScale(0);
	}

	public void unPauseGame() {
		try {
			mMediaPlayer.start();
		} catch (IllegalStateException e) {

		}

		mDPadView.setEnabled(true);
		setTimeScale(1);
	}

	public void onBackPressed() {
		pauseGame();
	}
}
