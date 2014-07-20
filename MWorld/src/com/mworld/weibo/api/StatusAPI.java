package com.mworld.weibo.api;

import java.io.File;
import java.io.FileNotFoundException;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * 该类封装了微博接口 详情请参考
 * 
 * @author MengMeng
 * 
 */
public class StatusAPI extends BaseAPI {

	/** 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐 */
	public static final int FEATURE_ALL = 0;
	public static final int FEATURE_ORIGINAL = 1;
	public static final int FEATURE_PICTURE = 2;
	public static final int FEATURE_VIDEO = 3;
	public static final int FEATURE_MUSICE = 4;

	/** 作者筛选类型，0：全部、1：我关注的人、2：陌生人 */
	public static final int AUTHOR_FILTER_ALL = 0;
	public static final int AUTHOR_FILTER_ATTENTIONS = 1;
	public static final int AUTHOR_FILTER_STRANGER = 2;

	/** 来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论 */
	public static final int SRC_FILTER_ALL = 0;
	public static final int SRC_FILTER_WEIBO = 1;
	public static final int SRC_FILTER_WEIQUN = 2;

	/** 原创筛选类型，0：全部微博、1：原创的微博。 */
	public static final int TYPE_FILTER_ALL = 0;
	public static final int TYPE_FILTER_ORIGAL = 1;

	/** 获取类型，1：微博、2：评论、3：私信，默认为1。 */
	public static final int TYPE_STATUSES = 1;
	public static final int TYPE_COMMENTS = 2;
	public static final int TYPE_MESSAGE = 3;

	/** 标识是否在转发的同时发表评论，0：否、1：评论给当前微博、2：评论给原微博、3：都评论，默认为0 */
	public static final int COMMENTS_NONE = 0;
	public static final int COMMENTS_CUR_STATUSES = 1;
	public static final int COMMENTS_RIGAL_STATUSES = 2;
	public static final int COMMENTS_BOTH = 3;

	/** 表情类别，face：普通表情、ani：魔法表情、cartoon：动漫表情，默认为face。 */
	public static final String EMOTION_TYPE_FACE = "face";
	public static final String EMOTION_TYPE_ANI = "ani";
	public static final String EMOTION_TYPE_CARTOON = "cartoon";

	/** 语言类别，cnname：简体、twname：繁体，默认为cnname。 */
	public static final String LANGUAGE_CNNAME = "cnname";
	public static final String LANGUAGE_TWNAME = "twname";

	private static final String API_BASE_URL = API_SERVER + "/statuses";

	/**
	 * 构造函数，使用各个 API 接口提供的服务前必须先获取 Token。
	 * 
	 * @param accesssToken
	 *            访问令牌
	 */
	public StatusAPI(String accessToken) {
		super(accessToken);
	}

	/**
	 * 返回最新的200条公共微博，返回结果非完全实时
	 * 
	 * @param count
	 *            单页返回的记录条数，最大不超过200，默认为20
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void publicTimeline(int count, AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		params.put("count", String.valueOf(count));
		requestAsync(API_BASE_URL + "/public_timeline.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 获取当前登录用户及其所关注用户的最新微博。
	 * 
	 * @param since_id
	 *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id
	 *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param base_app
	 *            是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
	 * @param feature
	 *            过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。 <li>
	 *            {@link #FEATURE_ALL} <li> {@link #FEATURE_ORIGINAL} <li>
	 *            {@link #FEATURE_PICTURE} <li> {@link #FEATURE_VIDEO} <li>
	 *            {@link #FEATURE_MUSICE}
	 * @param trim_user
	 *            返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void friendsTimeline(long since_id, long max_id, int count,
			int page, boolean base_app, int feature, boolean trim_user,
			AjaxCallBack<String> callBack) {
		AjaxParams params = buildTimeLineWithAppTrim(since_id, max_id, count,
				page, base_app, trim_user, feature);
		requestAsync(API_BASE_URL + "/friends_timeline.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 获取当前登录用户及其所关注用户的最新微博。
	 * 
	 * @param since_id
	 *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id
	 *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param base_app
	 *            是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
	 * @param feature
	 *            过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0 <li> {@link #FEATURE_ALL}
	 *            <li> {@link #FEATURE_ORIGINAL} <li> {@link #FEATURE_PICTURE} <li>
	 *            {@link #FEATURE_VIDEO} <li> {@link #FEATURE_MUSICE}
	 * @param trim_user
	 *            返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void homeTimeline(long since_id, long max_id, int count, int page,
			boolean base_app, int feature, boolean trim_user,
			AjaxCallBack<String> callBack) {
		AjaxParams params = buildTimeLineWithAppTrim(since_id, max_id, count,
				page, base_app, trim_user, feature);
		requestAsync(API_BASE_URL + "/home_timeline.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 获取当前登录用户及其所关注用户的最新微博的ID。
	 * 
	 * @param since_id
	 *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id
	 *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param base_app
	 *            是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
	 * @param feature
	 *            过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0 <li> {@link #FEATURE_ALL}
	 *            <li> {@link #FEATURE_ORIGINAL} <li> {@link #FEATURE_PICTURE} <li>
	 *            {@link #FEATURE_VIDEO} <li> {@link #FEATURE_MUSICE}
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void friendsTimelineIds(long since_id, long max_id, int count,
			int page, boolean base_app, int feature,
			AjaxCallBack<String> callBack) {
		AjaxParams params = buildTimeLineWithApp(since_id, max_id, count, page,
				base_app, feature);
		requestAsync(API_BASE_URL + "/friends_timeline/ids.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 通过用户ID获取某个用户最新发表的微博列表。
	 * 
	 * @param uid
	 *            需要查询的用户ID
	 * @param since_id
	 *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id
	 *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param base_app
	 *            是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
	 * @param feature
	 *            过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0 <li> {@link #FEATURE_ALL}
	 *            <li> {@link #FEATURE_ORIGINAL} <li> {@link #FEATURE_PICTURE} <li>
	 *            {@link #FEATURE_VIDEO} <li> {@link #FEATURE_MUSICE}
	 * @param trim_user
	 *            返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void userTimeline(long uid, long since_id, long max_id, int count,
			int page, boolean base_app, int feature, boolean trim_user,
			AjaxCallBack<String> callBack) {
		AjaxParams params = buildTimeLineWithAppTrim(since_id, max_id, count,
				page, base_app, trim_user, feature);
		params.put("uid", String.valueOf(uid));
		requestAsync(API_BASE_URL + "/user_timeline.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 通过用户昵称获取某个用户最新发表的微博列表。
	 * 
	 * @param screen_name
	 *            需要查询的用户昵称
	 * @param since_id
	 *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id
	 *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param base_app
	 *            是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
	 * @param feature
	 *            过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0 <li> {@link #FEATURE_ALL}
	 *            <li> {@link #FEATURE_ORIGINAL} <li> {@link #FEATURE_PICTURE} <li>
	 *            {@link #FEATURE_VIDEO} <li> {@link #FEATURE_MUSICE}
	 * @param trim_user
	 *            返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void userTimeline(String screen_name, long since_id, long max_id,
			int count, int page, boolean base_app, int feature,
			boolean trim_user, AjaxCallBack<String> callBack) {
		AjaxParams params = buildTimeLineWithAppTrim(since_id, max_id, count,
				page, base_app, trim_user, feature);
		params.put("screen_name", screen_name);
		requestAsync(API_BASE_URL + "/user_timeline.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 通过用户ID获取用户发布的微博的ID
	 * 
	 * @param uid
	 *            需要查询的用户ID
	 * @param since_id
	 *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id
	 *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param base_app
	 *            是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
	 * @param feature
	 *            过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0 <li> {@link #FEATURE_ALL}
	 *            <li> {@link #FEATURE_ORIGINAL} <li> {@link #FEATURE_PICTURE} <li>
	 *            {@link #FEATURE_VIDEO} <li> {@link #FEATURE_MUSICE}
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void userTimelineIds(long uid, long since_id, long max_id,
			int count, int page, boolean base_app, int feature,
			AjaxCallBack<String> callBack) {
		AjaxParams params = buildTimeLineWithApp(since_id, max_id, count, page,
				base_app, feature);
		params.put("uid", String.valueOf(uid));
		requestAsync(API_BASE_URL + "/user_timeline/ids.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 通过用户昵称获取用户发布的微博的ID
	 * 
	 * @param screen_name
	 *            需要查询的用户昵称
	 * @param since_id
	 *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id
	 *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param base_app
	 *            是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
	 * @param feature
	 *            过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0 <li> {@link #FEATURE_ALL}
	 *            <li> {@link #FEATURE_ORIGINAL} <li> {@link #FEATURE_PICTURE} <li>
	 *            {@link #FEATURE_VIDEO} <li> {@link #FEATURE_MUSICE}
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void userTimelineIds(String screen_name, long since_id, long max_id,
			int count, int page, boolean base_app, int feature,
			AjaxCallBack<String> callBack) {
		AjaxParams params = buildTimeLineWithApp(since_id, max_id, count, page,
				base_app, feature);
		params.put("screen_name", screen_name);
		requestAsync(API_BASE_URL + "/user_timeline/ids.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 通过多个用户ID批量获取指定的一批用户的微博列表
	 * 
	 * @param uids
	 *            需要查询的用户ID数组，（用半角逗号分隔，一次最多20个）
	 * @param count
	 *            单页返回的记录条数，默认为20
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param base_app
	 *            是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
	 * @param feature过滤类型ID
	 *            ，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0 <li> {@link #FEATURE_ALL} <li>
	 *            {@link #FEATURE_ORIGINAL} <li> {@link #FEATURE_PICTURE} <li>
	 *            {@link #FEATURE_VIDEO} <li> {@link #FEATURE_MUSICE}
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void timelineBatchBy(long[] uids, int count, int page,
			boolean base_app, int feature, AjaxCallBack<String> callBack) {
		AjaxParams params = buildHotParams(count, base_app);
		StringBuilder strb = new StringBuilder();
		for (long id : uids) {
			strb.append(id).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.put("uids", strb.toString());
		params.put("page", String.valueOf(page));
		params.put("count", String.valueOf(count));
		params.put("feature", String.valueOf(feature));
		requestAsync(API_BASE_URL + "/timeline_batch.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 通过多个用户昵称批量获取指定的一批用户的微博列表
	 * 
	 * @param screen_names
	 *            需要查询的用户昵称，用半角逗号分隔，一次最多20个
	 * @param count
	 *            单页返回的记录条数，默认为20
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param base_app
	 *            是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
	 * @param feature过滤类型ID
	 *            ，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0 <li> {@link #FEATURE_ALL} <li>
	 *            {@link #FEATURE_ORIGINAL} <li> {@link #FEATURE_PICTURE} <li>
	 *            {@link #FEATURE_VIDEO} <li> {@link #FEATURE_MUSICE}
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void timelineBatch(String[] screen_names, int count, int page,
			boolean base_app, int feature, AjaxCallBack<String> callBack) {
		AjaxParams params = buildHotParams(count, base_app);
		StringBuilder strb = new StringBuilder();
		for (String screen_name : screen_names) {
			strb.append(screen_name).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.put("screen_names", strb.toString());
		params.put("page", String.valueOf(page));
		params.put("count", String.valueOf(count));
		params.put("feature", String.valueOf(feature));
		requestAsync(API_BASE_URL + "/timeline_batch.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 获取指定微博的转发微博列表
	 * 
	 * @param id
	 *            需要查询的微博ID。
	 * @param since_id
	 *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id
	 *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param filter_by_author
	 *            作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。可为以下几种： <li>
	 *            {@link #AUTHOR_FILTER_ALL} <li>
	 *            {@link #AUTHOR_FILTER_ATTENTIONS} <li>
	 *            {@link #AUTHOR_FILTER_STRANGER}
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void repostTimeline(long id, long since_id, long max_id, int count,
			int page, int filter_by_author, AjaxCallBack<String> callBack) {
		AjaxParams params = buildTimeLineBase(since_id, max_id, count, page);
		params.put("id", String.valueOf(id));
		params.put("filter_by_author", String.valueOf(filter_by_author));
		requestAsync(API_BASE_URL + "/repost_timeline.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 获取一条原创微博的最新转发微博的ID。
	 * 
	 * @param id
	 *            需要查询的微博ID
	 * @param since_id
	 *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id
	 *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param filter_by_author
	 *            作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。可为以下几种： <li>
	 *            {@link #AUTHOR_FILTER_ALL} <li>
	 *            {@link #AUTHOR_FILTER_ATTENTIONS} <li>
	 *            {@link #AUTHOR_FILTER_STRANGER}
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void repostTimelineIds(long id, long since_id, long max_id,
			int count, int page, int filter_by_author,
			AjaxCallBack<String> callBack) {
		AjaxParams params = buildTimeLineBase(since_id, max_id, count, page);
		params.put("id", String.valueOf(id));
		params.put("filter_by_author", String.valueOf(filter_by_author));
		requestAsync(API_BASE_URL + "/repost_timeline/ids.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 获取最新的提到登录用户的微博列表，即@我的微博。
	 * 
	 * @param since_id
	 *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id
	 *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param filter_by_author
	 *            作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。可为以下几种： <li>
	 *            {@link #AUTHOR_FILTER_ALL} <li>
	 *            {@link #AUTHOR_FILTER_ATTENTIONS} <li>
	 *            {@link #AUTHOR_FILTER_STRANGER}
	 * @param filter_by_source
	 *            来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论。可分为以下几种： <li>
	 *            {@link #SRC_FILTER_ALL} <li> {@link #SRC_FILTER_WEIBO} <li>
	 *            {@link #SRC_FILTER_WEIQUN}
	 * @param filter_by_type
	 *            原创筛选类型，0：全部微博、1：原创的微博，默认为0。可分为以下几种： <li>
	 *            {@link #TYPE_FILTER_ALL} <li> {@link #TYPE_FILTER_ORIGAL}
	 * @param trim_user
	 *            返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void mentions(long since_id, long max_id, int count, int page,
			int filter_by_author, int filter_by_source, int filter_by_type,
			boolean trim_user, AjaxCallBack<String> callBack) {
		AjaxParams params = buildTimeLineBase(since_id, max_id, count, page);
		params.put("filter_by_author", String.valueOf(filter_by_author));
		params.put("filter_by_source", String.valueOf(filter_by_source));
		params.put("filter_by_type", String.valueOf(filter_by_type));
		params.put("trim_user", String.valueOf(trim_user ? 1 : 0));
		requestAsync(API_BASE_URL + "/mentions.json", params, HTTPMETHOD_GET,
				callBack);
	}

	/**
	 * 获取@当前用户的最新微博的ID。
	 * 
	 * @param since_id
	 *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id
	 *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param filter_by_author
	 *            作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。可为以下几种： <li>
	 *            {@link #AUTHOR_FILTER_ALL} <li>
	 *            {@link #AUTHOR_FILTER_ATTENTIONS} <li>
	 *            {@link #AUTHOR_FILTER_STRANGER}
	 * @param filter_by_source
	 *            来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论。可分为以下几种： <li>
	 *            {@link #SRC_FILTER_ALL} <li> {@link #SRC_FILTER_WEIBO} <li>
	 *            {@link #SRC_FILTER_WEIQUN}
	 * @param filter_by_type
	 *            原创筛选类型，0：全部微博、1：原创的微博，默认为0。可分为以下几种： <li>
	 *            {@link #TYPE_FILTER_ALL} <li> {@link #TYPE_FILTER_ORIGAL}
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void mentionsIds(long since_id, long max_id, int count, int page,
			int filter_by_author, int filter_by_source, int filter_by_type,
			AjaxCallBack<String> callBack) {
		AjaxParams params = buildTimeLineBase(since_id, max_id, count, page);
		params.put("filter_by_author", String.valueOf(filter_by_author));
		params.put("filter_by_source", String.valueOf(filter_by_source));
		params.put("filter_by_type", String.valueOf(filter_by_type));
		requestAsync(API_BASE_URL + "/mentions/ids.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 获取双向关注用户的最新微博。
	 * 
	 * @param since_id
	 *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id
	 *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param base_app
	 *            是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
	 * @param feature
	 *            过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0 <li> {@link #FEATURE_ALL}
	 *            <li> {@link #FEATURE_ORIGINAL} <li> {@link #FEATURE_PICTURE} <li>
	 *            {@link #FEATURE_VIDEO} <li> {@link #FEATURE_MUSICE}
	 * @param trim_user
	 *            返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void bilateralTimeline(long since_id, long max_id, int count,
			int page, boolean base_app, int feature, boolean trim_user,
			AjaxCallBack<String> callBack) {
		AjaxParams params = buildTimeLineWithAppTrim(since_id, max_id, count,
				page, base_app, trim_user, feature);
		requestAsync(API_BASE_URL + "/bilateral_timeline.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 根据微博ID获取单条微博内容。
	 * 
	 * @param id
	 *            需要获取的微博ID
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void show(long id, AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		requestAsync(API_BASE_URL + "/show.json", params, HTTPMETHOD_GET,
				callBack);
	}

	/**
	 * 根据微博ID获取单条微博内容。
	 * 
	 * @param ids
	 *            需要查询的微博ID数组（用半角逗号分隔，最多不超过50个）
	 * @param trim_user
	 *            返回值中user字段开关，0：返回完整user字段、1：user字段仅返回user_id，默认为0。
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void showBatch(long[] ids, boolean trim_user,
			AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		StringBuilder strb = new StringBuilder();
		for (long id : ids) {
			strb.append(id).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.put("ids", strb.toString());
		params.put("trim_user", String.valueOf(trim_user ? 1 : 0));
		requestAsync(API_BASE_URL + "/show_batch.json", params, HTTPMETHOD_GET,
				callBack);
	}

	/**
	 * 通过微博（评论、私信）ID获取其MID。
	 * 
	 * @param ids
	 *            需要查询的微博（评论、私信）ID，最多不超过20个。
	 * @param type
	 *            获取类型，1：微博、2：评论、3：私信，默认为1。可为几下几种： <li> {@link #TYPE_STATUSES}
	 *            <li> {@link #TYPE_COMMENTS} <li> {@link #TYPE_MESSAGE}
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void queryMID(long[] ids, int type, AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		if (1 == ids.length) {
			params.put("id", String.valueOf(ids[0]));
		} else {
			params.put("is_batch", String.valueOf(1));
			StringBuilder strb = new StringBuilder();
			for (long id : ids) {
				strb.append(id).append(",");
			}
			strb.deleteCharAt(strb.length() - 1);
			params.put("id", strb.toString());
		}
		params.put("type", String.valueOf(type));
		requestAsync(API_BASE_URL + "/querymid.json", params, HTTPMETHOD_GET,
				callBack);
	}

	/**
	 * 通过微博（评论、私信）MID获取其ID,形如“3z4efAo4lk”的MID即为经过base62转换的MID。
	 * 
	 * @param mids
	 *            需要查询的微博（评论、私信）MID，最多不超过20个
	 * @param type
	 *            获取类型，1：微博、2：评论、3：私信，默认为1。可为几下几种： <li> {@link #TYPE_STATUSES}
	 *            <li> {@link #TYPE_COMMENTS} <li> {@link #TYPE_MESSAGE}
	 * @param inbox
	 *            仅对私信有效，当MID类型为私信时用此参数，0：发件箱、1：收件箱，默认为0
	 * @param isBase62
	 *            MID是否是base62编码，0：否、1：是，默认为0
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void queryID(String[] mids, int type, boolean inbox,
			boolean isBase62, AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		if (mids != null) {
			if (1 == mids.length) {
				params.put("mid", mids[0]);
			} else {
				params.put("is_batch", String.valueOf(1));
				StringBuilder strb = new StringBuilder();
				for (String mid : mids) {
					strb.append(mid).append(",");
				}
				strb.deleteCharAt(strb.length() - 1);
				params.put("mid", strb.toString());
			}
		}

		params.put("type", String.valueOf(type));
		params.put("inbox", String.valueOf(inbox ? 1 : 0));
		params.put("isBase62", String.valueOf(isBase62 ? 1 : 0));
		requestAsync(API_BASE_URL + "/queryid.json", params, HTTPMETHOD_GET,
				callBack);
	}

	/**
	 * 批量获取指定微博的转发数评论数。
	 * 
	 * @param ids
	 *            需要获取数据的微博ID，最多不超过100个
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void count(long[] ids, AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		StringBuilder strb = new StringBuilder();
		for (long id : ids) {
			strb.append(id).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.put("ids", strb.toString());
		requestAsync(API_BASE_URL + "/count.json", params, HTTPMETHOD_GET,
				callBack);
	}

	/**
	 * 获取当前登录用户关注的人发给其的定向微博
	 * 
	 * @param since_id
	 *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id
	 *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param trim_user
	 *            返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void toMe(long since_id, long max_id, int count, int page,
			boolean trim_user, AjaxCallBack<String> callBack) {
		AjaxParams params = buildTimeLineBase(since_id, max_id, count, page);
		params.put("trim_user", String.valueOf(trim_user ? 1 : 0));
		requestAsync(API_BASE_URL + "/to_me.json", params, HTTPMETHOD_GET,
				callBack);
	}

	/**
	 * 获取当前登录用户关注的人发给其的定向微博ID列表
	 * 
	 * @param since_id
	 *            若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id
	 *            若指定此参数，则返回ID小于或等于max_id的微博，默认为0
	 * @param count
	 *            返回结果的条数数量，最大不超过200，默认为20
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void toMeIds(long since_id, long max_id, int count, int page,
			AjaxCallBack<String> callBack) {
		AjaxParams params = buildTimeLineBase(since_id, max_id, count, page);
		requestAsync(API_BASE_URL + "/to_me/ids.json", params, HTTPMETHOD_GET,
				callBack);
	}

	/**
	 * 根据ID跳转到单条微博页
	 * 
	 * @param uid
	 *            需要跳转的用户ID
	 * @param id
	 *            需要跳转的微博ID
	 * @param activity
	 *            当前界面
	 */
	public void go(long uid, long id, Activity activity) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(API_BASE_URL + "/to_me/go"));
		activity.startActivity(intent);
	}

	/**
	 * 获取微博官方表情的详细信息。
	 * 
	 * @param type
	 *            表情类别，表情类别，face：普通表情、ani：魔法表情、cartoon：动漫表情，默认为face。可为以下几种： <li>
	 *            {@link #EMOTION_TYPE_FACE} <li> {@link #EMOTION_TYPE_ANI} <li>
	 *            {@link #EMOTION_TYPE_CARTOON}
	 * @param language
	 *            语言类别，cnname：、twname：，默认为cnname。 <li> {@link #LANGUAGE_CNNAME}
	 *            <li> {@link #LANGUAGE_TWNAME}
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void emotions(String type, String language,
			AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		params.put("type", type);
		params.put("language", language);
		requestAsync(API_SERVER + "/emotions.json", params, HTTPMETHOD_GET,
				callBack);
	}

	/**
	 * 转发一条微博。
	 * 
	 * @param id
	 *            要转发的微博ID
	 * @param status
	 *            添加的转发文本，必须做URLencode，内容不超过140个汉字，不填则默认为“转发微博”
	 * @param is_comment
	 *            是否在转发的同时发表评论，0：否、1：评论给当前微博、2：评论给原微博、3：都评论，默认为0 <li>
	 *            {@link #COMMENTS_NONE} <li> {@link #COMMENTS_CUR_STATUSES} <li>
	 *            {@link #COMMENTS_RIGAL_STATUSES} <li> {@link #COMMENTS_BOTH}
	 * @param rip
	 *            开发者上报的操作用户真实IP，形如：211.156.0.1。
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void repost(long id, String status, int is_comment, String rip,
			AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		params.put("status", String.valueOf(status));
		params.put("is_comment", String.valueOf(is_comment));
		params.put("rip", rip);
		requestAsync(API_BASE_URL + "/repost.json", params, HTTPMETHOD_POST,
				callBack);
	}

	/**
	 * 根据微博ID删除指定微博。
	 * 
	 * @param id
	 *            需要删除的微博ID
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void destroy(long id, AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		requestAsync(API_BASE_URL + "/destroy.json", params, HTTPMETHOD_POST,
				callBack);
	}

	/**
	 * 发布一条新微博
	 * 
	 * @param status
	 *            要发布的微博文本内容，必须做URLencode，内容不超过140个汉字
	 * @param visible
	 *            微博的可见性，0：所有人能看，1：仅自己可见，2：密友可见，3：指定分组可见，默认为0
	 * @param list_id
	 *            微博的保护投递指定分组ID，只有当visible参数为3时生效且必选
	 * @param lat
	 *            纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0
	 * @param lon
	 *            经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0
	 * @param annotations
	 *            元数据，主要是为了方便第三方应用记录一些适合于自己使用的信息，每条微博可以包含一个或者多个元数据，
	 *            必须以json字串的形式提交，字串长度不超过512个字符，具体内容可以自定。
	 * @param rip
	 *            开发者上报的操作用户真实IP，形如：211.156.0.1
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void update(String status, int visible, String list_id, float lat,
			float lon, String annotations, String rip,
			AjaxCallBack<String> callBack) {
		AjaxParams params = buildUpdateParams(status, lat, lon);
		params.put("visible", String.valueOf(visible));
		params.put("list_id", list_id);
		params.put("annotations", annotations);
		params.put("rip", rip);
		requestAsync(API_BASE_URL + "/update.json", params, HTTPMETHOD_POST,
				callBack);
	}

	/**
	 * 上传图片并发布一条新微博
	 * 
	 * @param status
	 *            要发布的微博文本内容，必须做URLencode，内容不超过140个汉字
	 * @param visible
	 *            微博的可见性，0：所有人能看，1：仅自己可见，2：密友可见，3：指定分组可见，默认为0
	 * @param list_id
	 *            微博的保护投递指定分组ID，只有当visible参数为3时生效且必选
	 * @param pic
	 *            要上传的图片的路径，仅支持JPEG、GIF、PNG格式，图片大小小于5M
	 * @param lat
	 *            纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0
	 * @param lon
	 *            经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0
	 * @param annotations
	 *            元数据，主要是为了方便第三方应用记录一些适合于自己使用的信息，每条微博可以包含一个或者多个元数据，
	 *            必须以json字串的形式提交，字串长度不超过512个字符，具体内容可以自定。
	 * @param rip
	 *            开发者上报的操作用户真实IP，形如：211.156.0.1
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void upload(String status, int visible, String list_id, String pic,
			float lat, float lon, String annotations, String rip,
			AjaxCallBack<String> callBack) {
		AjaxParams params = buildUpdateParams(status, lat, lon);
		params.put("status", status);
		params.put("visible", String.valueOf(visible));
		params.put("list_id", list_id);
		try {
			params.put("pic", new File(pic));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		params.put("annotations", annotations);
		params.put("rip", rip);
		requestAsync(API_BASE_URL + "/upload.json", params, HTTPMETHOD_POST,
				callBack);
	}

	/**
	 * 上传图片并发布一条新微博
	 * 
	 * @param status
	 *            要发布的微博文本内容，必须做URLencode，内容不超过140个汉字
	 * @param visible
	 *            微博的可见性，0：所有人能看，1：仅自己可见，2：密友可见，3：指定分组可见，默认为0
	 * @param list_id
	 *            微博的保护投递指定分组ID，只有当visible参数为3时生效且必选
	 * @param url
	 *            图片的URL地址，必须以http开头
	 * @param pic_id
	 *            已经上传的图片pid，多个时使用英文半角逗号符分隔，最多不超过九张。 imageUrl 和
	 *            pic_id必选一个，两个参数都存在时，取picid参数的值为准
	 * @param lat
	 *            纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0
	 * @param lon
	 *            经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0
	 * @param annotations
	 *            元数据，主要是为了方便第三方应用记录一些适合于自己使用的信息，每条微博可以包含一个或者多个元数据，
	 *            必须以json字串的形式提交，字串长度不超过512个字符，具体内容可以自定。
	 * @param rip
	 *            开发者上报的操作用户真实IP，形如：211.156.0.1
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void uploadUrlText(String status, int visible, String list_id,
			String url, String pic_id, float lat, float lon,
			String annotations, String rip, AjaxCallBack<String> callBack) {
		AjaxParams params = buildUpdateParams(status, lat, lon);
		params.put("visible", String.valueOf(visible));
		params.put("list_id", list_id);
		params.put("url", url);
		params.put("pic_id", pic_id);
		params.put("annotations", annotations);
		params.put("rip", rip);
		requestAsync(API_BASE_URL + "/upload_url_text.json", params,
				HTTPMETHOD_POST, callBack);
	}

	/**
	 * 屏蔽某条微博
	 * 
	 * @param id
	 *            需要屏蔽的微博ID
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void filterCreate(long id, AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		requestAsync(API_BASE_URL + "/filter/create.json", params,
				HTTPMETHOD_POST, callBack);
	}

	/**
	 * 
	 * @param id
	 *            需要屏蔽的@提到我的微博ID。此ID必须在statuses/mentions列表中
	 * @param follow_up
	 *            是否仅屏蔽当前微博。0：仅屏蔽当前@提到我的微博；1：屏蔽当前@提到我的微博，以及后续对其转发而引起的@提到我的微博。默认1
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void mentionsShield(long id, boolean follow_up,
			AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		params.put("follow_up", String.valueOf(follow_up ? 0 : 1));
		requestAsync(API_BASE_URL + "mentions/shield.json", params,
				HTTPMETHOD_POST, callBack);
	}

	// 组装TimeLines的参数
	private AjaxParams buildTimeLineBase(long since_id, long max_id, int count,
			int page) {
		AjaxParams params = new AjaxParams();
		params.put("since_id", String.valueOf(since_id));
		params.put("max_id", String.valueOf(max_id));
		params.put("count", String.valueOf(count));
		params.put("page", String.valueOf(page));
		return params;
	}

	private AjaxParams buildTimeLineWithApp(long since_id, long max_id,
			int count, int page, boolean base_app, int feature) {
		AjaxParams params = buildTimeLineBase(since_id, max_id, count, page);
		params.put("feature", String.valueOf(feature));
		params.put("base_app", String.valueOf(base_app ? 1 : 0));
		return params;
	}

	private AjaxParams buildTimeLineWithAppTrim(long since_id, long max_id,
			int count, int page, boolean base_app, boolean trim_user,
			int feature) {
		AjaxParams params = buildTimeLineWithApp(since_id, max_id, count, page,
				base_app, feature);
		params.put("trim_user", String.valueOf(trim_user ? 1 : 0));
		return params;
	}

	private AjaxParams buildHotParams(int count, boolean base_app) {
		AjaxParams params = new AjaxParams();
		params.put("count", String.valueOf(count));
		params.put("base_app", String.valueOf(base_app ? 1 : 0));
		return params;
	}

	// 组装微博请求参数
	private AjaxParams buildUpdateParams(String content, float lat, float lon) {
		AjaxParams params = new AjaxParams();
		params.put("status", content);
		params.put("long", String.valueOf(lon));
		params.put("lat", String.valueOf(lat));
		return params;
	}
}