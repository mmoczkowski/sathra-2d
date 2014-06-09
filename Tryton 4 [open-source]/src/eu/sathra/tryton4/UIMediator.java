package eu.sathra.tryton4;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import eu.sathra.SathraActivity;
import eu.sathra.tryton4.fragments.DeathDialog;
import eu.sathra.view.DPadView;

public class UIMediator {

	private View mShootButton;
	private View mReloadButton;
	private DPadView mDPadView;
	private TextView mAmmoView;
	private TextView mHealthView;
	private TextView mScoreView;
	private int mScore;
	private int mHealth;
	private int mAmmo;

	public UIMediator(Context context, View shootButton, View reloadButton,
			DPadView dpadView, TextView healthText, TextView ammoText,
			TextView scoreText) {
		mShootButton = shootButton;
		mReloadButton = reloadButton;
		mDPadView = dpadView;
		mAmmoView = ammoText;
		mHealthView = healthText;
		mScoreView = scoreText;
		resetScore();
	}

	public void onResume() {

	}

	public boolean isShooting() {
		return mShootButton.isPressed();
	}

	public boolean isReloading() {
		return mReloadButton.isPressed();
	}

	public float getRotation() {
		return mDPadView.getAngle();
	}

	public void incrementScore() {
		++mScore;

		update();
	}

	public void resetScore() {
		mScore = 0;
		mHealth = 100;
		mAmmo = 30;
		
		update();
	}

	public void setAmmo(int ammo, int cap) {
		mAmmo = ammo;

		update();
	}

	public void setHealth(int health) {

		mHealth = Math.max(0, health);

		if (mHealth <= 0) {
			// dead
			Bundle arguments = new Bundle();
			arguments.putInt(DeathDialog.KEY_SCORE, mScore);
			final DeathDialog deathDialog = new DeathDialog();
			deathDialog.setArguments(arguments);

			final FragmentManager myFragmentManager = SathraActivity
					.getCurrentSathra().getFragmentManager();

			deathDialog.show(myFragmentManager, null);

		} else {
			update();
		}
	}

	private void update() {
		mScoreView.post(new Runnable() {

			@Override
			public void run() {

				mScoreView.setText(Integer.toString(mScore));
				mHealthView.setText(Integer.toString(mHealth));
				mAmmoView.setText(Integer.toString(mAmmo));

			}

		});
	}
}
