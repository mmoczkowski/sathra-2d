package eu.sathra.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.view.View;

public class DebugView extends View {

	private static DebugView sInstance;
	private static Paint mPaint;
	private static Canvas mCanvas;
	private static Bitmap mBitmap;

	public DebugView(Context context, int width, int height) {
		super(context);

		mPaint = new Paint();
		mPaint.setStyle(Style.STROKE);

		mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);

		sInstance = this;
	}

	@Override
	public void draw(Canvas canvas) {
		//canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		mBitmap.eraseColor(Color.TRANSPARENT);
		canvas.drawBitmap(mBitmap, 0, 0, mPaint);

	}

	public static void drawCircle(int color, float x, float y, float radius) {
		if (sInstance != null) {
			mPaint.setColor(color);
			mCanvas.drawCircle(translateX(x), translateY(y), 100, mPaint);
			
			
			//sInstance.postInvalidate();
		}
	}

	public static void drawLine(int color, float x1, float y1, float x2, float y2) {
		if (sInstance != null) {
			mPaint.setColor(color);
			mCanvas.drawLine(translateX(x1), translateY(y1), translateX(x2),
					translateY(y2), mPaint);
		}
			//sInstance.postInvalidate();
	}

	private static int translateX(float x) {
		return (int) (x + mBitmap.getWidth() / 2);
	}

	private static int translateY(float y) {
		return (int) (-y + mBitmap.getHeight() / 2);
	}
}
