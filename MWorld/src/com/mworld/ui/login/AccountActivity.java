package com.mworld.ui.login;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mworld.R;
import com.mworld.support.preference.PrefUtility;
import com.mworld.support.utils.GlobalContext;
import com.mworld.support.utils.ThemeUtility;
import com.mworld.support.utils.Utility;
import com.mworld.ui.main.MainTimelineActivity;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.entities.User;

public class AccountActivity extends Activity implements
		LoaderManager.LoaderCallbacks<List<Account>> {

	private static final String ACTION_OPEN_FROM_APP_INNER = "com.mworld:accountactivity";

	private static final int ADD_ACCOUNT_REQUEST_CODE = 0;

	private static final int LOADER_ID = 0;

	@ViewInject(id = R.id.listView)
	private ListView listView;

	private AccountAdapter listAdapter = null;

	private List<Account> accountList = new ArrayList<Account>();

	public static Intent newIntent() {
		Intent intent = new Intent(GlobalContext.getInstance(),
				AccountActivity.class);
		intent.setAction(ACTION_OPEN_FROM_APP_INNER);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (PrefUtility.firstStart()) {
			GlobalContext.getInstance().setActivity(this);
			showGuideActivity();
		}

		if (getIntent() != null
				&& !ACTION_OPEN_FROM_APP_INNER.equals(getIntent().getAction())) {
			jumpToMainTimeLineActivity();
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.accountactivity_layout);
		FinalActivity.initInjectedView(this);
		getActionBar().setTitle(getString(R.string.app_name));

		listAdapter = new AccountAdapter();
		listView.setOnItemClickListener(new AccountListItemClickListener());
		listView.setAdapter(listAdapter);
		listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new AccountMultiChoiceModeListener());
		getLoaderManager().initLoader(LOADER_ID, null, this);
	}

	private void showGuideActivity() {
		Intent intent = new Intent();
		intent.setClass(this, GuideActivity.class);
		startActivity(intent);
	}

	private void jumpToMainTimeLineActivity() {

		String id = PrefUtility.getDefaultAccountId();

		if (!TextUtils.isEmpty(id)) {
			Account account = FinalDb.create(GlobalContext.getInstance(), true)
					.findById(id, Account.class);
			if (account != null) {
				Intent start = MainTimelineActivity.newIntent(account);
				startActivity(start);
				finish();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar_menu_accountactivity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_account:
			@SuppressWarnings("rawtypes")
			final ArrayList<Class> activityList = new ArrayList<Class>();
			ArrayList<String> itemValueList = new ArrayList<String>();

			activityList.add(OauthActivity.class);
			itemValueList.add(getString(R.string.web_login));

			if (Utility.isCertificateFingerprintCorrect(AccountActivity.this)
					&& Utility.isSinaWeiboSafe(this)) {
				activityList.add(SSOActivity.class);
				itemValueList.add(getString(R.string.official_app_login));
			}

			activityList.add(LoginActivity.class);
			itemValueList.add(getString(R.string.direct_login));

			new AlertDialog.Builder(this).setItems(
					itemValueList.toArray(new String[0]),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(AccountActivity.this,
									activityList.get(which));
							startActivityForResult(intent,
									ADD_ACCOUNT_REQUEST_CODE);
						}
					}).show();
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADD_ACCOUNT_REQUEST_CODE && resultCode == RESULT_OK)
			refresh();
	}

	private void refresh() {
		getLoaderManager().getLoader(LOADER_ID).forceLoad();
	}

	@Override
	public Loader<List<Account>> onCreateLoader(int id, Bundle values) {
		return new AccountDBLoader(AccountActivity.this, values);
	}

	@Override
	public void onLoadFinished(Loader<List<Account>> loader, List<Account> data) {
		accountList = data;
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<List<Account>> arg0) {
		accountList = new ArrayList<Account>();
		listAdapter.notifyDataSetChanged();
	}

	private class AccountListItemClickListener implements
			AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int i,
				long l) {

			Intent intent = MainTimelineActivity.newIntent(accountList.get(i));
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}

	private class AccountMultiChoiceModeListener implements
			AbsListView.MultiChoiceModeListener {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.getMenuInflater().inflate(
					R.menu.contextual_menu_accountactivity, menu);
			mode.setTitle(getString(R.string.account_management));
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.menu_remove_account:
				remove();
				mode.finish();
				return true;
			}
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {

		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position,
				long id, boolean checked) {
			listAdapter.notifyDataSetChanged();
		}
	}

	private class AccountAdapter extends BaseAdapter {

		int checkedBG;

		int defaultBG;

		public AccountAdapter() {
			defaultBG = getResources().getColor(android.R.color.transparent);
			checkedBG = ThemeUtility.getColor(AccountActivity.this,
					R.attr.listview_checked_color);
			checkedBG = getResources().getColor(android.R.color.holo_blue_dark);

		}

		@Override
		public int getCount() {
			return accountList.size();
		}

		@Override
		public Object getItem(int position) {
			return accountList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return accountList.get(position).getId();
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup container) {

			Account account = accountList.get(position);
			LayoutInflater layoutInflater = getLayoutInflater();

			convertView = layoutInflater.inflate(
					R.layout.accountactivity_listview_item_layout, container,
					false);

			if (listView.getCheckedItemPositions().get(position)) {
				convertView.findViewById(R.id.listview_root)
						.setBackgroundColor(checkedBG);
			} else {
				convertView.findViewById(R.id.listview_root)
						.setBackgroundColor(defaultBG);
			}

			TextView textView = (TextView) convertView
					.findViewById(R.id.account_name);
			if (account.getUserInfo() != null) {
				textView.setText(account.getUserInfo().getScreenName());
			} else {
				textView.setText(account.getUid());
			}
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.imageView_avatar);

			if (!TextUtils.isEmpty(account.getAvatarUrl())) {
				FinalBitmap.create(GlobalContext.getInstance()).display(
						imageView, account.getAvatarUrl());
			}

			TextView token = (TextView) convertView
					.findViewById(R.id.token_expired);
			if (!Utility.isTokenValid(account)) {
				token.setVisibility(View.VISIBLE);
			} else {
				token.setVisibility(View.GONE);
			}

			return convertView;
		}

	}

	private static class AccountDBLoader extends AsyncTaskLoader<List<Account>> {

		public AccountDBLoader(Context context, Bundle values) {
			super(context);
		}

		@Override
		protected void onStartLoading() {
			super.onStartLoading();
			forceLoad();
		}

		@Override
		public List<Account> loadInBackground() {
			FinalDb fd = FinalDb.create(GlobalContext.getInstance(), true);
			List<Account> list = fd.findAll(Account.class);
			for (Account item : list) {
				User user = User.parse(item.getJsonUserInfo());
				item.setUserInfo(user);
			}
			return list;
		}

	}

	private void remove() {
		FinalDb fd = FinalDb.create(GlobalContext.getInstance(), true);

		long[] ids = listView.getCheckedItemIds();
		for (long id : ids) {
			fd.deleteById(Account.class, id);
		}
		accountList = fd.findAll(Account.class);
		for (Account item : accountList) {
			User user = User.parse(item.getJsonUserInfo());
			item.setUserInfo(user);
		}
		refresh();
	}

}
