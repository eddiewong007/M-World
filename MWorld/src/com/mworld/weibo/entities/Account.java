package com.mworld.weibo.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class Account implements Parcelable {

	private static final String TAG = Account.class.getName();

	private int id;
	private String uid;
	private String accessToken;
	private long expiresIn;
	private String jsonUserInfo;
	private User userInfo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String id) {
		this.uid = id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getJsonUserInfo() {
		return jsonUserInfo;
	}

	public void setJsonUserInfo(String jsonUserInfo) {
		this.jsonUserInfo = jsonUserInfo;
	}

	public User getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}

	public String getAvatarUrl() {
		if (userInfo != null
				&& !TextUtils.isEmpty(userInfo.getProfileImageUrl()))
			return userInfo.getProfileImageUrl();
		return null;
	}

	public static Account parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return Account.parse(JSON.parseObject(jsonString));
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}

		return null;
	}

	public static Account parse(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}
		Account account = new Account();
		account.uid = jsonObject.getString("uid", "0");
		account.accessToken = jsonObject.getString("access_token", "");

		String expires_in = jsonObject.getString("expires_in", "0");
		account.expiresIn = System.currentTimeMillis()
				+ Long.parseLong(expires_in) * 1000;

		return account;

	}

	public Account() {

	}

	private Account(Parcel in) {
		uid = in.readString();
		accessToken = in.readString();
		expiresIn = in.readLong();
		userInfo = in.readParcelable(User.class.getClassLoader());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(uid);
		dest.writeString(accessToken);
		dest.writeLong(expiresIn);
		dest.writeParcelable(userInfo, flags);
	}

	public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
		public Account createFromParcel(Parcel in) {
			return new Account(in);
		}

		public Account[] newArray(int size) {
			return new Account[size];
		}
	};

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Account) {
			return ((Account) obj).getUid() == getUid();
		}
		return false;

	}

}
