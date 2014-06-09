package eu.sathra.scene;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;
import eu.sathra.ai.Task;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.physics.Body;
import eu.sathra.resources.Font;
import eu.sathra.scene.animation.Animation;
import eu.sathra.video.opengl.Sprite;

/**
 * @deprecated
 * This class needs to be redesigned to fit into the new resource system
 */
@Deprecated
public class TextNode extends SceneNode {

	public enum Alignment {
		LEFT(-1), 
		RIGHT(0), 
		CENTER(-0.5f);

		private float mOffset;
		
		private Alignment(float offset) {
			mOffset = offset;
		}
		
		public float getOffset() {
			return mOffset;
		}
	}

	public static final char DEFAULT_COLOR_PREFIX = '^';
	public static final char COLOR_PREFIX_WHITE = '0';
	public static final char COLOR_PREFIX_RED = '1';
	public static final char COLOR_PREFIX_GREEN = '2';
	public static final char COLOR_PREFIX_BLUE = '3';
	public static final char COLOR_PREFIX_YELLOW = '4';
	public static final char COLOR_PREFIX_MAGENTA = '5';
	public static final char COLOR_PREFIX_MINT_GREEN = '6';
	public static final char COLOR_PREFIX_BABY_BLUE = '7';
	public static final char COLOR_PREFIX_GREY = '8';
	public static final char COLOR_PREFIX_BLACK = '9';

	private static Map<Character, Integer> sColorCodes = new HashMap<Character, Integer>();

	static {
		sColorCodes.put(COLOR_PREFIX_WHITE, Color.WHITE);
		sColorCodes.put(COLOR_PREFIX_RED, Color.RED);
		sColorCodes.put(COLOR_PREFIX_GREEN, Color.GREEN);
		sColorCodes.put(COLOR_PREFIX_BLUE, Color.BLUE);
		sColorCodes.put(COLOR_PREFIX_YELLOW, Color.YELLOW);
		sColorCodes.put(COLOR_PREFIX_MAGENTA, Color.MAGENTA);
		sColorCodes.put(COLOR_PREFIX_MINT_GREEN, 0xFF29AB87);
		sColorCodes.put(COLOR_PREFIX_BABY_BLUE, 0xFF89CFF0);
		sColorCodes.put(COLOR_PREFIX_GREY, Color.GRAY);
		sColorCodes.put(COLOR_PREFIX_BLACK, Color.BLACK);
	}

	private Font mFont;
	private String mText;
	private char mColorPrefix;
	private Boolean mParseColors;
	private int mColor;
	private Alignment mAlignment;
	private float mTextWidth;

	public TextNode(Transform t, Font font, String text,
			boolean parseColors) {
		this(null, t, font, text, Alignment.CENTER, parseColors, true, null,
				null, null, null);
	}

	@Deserialize({ "id", "transform", "font", "text", "alignment", "parse_colors",
			"is_visible", "animation", "children", "body", "ai" })
	@Defaults({ Deserialize.NULL, Deserialize.NULL, Deserialize.NULL, "", "CENTER", "true",
			"true", Deserialize.NULL, Deserialize.NULL, Deserialize.NULL,
			Deserialize.NULL, Deserialize.NULL })
	public TextNode(String id, Transform t, Font font, String text,
			Alignment alignment, boolean parseColors, boolean isVisible,
			Animation animation, SceneNode[] children, Body body, Task ai) {
		super(id, t, isVisible, animation, children, body, ai);

		setColorPrefix(DEFAULT_COLOR_PREFIX);
		setFont(font);
		setText(text);
		setParseColors(parseColors);
		setColor(Color.WHITE);
		setAlignment(alignment);
	}

	protected void draw(GL10 gl, long time, long delta) {
		int currentColor = getColor();

		gl.glPushMatrix();
		gl.glTranslatef(mAlignment.getOffset()*mTextWidth, 0, 0);
		
		mTextWidth = 0;
		for (int c = 0; c < mText.length(); ++c) {
			char currentChar = mText.charAt(c);

			Sprite charSprite = mFont.getCharacter(currentChar);
			if (isParsingColors() && currentChar == getColorPrefix()
					&& mText.length() > c + 1) {
				char colorCode = mText.charAt(c + 1);
				if (sColorCodes.containsKey(colorCode)) {
					currentColor = sColorCodes.get(colorCode);
					++c;
					continue;
				}
			}
			mTextWidth+=charSprite.getWidth();
			charSprite.setColor(currentColor);
			charSprite.draw(gl);
			charSprite.setColor(Color.WHITE);
			gl.glTranslatef(charSprite.getWidth(), 0, 0);
		}

		gl.glPopMatrix();
	}

	public void setFont(Font font) {
		mFont = font;
	}

	public Font getFont() {
		return mFont;
	}

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		mText = text;
	}

	public boolean isParsingColors() {
		return mParseColors;
	}

	public void setParseColors(boolean parseColors) {
		mParseColors = parseColors;
	}

	public char getColorPrefix() {
		return mColorPrefix;
	}

	public void setColorPrefix(char prefix) {
		mColorPrefix = prefix;
	}

	public void setColor(int color) {
		mColor = color;
	}

	public int getColor() {
		return mColor;
	}

	public Alignment getAlignment() {
		return mAlignment;
	}

	public void setAlignment(Alignment alignment) {
		mAlignment = alignment;
	}

}
