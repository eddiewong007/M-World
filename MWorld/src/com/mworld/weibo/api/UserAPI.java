package com.mworld.weibo.api;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.util.SparseArray;

/**
 * 该类封装了用户接口。
 * 
 * @author MengMeng
 */
public class UserAPI extends BaseAPI {

	private static final int READ_USER = 0;
	private static final int READ_USER_BY_DOMAIN = 1;
	private static final int READ_USER_COUNT = 2;

	private static final String API_BASE_URL = API_SERVER + "/users";

	private static final SparseArray<String> sAPIList = new SparseArray<String>();
	static {
		sAPIList.put(READ_USER, API_BASE_URL + "/show.json");
		sAPIList.put(READ_USER_BY_DOMAIN, API_BASE_URL + "/domain_show.json");
		sAPIList.put(READ_USER_COUNT, API_BASE_URL + "/counts.json");
	}

	public UserAPI(String accessToken) {
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
		requestAsync(sAPIList.get(READ_USER), params, HTTPMETHOD_GET, callBack);
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
		requestAsync(sAPIList.get(READ_USER), params, HTTPMETHOD_GET, callBack);
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
		requestAsync(sAPIList.get(READ_USER_BY_DOMAIN), params, HTTPMETHOD_GET,
				callBack);
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
		requestAsync(sAPIList.get(READ_USER_COUNT), params, HTTPMETHOD_GET,
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
