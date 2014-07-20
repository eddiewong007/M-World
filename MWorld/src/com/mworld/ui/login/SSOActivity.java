package com.mworld.ui.login;

import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.weibo.api.UserAPI;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.oauth.SsoHandler;

public class SSOActivity extends FragmentActivity {

	private static final String TAG = SSOActivity.class.getName();

	private static boolean oauthing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setTitle(R.string.official_app_login);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		SsoHandler ssoHandler = new SsoHandler(SSOActivity.this);
		ssoHandler.authorize();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_CANCELED || data == null) {
			Toast.makeText(this, R.string.you_cancel_login, Toast.LENGTH_SHORT)
					.show();
			finish();
			return;
		}

		String error = data.getStringExtra("error");
		if (error == null) {
			error = data.getStringExtra("error_type");
		}

		if (data.getStringExtra("error") != null) {
			if (error.equals("access_denied")
					|| error.equals("OAuthAccessDeniedException")) {
				Log.d("Weibo-authorize", "Login canceled by user.");
				Toast.makeText(this, R.string.you_cancel_login,
						Toast.LENGTH_SHORT).show();
				finish();
			} else {
				String description = data.getStringExtra("error_description");
				if (description != null) {
					error = error + ":" + description;
				}
				Log.d("Weibo-authorize", "Login failed: " + error);
				Toast.makeText(this, getString(R.string.oauth_failed) + error,
						Toast.LENGTH_SHORT).show();
				finish();

			}
			return;

		}

		String token = data.getStringExtra("access_token");
		String expires = data.getStringExtra("expires_in");
		String uid = data.getStringExtra("uid");
		long expiresIn = System.currentTimeMillis() + Long.parseLong(expires)
				* 1000;
		Account account = new Account();
		account.setAccessToken(token);
		account.setExpiresIn(expiresIn);
		account.setUid(uid);

		oauthing = true;
		setResult(RESULT_OK, new Intent());
		new ProgressFragment().show(getSupportFragmentManager(), "");
		new UserAPI(account.getAccessToken()).show(
				Long.parseLong(account.getUid()), new AccountHandler(account));

	}

	private class AccountHandler extends AjaxCallBack<String> {

		private Account mAccount;

		AccountHandler(Account account) {
			mAccount = account;
		}

		@Override
		public void onSuccess(String jsonString) {
			super.onSuccess(jsonString);
			if (!oauthing) {
				handleOauthFailure();
				return;
			}
			FinalDb fd = FinalDb.create(GlobalContext.getInstance(), true);
			List<Account> accounts = fd.findAllByWhere(Account.class, "uid=\'"
					+ mAccount.getUid() + "\'");
			mAccount.setJsonUserInfo(jsonString);
			if (accounts.isEmpty()) {
				fd.save(mAccount);
			} else {
				fd.update(mAccount, "uid=" + mAccount.getUid());
			}
			finish();
		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			handleOauthFailure();
			Log.e(TAG, strMsg);
		}

	}

	private void handleOauthFailure() {
		Toast.makeText(SSOActivity.this, getString(R.string.oauth_failed),
				Toast.LENGTH_SHORT).show();
		finish();
	}

	private class ProgressFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			ProgressDialog dialog = new ProgressDialog(getActivity());
			dialog.setMessage(getString(R.string.oauthing));
			dialog.setIndeterminate(false);
			dialog.setCancelable(true);

			return dialog;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			oauthing = false;
			SSOActivity.this.finish();
			super.onCancel(dialog);
		}

	};

}
