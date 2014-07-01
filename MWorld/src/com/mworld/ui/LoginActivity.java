package com.mworld.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class LoginActivity extends Activity {

	private Animation mAlphaAnimation;
	private Animation mScaleAnimation;
	private View mLoginLogo;
	private View mApiKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mAlphaAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
		mLoginLogo = findViewById(R.id.login_logo);
		mLoginLogo.setAnimation(mAlphaAnimation);

		mScaleAnimation = AnimationUtils
				.loadAnimation(this, R.anim.apikey_anim);
		mApiKey = findViewById(R.id.btn_apikey);
		mApiKey.setAnimation(mScaleAnimation);

	}

	public void login(View v) {
		startActivity(new Intent(LoginActivity.this, OauthActivity.class));
	}

	public void changeApikey(View v) {
		mLoginLogo.startAnimation(mAlphaAnimation);
		mApiKey.startAnimation(mScaleAnimation);
	}

}
