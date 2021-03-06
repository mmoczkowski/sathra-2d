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
package eu.sathra.ai.context;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import eu.sathra.SathraActivity;
import eu.sathra.scene.SceneNode;

public class WebContext implements AIContext {

	private class JSCallback implements ValueCallback<String> {

		private int mId;
		
		public JSCallback(int id) {
			mId = id;
		}
		
		@Override
		public void onReceiveValue(String value) {
			
		}
		
	}
	
	private WebView mWebView;
	private SceneNode mOwner;
	private Map<String, Object> mVariables;
	private static final String JS_INTERFACE = "android";

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	public WebContext(SceneNode owner) {
		mWebView = new WebView(SathraActivity.getCurrentSathra());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(this, JS_INTERFACE);
		mVariables = new HashMap<String, Object>();
		mOwner = owner;
	}

	@Override
	public SceneNode getOwner() {
		return mOwner;
	}

	@Override
	public void setOwner(SceneNode owner) {
		mOwner = owner;
	}

	@Override
	public void setVariable(String name, Object var) {
		mVariables.put(name, var);
		mWebView.addJavascriptInterface(var, name);
	}

	@Override
	public Object getVariable(String name) {
		return mVariables.get(name);
	}

	public <T> T getVariable(String name, Class<T> type) {
		try {
			return (T) getVariable(name);
		} catch (ClassCastException e) {
			return null;
		}
	}

	@Override
	public Object eval(String expression) throws ExpressionException {
		mWebView.addJavascriptInterface(this, "android");
		mWebView.loadUrl("javascript:android.onResult("+expression+")");



		return null;
	}
	
	@JavascriptInterface
	public void onResult() {
		Log.e("pagelx", "gunwo");
	}

}
