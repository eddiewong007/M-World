package com.weibo.entities;

import java.util.ArrayList;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 评论列表结构体
 * 
 * @author MengMeng
 * 
 */
public class CommentsList {
	private static final String TAG = "CommentsList";

	/** 评论列表 */
	public ArrayList<Comment> commentsList;
	/** 暂不支持 */
	public long previous_cursor;
	/** 暂不支持 */
	public long next_cursor;
	/** 评论总数 */
	public int total_number;

	/**
	 * 将json字符串解析成CommentsList对象
	 * 
	 * @param jsonString
	 *            待解析的json字符串
	 * @return 解析出来的CommentsList对象
	 */
	public static CommentsList parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		CommentsList comments = new CommentsList();
		try {
			JSONObject jsonObject = JSON.parseObject(jsonString);
			comments.previous_cursor = jsonObject.getLongValue(
					"previous_cursor", 0); // 暂未支持
			comments.next_cursor = jsonObject.getLongValue("next_cursor", 0);// 暂未支持
			comments.total_number = jsonObject.getIntValue("total_number", 0);

			JSONArray jsonArray = jsonObject.getJSONArray("comments");
			comments.commentsList = new ArrayList<Comment>();
			if (jsonArray != null && !jsonArray.isEmpty()) {
				int size = jsonArray.size();
				for (int i = 0; i < size; i++) {
					comments.commentsList.add(Comment.parse(jsonArray
							.getJSONObject(i)));
				}
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}

		return comments;
	}
}
