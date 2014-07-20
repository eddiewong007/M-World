package com.mworld.ui.main;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.ui.login.AccountActivity;
import com.mworld.weibo.entities.User;
import com.mworld.weibo.oauth.Oauth2API;

public class DialogActivity extends Activity {

	@ViewInject(id = R.id.llayout01, click = "click")
	private LinearLayout layout01;
	@ViewInject(id = R.id.llayout02, click = "click")
	private LinearLayout layout02;
	@ViewInject(id = R.id.llayout03, click = "click")
	private LinearLayout layout03;
	@ViewInject(id = R.id.llayout04, click = "click")
	private LinearLayout layout04;
	@ViewInject(id = R.id.llayout05, click = "click")
	private LinearLayout layout05;

	@ViewInject(id = R.id.head_icon)
	ImageView mHeadIcon;
	@ViewInject(id = R.id.head_name)
	TextView mHeadName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogactivity_layout);
		FinalActivity.initInjectedView(this);
		findViewById(R.id.llayout01).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				click(view);
			}
		});
		findViewById(R.id.llayout05).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				click(view);
			}
		});
		setHeadIcon();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void click(View view) {

		switch (view.getId()) {
		case R.id.llayout01:
			Intent intent = new Intent(this, ProfileActivity.class);
			intent.putExtra("user", GlobalContext.getInstance().getAccount()
					.getUserInfo());
			startActivity(intent);
			break;
		case R.id.llayout05:
			startActivity(AccountActivity.newIntent());
			Oauth2API.revokeOauth2(GlobalContext.getInstance().getAccount()
					.getAccessToken(), new AjaxCallBack<String>() {
			});
			finish();
			GlobalContext.getInstance().getActivity().finish();
			break;
		default:
			break;
		}
		finish();
	}

	private void setHeadIcon() {
		User user = GlobalContext.getInstance().getAccount().getUserInfo();
		FinalBitmap.create(this).display(mHeadIcon, user.getAvatarLarge());
		mHeadName.setText(user.getScreenName());
	}
}