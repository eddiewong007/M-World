package com.mworld.ui;

import net.tsz.afinal.FinalActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mworld.utils.PreUtils;
import com.weibo.entities.AccessToken;

public class MworldActivity extends Activity {

	private AccessToken mAccessToken = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mworld);
		FinalActivity.initInjectedView(this);
		initComponents();
	}

	private void initComponents() {
		mAccessToken = PreUtils.readAccessToken(MworldActivity.this);
	}

	public void friendsTimeline(View v) {
		Intent intent = new Intent(MworldActivity.this, DisplayActivity.class);
		intent.putExtra("type", 0);
		intent.putExtra("uid", mAccessToken.uid);
		startActivity(intent);
	}

	public void toMe(View v) {
		Intent intent = new Intent(MworldActivity.this, DisplayActivity.class);
		intent.putExtra("type", 1);
		intent.putExtra("uid", mAccessToken.uid);
		startActivity(intent);
	}

	public void comMe(View v) {
		Intent intent = new Intent(MworldActivity.this, DisplayActivity.class);
		intent.putExtra("type", 2);
		intent.putExtra("uid", mAccessToken.uid);
		startActivity(intent);
	}

	public void profile(View v) {
		Intent intent = new Intent(MworldActivity.this, DisplayActivity.class);
		intent.putExtra("type", 3);
		intent.putExtra("uid", mAccessToken.uid);
		startActivity(intent);
	}

	public void logout(View v) {
		PreUtils.clear(this);
		startActivity(new Intent(MworldActivity.this, LoginActivity.class));
		finish();
	}

}
