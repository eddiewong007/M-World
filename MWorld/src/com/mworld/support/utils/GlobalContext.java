package com.mworld.support.utils;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.mworld.support.preference.PrefUtility;
import com.mworld.ui.adapter.GroupListNavAdapter;
import com.mworld.ui.main.MainActivity;
import com.mworld.weibo.api.GroupAPI;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.entities.GroupList;

public class GlobalContext extends Application {

	private static GlobalContext globalContext;

	private Activity activity = null;

	private Account mAccount = null;

	private GroupList mGroup = null;

	private int mCurGroupNum = 0;

	private int currentTab = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		globalContext = this;
	}

	public static GlobalContext getInstance() {
		return globalContext;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;

	}

	public Account getAccount() {
		if (mAccount == null) {
			int id = PrefUtility.getDefaultAccountId();
			FinalDb fd = FinalDb.create(getInstance());
			mAccount = fd.findById(id, Account.class);
		}
		return mAccount;
	}

	public void setAccount(final Account account) {
		mAccount = account;

	}

	public String getCurrentAccountName() {
		return getAccount().getUserInfo().getScreenName();
	}

	public GroupList getGroup() {
		return mGroup;
	}

	public void setActivityGroup(final MainActivity activity,
			final OnNavigationListener callback) {
		if (mGroup == null) {
			new GroupAPI(getAccount().getAccessToken())
					.groups(new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							activity.getActionBar().setListNavigationCallbacks(
									new GroupListNavAdapter(activity,
											new String[1]), callback);
							Toast.makeText(activity, "暂时还没有权限获取分组信息，请耐心等待",
									Toast.LENGTH_LONG).show();
							Log.i("分组", t.getMessage() + strMsg);
						}

						@Override
						public void onSuccess(String jsonString) {
							super.onSuccess(jsonString);
							mGroup = GroupList.parse(jsonString);
							activity.getActionBar().setListNavigationCallbacks(
									new GroupListNavAdapter(activity,
											mGroup.groupStrings), callback);
						}

					});
		} else {
			activity.getActionBar().setListNavigationCallbacks(
					new GroupListNavAdapter(activity, mGroup.groupStrings),
					callback);
		}
	}

	public void setGroup(GroupList group) {
		mGroup = group;
	}

	public int getCurGroupNum() {
		return mCurGroupNum;
	}

	public void setCurGroupNum(int curGroupNum) {
		mCurGroupNum = curGroupNum;
	}

	public int getCurTab() {
		return currentTab;
	}

	public void setCurTab(int tab) {
		currentTab = tab;
	}

}
