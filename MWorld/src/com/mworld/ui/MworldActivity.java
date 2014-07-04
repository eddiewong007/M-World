package com.mworld.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mworld.utils.AccessTokenKeeper;
import com.mworld.weibo.entities.AccessToken;

public class MworldActivity extends Activity {
	private AccessToken mAccessToken = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mworld);
		initComponents();
	}

	private void initComponents() {
		mAccessToken = AccessTokenKeeper.readAccessToken(MworldActivity.this);
		((TextView) findViewById(R.id.actionbar_title)).setText("M-World");
		// ((ImageView) findViewById(R.id.home)).setImageDrawable(getResources()
		// .getDrawable(R.drawable.home));
		// ((ImageView) findViewById(R.id.friends))
		// .setImageDrawable(getResources().getDrawable(R.drawable.home));
		// ((ImageView)
		// findViewById(R.id.search)).setImageDrawable(getResources()
		// .getDrawable(R.drawable.home));

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
		AccessTokenKeeper.clear(this);
		startActivity(new Intent(MworldActivity.this, LoginActivity.class));
		finish();
	}

}
