package com.weibo.entities;

import java.util.ArrayList;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 用户列表结构体
 * 
 * @author MengMeng
 * 
 */
public class UsersList {
	private static final String TAG = "UserList";

	/** 微博列表 */
	public ArrayList<User> usersList;
	/** 上一页返回结果的游标， */
	public int previous_cursor;
	/** 下一页返回结果的游标， */
	public int next_cursor;
	/** 用户总数 */
	public int total_number;

	/**
	 * 将json字符串解析成UsersList对象
	 * 
	 * @param jsonString
	 *            待解析的json字符串
	 * @return 解析出来的UsersList对象
	 */
	public static UsersList parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		UsersList users = new UsersList();
		try {
			JSONObject jsonObject = JSON.parseObject(jsonString);
			users.previous_cursor = jsonObject
					.getIntValue("previous_cursor", 0);
			users.next_cursor = jsonObject.getIntValue("next_cursor", 0);
			users.total_number = jsonObject.getIntValue("total_number", 0);

			JSONArray jsonArray = jsonObject.getJSONArray("users");
			if (jsonArray != null && !jsonArray.isEmpty()) {
				int size = jsonArray.size();
				users.usersList = new ArrayList<User>(size);
				for (int ix = 0; ix < size; ix++) {
					users.usersList
							.add(User.parse(jsonArray.getJSONObject(ix)));
				}
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}

		return users;
	}
}
