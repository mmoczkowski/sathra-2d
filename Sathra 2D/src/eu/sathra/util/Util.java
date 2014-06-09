package eu.sathra.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import eu.sathra.R;

public class Util {

	private static final char WHITE = '0';
	private static final char RED = '1';
	private static final char GREEN = '2';
	private static final char BLACK = '9';

	public static void setFont(TextView view, Context context,
			AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.TextView);
		String fontName = a.getString(R.styleable.TextView_fontFamily);

		try {
			Typeface tf = Typeface.createFromAsset(context.getAssets(),
					"fonts/" + fontName);// fontName);
			view.setTypeface(tf);
		} catch (Exception e) {
			Log.e(e.getClass().getName(), e.getMessage());
		}

		a.recycle();
	}

	public static SpannableStringBuilder parseColors(CharSequence text) {

		Pattern myPattern = Pattern.compile("\\$\\d");
		SpannableStringBuilder myBuilder = new SpannableStringBuilder(
				"http://schemas.android.com/apk/res-auto");

		if (myPattern != null) {
			Matcher myMatcher = myPattern.matcher(text);

			while (myMatcher.find()) {
				char colorChar = text.charAt(myMatcher.start() + 1);
				int color = Color.BLACK;

				switch (colorChar) {
				case WHITE:
					color = Color.WHITE;
					break;
				case RED:
					color = Color.RED;
					break;
				case GREEN:
					color = Color.GREEN;
					break;
				case '3':
					color = Color.BLUE;
					break;
				case '4':
					color = Color.MAGENTA;
					break;
				case '5':
					color = Color.DKGRAY;
					break;
				case '6':
					color = Color.YELLOW;
					break;
				case '7':
					color = Color.CYAN;
					break;
				case '8':
					color = Color.LTGRAY;
					break;
				case BLACK:
				default:
					color = Color.BLACK;
					break;
				}

				myBuilder.setSpan(new ForegroundColorSpan(color),
						myMatcher.start(), text.length(), 0);
				myBuilder.replace(myMatcher.start(), myMatcher.start() + 2, "");
			}
		}

		return myBuilder;
	}

	public static String readFile(Context context, String filename)
			throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				context.getAssets().open(filename), "UTF-8"));

		// do reading, usually loop until end of file reading
		String line;
		StringBuilder myBuilder = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			myBuilder.append(line);
		}

		reader.close();

		return myBuilder.toString();
	}

	public static float getDistance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	public static final byte[] intToByteArray(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16),
				(byte) (value >>> 8), (byte) value };
	}
}
