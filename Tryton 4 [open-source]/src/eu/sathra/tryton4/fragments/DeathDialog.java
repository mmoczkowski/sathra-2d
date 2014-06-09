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
package eu.sathra.tryton4.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import eu.sathra.tryton4.R;
import eu.sathra.tryton4.activities.GameActivity;
import eu.sathra.util.Log;

public class DeathDialog extends DialogFragment implements OnClickListener {

	public static final String KEY_SCORE = "key_score";

	private GameActivity mGameActivity;

	public DeathDialog() {
		// noop
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mGameActivity = (GameActivity) activity;
		} catch (ClassCastException e) {
			Log.error("Activity should be instance of "
					+ GameActivity.class.getSimpleName());
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int score = getArguments().getInt(KEY_SCORE);
		setCancelable(false);
		// setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
		setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		View myView = inflater.inflate(R.layout.fragment_dead, container);
		TextView scoreView = (TextView) myView.findViewById(R.id.dead_score);
		View playButton = myView.findViewById(R.id.dead_again);

		myView.findViewById(R.id.menu_rate).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// GP link
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=eu.sathra.tryton4"));
				startActivity(intent);
			}
			
		});
		
		playButton.setOnClickListener(this);

		scoreView.setText("Score: " + score);

		return myView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.dead_again:
			dismiss();
			mGameActivity.restart();
			break;
		}
	}
}
