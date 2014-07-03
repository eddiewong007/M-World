package com.mworld.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.mworld.utils.AccessTokenKeeper;
import com.mworld.weibo.api.Oauth2API;
import com.mworld.weibo.api.Oauth2API.WeiboWebViewClient;
import com.mworld.weibo.entities.AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class OauthActivity extends Activity {

	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SwipeBack.attach(this, Position.LEFT)
				.setContentView(R.layout.activity_oauth)
				.setSwipeBackView(R.layout.swipeback_default);
		initWebView();

	}

	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	private void initWebView() {
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new WeiboWebViewClient(new TokenHandler()));
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSaveFormData(false);
		webSettings.setSavePassword(false);
		webSettings.setCacheMode(2);
		webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
		mWebView.loadUrl(Oauth2API.fetchAuthorizeUrl());
	}

	public class TokenHandler implements RequestListener {

		@Override
		public void onComplete(String jsonString) {
			Log.i("------------------", "回调");
			AccessToken accessToken = AccessToken.parse(jsonString);
			if (null == accessToken) {
				Toast.makeText(OauthActivity.this, "授权失败，请重新授权！",
						Toast.LENGTH_SHORT).show();
				mWebView.loadUrl(Oauth2API.fetchAuthorizeUrl());
			} else {
				AccessTokenKeeper.keepAccessToken(OauthActivity.this,
						accessToken);
				startActivity(new Intent(OauthActivity.this,
						MworldActivity.class));
				LoginActivity.instance.finish();
				finish();
			}

		}

		@Override
		public void onWeiboException(WeiboException e) {
			Log.e("----------------------", e.getMessage());
		}

	}

}
