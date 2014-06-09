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
