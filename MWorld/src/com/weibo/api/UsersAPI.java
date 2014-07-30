package com.weibo.api;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.weibo.entities.AccessToken;

/**
 * 该类封装了用户接口。 详情请参考<a href="http://t.cn/8F1n1eF">用户接口</a>
 * 
 * @author SINA
 * @since 2014-03-03
 */
public class UsersAPI extends BaseAPI {

	private static final String API_BASE_URL = API_SERVER + "/users";

	/**
	 * 构造函数，使用各个 API 接口提供的服务前必须先获取 Token。
	 * 
	 * @param accessToken
	 *            访问令牌
	 */
	public UsersAPI(AccessToken accessToken) {
		super(accessToken);
	}

	/**
	 * 根据用户ID获取用户信息。
	 * 
	 * @param uid
	 *            需要查询的用户ID
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void show(long uid, AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		params.put("uid", String.valueOf(uid));
		requestAsync(API_BASE_URL + "/show.json", params, HTTPMETHOD_GET,
				callBack);
	}

	/**
	 * 根据用户昵称获取用户信息。
	 * 
	 * @param screen_name
	 *            需要查询的用户昵称
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void show(String screen_name, AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		params.put("screen_name", screen_name);
		requestAsync(API_BASE_URL + "/show.json", params, HTTPMETHOD_GET,
				callBack);
	}

	/**
	 * 通过个性化域名获取用户资料以及用户最新的一条微博。
	 * 
	 * @param domain
	 *            需要查询的个性化域名（请注意：是http://weibo.com/xxx后面的xxx部分）
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void domainShow(String domain, AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		params.put("domain", domain);
		requestAsync(API_BASE_URL + "/domain_show.json", params,
				HTTPMETHOD_GET, callBack);
	}

	/**
	 * 批量获取用户的粉丝数、关注数、微博数。
	 * 
	 * @param uids
	 *            需要获取数据的用户UID，多个之间用逗号分隔，最多不超过100个
	 * @param callBack
	 *            异步请求回调接口
	 */
	public void counts(long[] uids, AjaxCallBack<String> callBack) {
		AjaxParams params = buildCountsParams(uids);
		requestAsync(API_BASE_URL + "/counts.json", params, HTTPMETHOD_GET,
				callBack);
	}

	private AjaxParams buildCountsParams(long[] uids) {
		AjaxParams params = new AjaxParams();
		StringBuilder strb = new StringBuilder();
		for (long cid : uids) {
			strb.append(cid).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.put("uids", strb.toString());
		return params;
	}
}
