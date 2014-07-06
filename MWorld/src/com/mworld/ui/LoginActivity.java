package com.mworld.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.Toast;

import com.mworld.utils.AccessTokenKeeper;
import com.mworld.weibo.api.Oauth2API;
import com.mworld.weibo.entities.AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class LoginActivity extends Activity {

	public static LoginActivity instance = null;

	private Animation mAlphaAnimation;
	private Animation mScaleAnimation;

	private View mLoginLogo;
	private View mApiKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		judgeIsLoggedIn();
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void gologin(View v) {
		String username = ((EditText) findViewById(R.id.editText1))
				.getEditableText().toString();
		String password = ((EditText) findViewById(R.id.editText2))
				.getEditableText().toString();
		Oauth2API.authorize(this, username, password, new TokenHandler());

	}

	public class TokenHandler implements RequestListener {

		@Override
		public void onComplete(String jsonString) {

			Log.i("------------------", "回调");
			AccessToken accessToken = AccessToken.parse(jsonString);
			if (null == accessToken) {
				Toast.makeText(LoginActivity.this, "授权失败，请重新授权！",
						Toast.LENGTH_SHORT).show();
				String username = ((EditText) findViewById(R.id.editText1))
						.getEditableText().toString();
				String password = ((EditText) findViewById(R.id.editText2))
						.getEditableText().toString();
				Intent intent = new Intent(LoginActivity.this,
						OauthActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				startActivity(intent);
				finish();
			} else {
				AccessTokenKeeper.keepAccessToken(LoginActivity.this,
						accessToken);
				startActivity(new Intent(LoginActivity.this,
						MworldActivity.class));
				finish();
			}

		}

		@Override
		public void onWeiboException(WeiboException e) {
			Log.e("----------------------", e.getMessage());
		}

	}

	public void login(View v) {
		String username = ((EditText) findViewById(R.id.editText1))
				.getEditableText().toString();
		String password = ((EditText) findViewById(R.id.editText2))
				.getEditableText().toString();
		Intent intent = new Intent(LoginActivity.this, OauthActivity.class);
		intent.putExtra("username", username);
		intent.putExtra("password", password);
		startActivity(intent);
		finish();

	}

	public void changeApikey(View v) {
		mLoginLogo.startAnimation(mAlphaAnimation);
		mApiKey.startAnimation(mScaleAnimation);
	}

	private void judgeIsLoggedIn() {

		if (null == AccessTokenKeeper.readAccessToken(LoginActivity.this)) {
			instance = this;
			// loadAnimation();
		} else {
			Intent intent = new Intent(LoginActivity.this, MworldActivity.class);
			startActivity(intent);
			finish();
		}
	}

	// private void loadAnimation() {
	// mAlphaAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
	// mLoginLogo = findViewById(R.id.login_logo);
	// mLoginLogo.setAnimation(mAlphaAnimation);
	//
	// mScaleAnimation = AnimationUtils
	// .loadAnimation(this, R.anim.apikey_anim);
	// mApiKey = findViewById(R.id.btn_apikey);
	// mApiKey.setAnimation(mScaleAnimation);
	// }

}
