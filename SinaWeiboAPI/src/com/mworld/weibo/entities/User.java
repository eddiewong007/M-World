package com.mworld.weibo.entities;



import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;


/**
 * 用户（user）对象数据结构
 * 
 * @author MengMeng
 * 
 */
public class User {
	/** 用户UID */
	public long id;
	/** 字符串型的用户UID */
	public String idstr;
	/** 用户昵称 */
	public String screen_name;
	/** 友好显示名称 */
	public String name;
	/** 用户所在省级ID */
	public int province;
	/** 用户所在城市ID */
	public int city;
	/** 用户所在地 */
	public String location;
	/** 用户个人描述 */
	public String description;
	/** 用户博客地址 */
	public String url;
	/** 用户头像地址（中图），50×50像素 */
	public String profile_image_url;
	/** 用户的微博统一URL地址 */
	public String profile_url;
	/** 用户的个性化域名 */
	public String domain;
	/** 用户的微号 */
	public String weihao;
	/** 性别，m：男、f：女、n：未知 */
	public String gender;
	/** 粉丝数 */
	public int followers_count;
	/** 关注数 */
	public int friends_count;
	/** 微博数 */
	public int statuses_count;
	/** 收藏数 */
	public int favourites_count;
	/** 用户创建（注册）时间 */
	public String created_at;
	/** 暂未支持 */
	public boolean following;
	/** 是否允许所有人给我发私信，true：是，false：否 */
	public boolean allow_all_act_msg;
	/** 是否允许标识用户的地理位置，true：是，false：否 */
	public boolean geo_enabled;
	/** 是否是微博认证用户，即加V用户，true：是，false：否 */
	public boolean verified;
	/** 暂未支持 */
	public int verified_type;
	/** 用户备注信息，只有在查询用户关系时才返回此字段 */
	public String remark;
	/** 用户的最近一条微博信息字段 详细 */
	//public Status status;
	/** 是否允许所有人对我的微博进行评论，true：是，false：否 */
	public boolean allow_all_comment;
	/** 用户头像地址（大图），180×180像素 */
	public String avatar_large;
	/** 用户头像地址（高清），高清头像原图 */
	public String avatar_hd;
	/** 认证原因 */
	public String verified_reason;
	/** 该用户是否关注当前登录用户，true：是，false：否 */
	public boolean follow_me;
	/** 用户的在线状态，0：不在线、1：在线 */
	public int online_status;
	/** 用户的互粉数 */
	public int bi_followers_count;
	/** 用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语 */
	public String lang;
	
	public static User parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
		
        try {
            return User.parse(JSON.parseObject(jsonString));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static User parse(JSONObject jsonObject) {
    	if (null == jsonObject) {
            return null;
        }
        
        User user = new User();
        user.id                 = jsonObject.getLongValue("id");
        user.idstr              = jsonObject.getString("idstr", "");
        user.screen_name        = jsonObject.getString("screen_name", "");
        user.name               = jsonObject.getString("name", "");
        user.province           = jsonObject.getIntValue("province", -1);
        user.city               = jsonObject.getIntValue("city", -1);
        user.location           = jsonObject.getString("location", "");
        user.description        = jsonObject.getString("description", "");
        user.url                = jsonObject.getString("url", "");
        user.profile_image_url  = jsonObject.getString("profile_image_url", "");
        user.profile_url        = jsonObject.getString("profile_url", "");
        user.domain             = jsonObject.getString("domain", "");
        user.weihao             = jsonObject.getString("weihao", "");
        user.gender             = jsonObject.getString("gender", "");
        user.followers_count    = jsonObject.getIntValue("followers_count", 0);
        user.friends_count      = jsonObject.getIntValue("friends_count", 0);
        user.statuses_count     = jsonObject.getIntValue("statuses_count", 0);
        user.favourites_count   = jsonObject.getIntValue("favourites_count", 0);
        user.created_at         = jsonObject.getString("created_at", "");
        user.following          = jsonObject.getBooleanValue("following", false);
        user.allow_all_act_msg  = jsonObject.getBooleanValue("allow_all_act_msg", false);
        user.geo_enabled        = jsonObject.getBooleanValue("geo_enabled", false);
        user.verified           = jsonObject.getBooleanValue("verified", false);
        user.verified_type      = jsonObject.getIntValue("verified_type", -1);
        user.remark             = jsonObject.getString("remark", "");
       // user.status             = Status.parse(jsonObject.getJSONObject("status")); // XXX: NO Need ?
        user.allow_all_comment  = jsonObject.getBooleanValue("allow_all_comment", true);
        user.avatar_large       = jsonObject.getString("avatar_large", "");
        user.avatar_hd          = jsonObject.getString("avatar_hd", "");
        user.verified_reason    = jsonObject.getString("verified_reason", "");
        user.follow_me          = jsonObject.getBooleanValue("follow_me", false);
        user.online_status      = jsonObject.getIntValue("online_status", 0);
        user.bi_followers_count = jsonObject.getIntValue("bi_followers_count", 0);
        user.lang               = jsonObject.getString("lang", "");
        
        return user;
    
	}

}
