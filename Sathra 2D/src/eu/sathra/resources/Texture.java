package eu.sathra.resources;


public class Texture extends ResourceHandle {

	private int mWidth;
	private int mHeight;

	public Texture(int handle, int width, int height) {
		super(handle);
		mWidth = width;
		mHeight = height;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}
}
