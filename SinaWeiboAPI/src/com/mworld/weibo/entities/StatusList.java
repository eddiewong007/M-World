package com.mworld.weibo.entities;

import java.util.ArrayList;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 微博列表结构体
 * 
 * @author MengMeng
 * 
 */
public class StatusList {
	/** 微博列表 */
	public ArrayList<Status> statusList;
	public boolean hasvisible;
	public String previous_cursor;
	public String next_cursor;
	public int total_number;

	public static StatusList parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		StatusList statuses = new StatusList();
		try {
			JSONObject jsonObject = JSON.parseObject(jsonString);
			statuses.hasvisible = jsonObject.getBooleanValue("hasvisible",
					false);
			statuses.previous_cursor = jsonObject.getString("previous_cursor",
					"0");
			statuses.next_cursor = jsonObject.getString("next_cursor", "0");
			statuses.total_number = jsonObject.getIntValue("total_number", 0);

			JSONArray jsonArray = jsonObject.getJSONArray("statuses");
			if (jsonArray != null && !jsonArray.isEmpty()) {
				int size = jsonArray.size();
				statuses.statusList = new ArrayList<Status>(size);
				for (int ix = 0; ix < size; ix++) {
					statuses.statusList.add(Status.parse(jsonArray
							.getJSONObject(ix)));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return statuses;
	}

}
