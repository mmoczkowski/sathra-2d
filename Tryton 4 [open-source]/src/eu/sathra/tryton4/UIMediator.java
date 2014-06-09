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
