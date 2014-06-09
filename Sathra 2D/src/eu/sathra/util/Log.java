package eu.sathra.util;

public class Log {

	private static final String LOG_MESSAGE_FORMAT = "[%s::%s:%s]";

	public static void debug(Object message) {
		log(android.util.Log.DEBUG, message.toString());
	}
	
	public static void info(Object message) {
		log(android.util.Log.INFO, message.toString());
	}
	
	public static void warning(Object message) {
		log(android.util.Log.WARN, message.toString());
	}
	
	public static void error(Object message) {
		log(android.util.Log.ERROR, message.toString());
	}

	private static void log(int priority, String message) {
		StackTraceElement myElement = Thread.currentThread().getStackTrace()[4];

		String tag = String.format(LOG_MESSAGE_FORMAT, myElement.getClassName(),
				myElement.getMethodName(), myElement.getLineNumber());
		
		android.util.Log.println(priority, tag, message);
	}
}
