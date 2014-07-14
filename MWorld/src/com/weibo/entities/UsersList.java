package com.weibo.entities;

import java.util.ArrayList;

import android.text.TextUtils;

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
	/** 微博列表 */
	public ArrayList<User> usersList;
	public boolean hasvisible;
	public String previous_cursor;
	public String next_cursor;
	public int total_number;

	public static UsersList parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		UsersList users = new UsersList();
		try {
			JSONObject jsonObject = JSON.parseObject(jsonString);
			users.hasvisible = jsonObject.getBooleanValue("hasvisible", false);
			users.previous_cursor = jsonObject
					.getString("previous_cursor", "0");
			users.next_cursor = jsonObject.getString("next_cursor", "0");
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
			e.printStackTrace();
		}

		return users;
	}
}
