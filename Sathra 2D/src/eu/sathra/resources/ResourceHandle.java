package eu.sathra.resources;

/**
 * A resource handle is a POJO containing a unique integer resource id
 * recognizable by the desired resource subsystem. It does not contain resource
 * data (e.g. bitmap or sound) as it is only supposed to "point" to the data. 
 * 
 * @tag Do not create ResourceHandles yourself. Use ResourceManager.
 * 
 * @author Milosz Moczkowski
 * 
 */
public class ResourceHandle {

	private int mHandle;
	private int mReferences;

	public ResourceHandle(int handle) {
		mHandle = handle;
		mReferences = 1;
	}

	public int getHandle() {
		return mHandle;
	}
	
	public void grab() {
		++mReferences;
	}
	
	public void drop() {
		--mReferences;
	}
}
