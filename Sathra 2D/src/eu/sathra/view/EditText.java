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
package eu.sathra.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import eu.sathra.R;
import eu.sathra.util.Util;

public class EditText extends android.widget.EditText {

	private boolean mIsColored;

	public EditText(Context context) {
		super(context);

		initialize(context, null);
	}

	public EditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		initialize(context, attrs);
	}

	public EditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initialize(context, attrs);
	}

	private void initialize(Context context, AttributeSet attrs) {

		if (attrs == null) {
			mIsColored = true;
		} else {
			Util.setFont(this, context, attrs);

			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.TextView);
			
			mIsColored = a.getBoolean(R.styleable.TextView_colored, true);

			a.recycle();
		}
	}

	protected void onTextChanged(CharSequence text, int start, int before,
			int after) {
		
		if(mIsColored) {
			mIsColored = false;
			setText(Util.parseColors(text));
			mIsColored = true;
		} else {
			super.onTextChanged(text, start, before, after);
		}
	}

}
