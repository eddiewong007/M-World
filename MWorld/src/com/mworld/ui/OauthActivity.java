package com.mworld.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

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
		setContentView(R.layout.activity_oauth);
		initWebView();

	}

	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	private void initWebView() {
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new WeiboWebViewClient(getIntent().getStringExtra("username"),
				getIntent().getStringExtra("password"), new TokenHandler()));
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
			if (null != AccessTokenKeeper.readAccessToken(OauthActivity.this)) return;
			AccessToken accessToken = AccessToken.parse(jsonString);
			if (null == accessToken) {
				Toast.makeText(OauthActivity.this, "授权失败，请重新授权！",
						Toast.LENGTH_SHORT).show();
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
