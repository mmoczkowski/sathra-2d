package eu.sathra;

public class Parameters {

	public enum Orientation {
		VERTICAL,
		HORIZONTAL
	}
	
	private static final int DEFAULT_MAX_FPS = 60;
	private static final int DEFAULT_BG_COLOR = 0xff000000;
	private static final int DEFAULT_LIGHT_COLOR = 0x00000000; // No shadow

	public boolean fullscreen;
	public int width = 0;
	public int height = 0;
	public boolean showFPS = false;
	//public int maxFPS = DEFAULT_MAX_FPS;
	public int bgColor = DEFAULT_BG_COLOR;
	public int ambientColor = DEFAULT_LIGHT_COLOR;
	public int layout = 0;
	public Orientation orientation = Orientation.HORIZONTAL;
	public boolean drawDebug = false;
	
	// antialias
	// color depth
}
