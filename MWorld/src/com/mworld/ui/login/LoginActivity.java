package com.mworld.ui.login;

import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.weibo.api.UserAPI;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.oauth.OauthConstants;

public class LoginActivity extends FragmentActivity implements OauthConstants {

	private static final String TAG = LoginActivity.class.getName();

	private static boolean oauthing = false;

	@ViewInject(id = R.id.username)
	private EditText username;
	@ViewInject(id = R.id.passworld)
	private EditText password;
	@ViewInject(id = R.id.login, click = "login")
	private Button loginBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginactivity_layout);
		FinalActivity.initInjectedView(this);

	}

	public void login(View view) {
		String name = username.getEditableText().toString();
		String pass = password.getEditableText().toString();
		if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pass)) {
			Toast.makeText(LoginActivity.this, "请输入用户名/密码", Toast.LENGTH_SHORT)
					.show();
		}

		AjaxParams params = new AjaxParams();
		params.put("username", name);
		params.put("password", pass);
		params.put("client_id", APP_KEY0);
		params.put("client_secret", APP_SECRET0);
		params.put("grant_type", "password");
		new FinalHttp().post("https://api.weibo.com/2/oauth2/access_token",
				params, new TokenHandler());
		new ProgressFragment().show(getSupportFragmentManager(), "");
		oauthing = true;

	}

	private class TokenHandler extends AjaxCallBack<String> {

		@Override
		public void onSuccess(String jsonString) {
			super.onSuccess(jsonString);
			if (!oauthing) {
				handleOauthFailure();
				return;
			}
			Account account = Account.parse(jsonString);
			if (account == null || TextUtils.isEmpty(account.getUid())) {
				handleOauthFailure();
				return;
			}
			setResult(RESULT_OK, new Intent());
			new UserAPI(account.getAccessToken()).show(Long.parseLong(account
					.getUid()), new AccountHandler(account));

		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			handleOauthFailure();
			Log.e(TAG, strMsg);
		}

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
				fd.update(mAccount, "uid=\'" + mAccount.getUid() + "\'");
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
		Toast.makeText(LoginActivity.this, getString(R.string.oauth_failed),
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
			LoginActivity.this.finish();
			super.onCancel(dialog);
		}

	};

	public class SinaWeiboErrorDialog extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.sina_server_error).setPositiveButton(
					R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			return builder.create();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	};

}