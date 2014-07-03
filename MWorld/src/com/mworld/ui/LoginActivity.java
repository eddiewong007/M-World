package com.mworld.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.mworld.utils.AccessTokenKeeper;

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

	public void login(View v) {
		Intent intent = new Intent(LoginActivity.this, OauthActivity.class);
		startActivity(intent);
	}

	public void changeApikey(View v) {
		mLoginLogo.startAnimation(mAlphaAnimation);
		mApiKey.startAnimation(mScaleAnimation);
	}

	private void judgeIsLoggedIn() {

		if (null == AccessTokenKeeper.readAccessToken(LoginActivity.this)) {
			instance = this;
			loadAnimation();
		} else {
			Intent intent = new Intent(LoginActivity.this, MworldActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void loadAnimation() {
		mAlphaAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
		mLoginLogo = findViewById(R.id.login_logo);
		mLoginLogo.setAnimation(mAlphaAnimation);

		mScaleAnimation = AnimationUtils
				.loadAnimation(this, R.anim.apikey_anim);
		mApiKey = findViewById(R.id.btn_apikey);
		mApiKey.setAnimation(mScaleAnimation);
	}

}
