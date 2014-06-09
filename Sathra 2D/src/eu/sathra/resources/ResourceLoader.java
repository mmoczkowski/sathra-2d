package eu.sathra.resources;

import java.io.IOException;

public interface ResourceLoader<T> {

	T load(String path) throws IOException;
	void unload(T resource);
}
