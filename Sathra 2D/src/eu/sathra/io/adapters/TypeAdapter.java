package eu.sathra.io.adapters;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class for implementing own deserialization logic. Useful for third-party
 * library classes.
 * 
 * @author Milosz Moczkowski
 * 
 * @param <T> Class type this adapter can deserialize.
 */
public interface TypeAdapter<T> {
	T load(String param, JSONObject parent) throws JSONException, IOException;
}
