package com.mworld.ui.login;

import android.app.Activity;
import android.os.Bundle;

import com.mworld.R;

public class SSOActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setTitle(R.string.official_app_login);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);

	}

}
