package com.mworld.weibo.entities;

import java.io.Serializable;
import java.util.ArrayList;

import android.text.TextUtils;
import android.util.Log;

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
public class StatusList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3026584305125536342L;

	private static final String TAG = "StatusesList";

	/** 微博列表 */
	public ArrayList<Status> statuses;
	/** 暂时不支持 */
	public long previous_cursor;
	/** 暂时不支持 */
	public long next_cursor;
	/** 微博总数 */
	public int total_number;

	/**
	 * 将json字符串解析成StatusesList对象
	 * 
	 * @param jsonString
	 *            待解析的json字符串
	 * @return 解析出来的StatusesList对象
	 */
	public static StatusList parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		StatusList statuses = new StatusList();
		try {
			JSONObject jsonObject = JSON.parseObject(jsonString);
			statuses.previous_cursor = jsonObject.getLongValue(
					"previous_cursor", 0);// 暂未支持
			statuses.next_cursor = jsonObject.getLongValue("next_cursor", 0);// 暂未支持
			statuses.total_number = jsonObject.getIntValue("total_number", 0);

			JSONArray jsonArray = jsonObject.getJSONArray("statuses");
			if (jsonArray != null && !jsonArray.isEmpty()) {
				int size = jsonArray.size();
				statuses.statuses = new ArrayList<Status>(size);
				for (int ix = 0; ix < size; ix++) {
					statuses.statuses.add(Status.parse(jsonArray
							.getJSONObject(ix)));
				}
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}

		return statuses;
	}

}