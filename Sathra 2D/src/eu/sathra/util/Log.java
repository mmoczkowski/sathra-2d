/*******************************************************************************
 * Copyright 2014 SATHRA Milosz Moczkowski, milosz.moczkowski@sathra.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
