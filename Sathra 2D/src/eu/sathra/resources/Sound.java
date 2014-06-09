package eu.sathra.resources;

public class Sound extends ResourceHandle {
	
	private int mLength;
	private int mChannels;
	
	public Sound(int handle, int lenght, int channels) {
		super(handle);
	}
	
	public int getLength() {
		return mLength;
	}
	
	public int getChannels() {
		return mChannels;
	}
}
