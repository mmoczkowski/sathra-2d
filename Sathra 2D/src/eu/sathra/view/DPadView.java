package eu.sathra.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import eu.sathra.R;

public class DPadView extends ImageView {

	private static final int DEFAULT_RADIUS = 80;

	private int mRadius;
	private boolean mResetOnTouchUp;
	private boolean mIsContinous = true;
	private float mDirectionX;
	private float mDirectionY;
	private float mAngle;
	private float mDeltaAngle;
	private float mTouchDownX;
	private float mTouchDownY;
	private float mCenterX;
	private float mCenterY;
	private float mPreviousAngle;
	private boolean mInit;

	public DPadView(Context context) {
		super(context);

	}

	public DPadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setAttributes(context, attrs);
	}

	public DPadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setAttributes(context, attrs);
	}
	
	private void setAttributes(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.DPadView);

		mRadius = a.getInt(R.styleable.DPadView_radius, DEFAULT_RADIUS);
		mResetOnTouchUp = a.getBoolean(R.styleable.DPadView_resetOnUp, true);

		a.recycle();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!this.isEnabled()) return true;
		
		switch (event.getAction()) {
		
			case MotionEvent.ACTION_DOWN:
				mTouchDownX = getX();
				mTouchDownY = getY();
				
				Rect bounds = new Rect();
				getGlobalVisibleRect(bounds);
				mCenterX = bounds.centerX();
				mCenterY = bounds.centerY();
				mPreviousAngle = 0;
				mInit = true;
			break;
	
			case MotionEvent.ACTION_MOVE:
				float x = event.getRawX();
				float y = event.getRawY();
				float distance = (float) Math.sqrt((x - mCenterX) * (x - mCenterX)
						+ (y - mCenterY) * (y - mCenterY));
				
				//if(distance < 50) break;

				float angleInRadians = (float) Math.atan2(event.getRawX()
						- mCenterX, event.getRawY() - mCenterY);
	
				float angleInDegrees = ((float) Math.toDegrees(angleInRadians));
				
				if(mInit) {
					mPreviousAngle = angleInDegrees;
					mInit = false;
				}
				
				mDeltaAngle = angleInDegrees - mPreviousAngle;
				
				if(mIsContinous)
					mAngle += mDeltaAngle;
				else 
					mAngle = angleInDegrees;
				
				mPreviousAngle = angleInDegrees;

				mDirectionX = (float) Math.sin(angleInRadians);
				mDirectionY = (float) Math.cos(angleInRadians);
				
				if (distance > mRadius) {
					distance = mRadius;
					
					x = mRadius * mDirectionX;
					y = mRadius * mDirectionY;
	
					setX(mTouchDownX + x);
					setY(mTouchDownY + y);
				} else {
					setX(event.getRawX() - getWidth()/2 );
					setY(event.getRawY() - getHeight()/2 );
				}
				
//				mDirectionX = x/mRadius;
//				mDirectionY = -y/mRadius;
				
			break;
	
			default:
				setX(mTouchDownX);
				setY(mTouchDownY);
				
				if(mResetOnTouchUp) {
					mDirectionX = 0;
					mDirectionY = 0;
					mAngle = 0;
				}
				
			break;
		}
		return true;
	}

	public float getAngle() {
		return mAngle;
	}

	/**
	 * Returns horizontal value for this directional pad within a range of [-1,
	 * 1] where -1 is max to the left, 0 is center and 1 is max to the right.
	 */
	public float getDirectionX() {
		return mDirectionX;
	}

	/**
	 * Returns vertical value for this directional pad within a range of [-1,
	 * 1] where -1 is max up, 0 is center and 1 is max down.
	 */
	public float getDirectionY() {
		return mDirectionY;
	}
}
