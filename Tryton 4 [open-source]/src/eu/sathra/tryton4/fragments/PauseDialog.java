package eu.sathra.tryton4.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import eu.sathra.tryton4.R;
import eu.sathra.tryton4.activities.GameActivity;

public class PauseDialog extends DialogFragment implements OnClickListener {

	private GameActivity mGameActivity;

	public PauseDialog() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mGameActivity = (GameActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setCancelable(false);
		// setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
		setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		View myView = inflater.inflate(R.layout.fragment_pause, container);
		View resumeView = myView.findViewById(R.id.pause_resume);

		resumeView.setOnClickListener(this);

		return myView;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onDismiss(DialogInterface di) {
		mGameActivity.unPauseGame();
	}

	@Override
	public void onCancel(DialogInterface di) {
		// mGameActivity.unPauseGame();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.pause_resume:
				dismiss();
			break;
		}
	}

}
