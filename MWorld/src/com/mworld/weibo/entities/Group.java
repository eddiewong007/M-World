package com.mworld.weibo.entities;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mworld.weibo.entities.User;

/**
 * 好友分组信息。
 * 
 */
public class Group {
	public static final String GROUP_ID_ALL = "1";

	/** 微博分组ID **/
	public String id;
	/** 微博分组字符串ID **/
	public String idStr;
	/** 分组名称 **/
	public String name;
	/** 类型（不公开分组等） **/
	public String mode;
	/** 是否公开 **/
	public int visible;
	/** 喜欢数 **/
	public int like_count;
	/** 分组成员数 **/
	public int member_count;
	/** 分组描述 **/
	public String description;
	/** 分组的Tag 信息 **/
	public ArrayList<Tag> tags;
	/** 头像信息 **/
	public String profile_image_url;
	/** 分组所属用户信息 **/
	public User user;
	/** 分组创建时间 **/
	public String createAtTime;

	public static Group parse(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}

		Group group = new Group();
		group.user = User.parse(jsonObject.getJSONObject("user"));
		group.id = jsonObject.getString("id");
		group.idStr = jsonObject.getString("idstr");
		group.name = jsonObject.getString("name");
		group.mode = jsonObject.getString("mode");
		group.visible = jsonObject.getIntValue("visible");
		group.like_count = jsonObject.getIntValue("like_count");
		group.member_count = jsonObject.getIntValue("member_count");
		group.description = jsonObject.getString("description");
		group.profile_image_url = jsonObject.getString("profile_image_url");
		group.createAtTime = jsonObject.getString("create_time", "");

		JSONArray jsonArray = jsonObject.getJSONArray("tags");
		if (jsonArray != null && jsonObject.size() > 0) {
			int length = jsonArray.size();
			group.tags = new ArrayList<Tag>(length);
			for (int ix = 0; ix < length; ix++) {
				group.tags.add(Tag.parse(jsonArray.getJSONObject(ix)));
			}
		}

		return group;
	}
}
