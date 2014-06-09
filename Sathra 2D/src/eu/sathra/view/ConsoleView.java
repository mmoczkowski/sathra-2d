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
package eu.sathra.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.AttributeSet;

public class ConsoleView extends TextView {

	public ConsoleView(Context context) {
		super(context);
		
		initialize() ;
	}
	
	public ConsoleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initialize() ;
	}
	
	public ConsoleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initialize() ;
	}
	
	private void initialize() {
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            try {
	                Process process = Runtime.getRuntime().exec("logcat *:I");
	                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

	                String line;
	                while ((line = bufferedReader.readLine()) != null) {
	                	final String tmp = line;
	                	ConsoleView.this.post(new Runnable() {

							@Override
							public void run() {
								append(tmp);
								scrollTo(0, getBottom());
							}
	                		
	                	});
	                    
	                }
	            } catch (IOException e) {
	            }
	        }
	    }).start();
	}

}
