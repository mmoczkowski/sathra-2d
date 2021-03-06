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
package eu.sathra.tryton4.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;
import eu.sathra.tryton4.R;

public class InfiniteBackgroundView extends ImageView {

	private Bitmap mBitmap;
	private Paint mPaint;
	private Rect mBounds;

	public InfiniteBackgroundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context, attrs);
	}

	public InfiniteBackgroundView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initialize(context, attrs);
		
	}

	private void initialize(Context context, AttributeSet attrs) {
		mPaint = new Paint();
		mBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.img_menu_bg);
		mBounds = new Rect();
		setWillNotDraw(false);
	}

	protected void onDraw(Canvas canvas) {
		long time = System.currentTimeMillis();
		int offsetX = (int) ((time/50)%mBitmap.getWidth());

		mBounds.set(offsetX, 0, mBitmap.getWidth()+offsetX, getHeight());
		canvas.drawBitmap(mBitmap, null, mBounds, mPaint);
		mBounds.set(offsetX-mBitmap.getWidth(), 0, offsetX, getHeight());
		canvas.drawBitmap(mBitmap, null, mBounds, mPaint);
		this.postInvalidate();
	}

}
