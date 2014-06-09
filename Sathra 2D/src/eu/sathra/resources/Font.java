package eu.sathra.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.Typeface;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.video.opengl.Sprite;

/**
 * @deprecated
 * This class needs to be redesigned to fit into the new resource system
 */
@Deprecated
public class Font {

	private static final char START_CHAR = 32;
	private static final char END_CHAR = 130;
	private static final char CHAR_COUNT = END_CHAR - START_CHAR;
	private static final char UNKNOWN_CHAR = 130;

	private Map<Character, Sprite> mSprites = new HashMap<Character, Sprite>();
	private FontMetricsInt mMetrics;
	
	public Font(Context context) throws IOException {
		this(context, Typeface.DEFAULT);
	}
	
	@Deserialize( { Deserialize.NULL, "font_path"} )
	@Defaults( { Deserialize.NULL, Deserialize.NULL} )
	public Font(Context context, String fontPath) throws IOException {
		this(context, Typeface.createFromAsset(context.getAssets(), fontPath));
	}

	public Font(Context context, Typeface typeface) throws IOException {
		// generate text table
		char table[] = new char[CHAR_COUNT];
		for (char c = START_CHAR; c < END_CHAR; ++c)
			table[c - START_CHAR] = c;

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(Color.WHITE);
		
		if(typeface!=null)
			paint.setTypeface(typeface);
		
		float scale = context.getResources().getDisplayMetrics().density;
		paint.setTextSize(10 * scale); // TODO

		mMetrics = paint.getFontMetricsInt();

		Rect bounds = new Rect();
		paint.getTextBounds(table, 0, table.length, bounds);

		int fontHeight = Math.abs(mMetrics.bottom) + Math.abs(mMetrics.top)
				+ Math.abs(mMetrics.ascent) + Math.abs(mMetrics.descent);

		Bitmap bitmap = Bitmap.createBitmap(bounds.width(), fontHeight,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.TRANSPARENT);
		canvas.drawText(table, 0, table.length, 0, fontHeight / 2, paint);

		//Texture fontTexture = new Texture(bitmap);
		Texture fontTexture = null;// TODO
		float widths[] = new float[table.length];
		paint.getTextWidths(table, 0, table.length, widths);

		int x = 0;
		for (int c = 0; c < table.length; ++c) {

			Rect clip = new Rect(x, 0, (int) (x + widths[c]), fontHeight);

			mSprites.put(table[c], new Sprite(fontTexture, clip));

			x += widths[c];
		}
	}

	public Sprite getCharacter(char c) {
		return mSprites.containsKey(c) ? mSprites.get(c) : mSprites
				.get(UNKNOWN_CHAR);
	}

	public Sprite[] getCharacters(String text) {
		Sprite[] charsArray = new Sprite[text.length()];

		for (int c = 0; c < text.length(); ++c) {
			char currentChar = text.charAt(c);
			charsArray[c] = getCharacter(currentChar);
		}

		return charsArray;
	}
}
