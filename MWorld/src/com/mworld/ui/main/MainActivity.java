package com.mworld.ui.main;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mworld.R;
import com.mworld.support.preference.PrefUtility;
import com.mworld.support.utils.GlobalContext;
import com.mworld.ui.adapter.TabsAdapter;
import com.mworld.ui.fragment.AtFragment;
import com.mworld.ui.fragment.CommentFragment;
import com.mworld.ui.fragment.HomeFragment;
import com.mworld.weibo.api.StatusAPI;
import com.mworld.weibo.entities.Account;

public class MainActivity extends FragmentActivity {

	private Account mAccount;

	private ViewPager mViewPager;

	private TabsAdapter mTabsAdapter;

	public static Intent newIntent() {
		return new Intent(GlobalContext.getInstance(), MainActivity.class);
	}

	public static Intent newIntent(Account account) {
		Intent intent = newIntent();
		intent.putExtra("account_extra", account);
		return intent;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			mAccount = savedInstanceState.getParcelable("account");
		} else {
			Intent intent = getIntent();
			mAccount = intent.getParcelableExtra("account_extra");
		}

		if (mAccount == null) {
			mAccount = GlobalContext.getInstance().getAccount();
		}

		GlobalContext.getInstance().setAccount(mAccount);
		GlobalContext.getInstance().setActivity(this);
		PrefUtility.setDefaultAccountId(mAccount.getId());

		bindUserInterfaces(savedInstanceState);

	}

	private void bindUserInterfaces(Bundle savedInstanceState) {
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.vPager);
		setContentView(mViewPager);

		// ActionBar and Tabs
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);

		// 设置ActionBar的背景
		actionBar.setDisplayUseLogoEnabled(true);

		mTabsAdapter = new TabsAdapter(getSupportFragmentManager(), this,
				mViewPager);
		mTabsAdapter.addTab(actionBar.newTab().setText("Home"),
				HomeFragment.class, null);
		mTabsAdapter.addTab(actionBar.newTab().setText("At"), AtFragment.class,
				null);
		mTabsAdapter.addTab(actionBar.newTab().setText("Comment"),
				CommentFragment.class, null);

		if (savedInstanceState != null) {
			actionBar.setSelectedNavigationItem(savedInstanceState.getInt(
					"tab", 0));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		// 设置Menu可见
		MenuItem searchItem = menu.findItem(R.id.action_search);
		MenuItem addItem = menu.findItem(R.id.action_add);
		MenuItem moreItem = menu.findItem(R.id.action_more);
		MenuItemCompat.setShowAsAction(searchItem,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		MenuItemCompat.setShowAsAction(addItem,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		MenuItemCompat.setShowAsAction(moreItem,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			break;
		case R.id.action_add:
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, 1);
			break;
		case R.id.action_more:
			startActivity(new Intent(MainActivity.this, DialogActivity.class));

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

			Uri selectedImage = data.getData();

			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,

			filePathColumn, null, null, null);

			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

			String picturePath = cursor.getString(columnIndex);

			cursor.close();

			new StatusAPI(mAccount.getAccessToken()).upload("测试微博API中，忽略我", 0,
					"", picturePath, 0.0F, 0.0F, "[]", getLocalIpAddress(),
					new AjaxCallBack<String>() {

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							Log.i("-------------------", "发送图片成功");
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							Log.i("-------------------", t.getMessage()
									+ strMsg);
							Toast.makeText(MainActivity.this,
									t.getMessage() + strMsg, Toast.LENGTH_LONG)
									.show();
						}

						@Override
						public void onLoading(long count, long current) {
							super.onLoading(count, current);

							if (current % 10 == 0)
								Toast.makeText(MainActivity.this,
										count + "/" + current,
										Toast.LENGTH_LONG).show();
						}

					});

		}

	}

	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return "";
	}

}
