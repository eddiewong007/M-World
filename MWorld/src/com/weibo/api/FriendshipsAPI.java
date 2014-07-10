package com.weibo.api;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.weibo.entities.AccessToken;

/**
 * 此类封装了关系的接口。
 * 详情见<a href=http://t.cn/8FUByoM">关系接口</a>
 * 
 * @author SINA
 * @date 2014-03-03
 */
public class FriendshipsAPI extends BaseAPI {

    public FriendshipsAPI(AccessToken accessToken) {
        super(accessToken);
    }

    private static final String API_BASE_URL = API_SERVER + "/friendships";

    /**
     * 获取用户的关注列表。
     * 
     * @param uid           需要查询的用户UID
     * @param count         单页返回的记录条数，默认为50，最大不超过200
     * @param cursor        返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param trim_status   返回值中user字段中的status字段开关，false：返回完整status字段、true：status字段仅返回status_id，默认为true
     * @param callBack      异步请求回调接口
     */
    public void friends(long uid, int count, int cursor, boolean trim_status, AjaxCallBack<String> callBack) {
        AjaxParams params = new AjaxParams(); params = buildFriendsParam(count, cursor, trim_status);
        params.put("uid", String.valueOf(uid));
        requestAsync(API_BASE_URL + "/friends.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取用户的关注列表。
     * 
     * @param screen_name   需要查询的用户昵称
     * @param count         单页返回的记录条数，默认为50，最大不超过200
     * @param cursor        返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param trim_status   返回值中user字段中的status字段开关，false：返回完整status字段、true：status字段仅返回status_id，默认为true
     * @param callBack      异步请求回调接口
     */
    public void friends(String screen_name, int count, int cursor, boolean trim_status, AjaxCallBack<String> callBack) {
        AjaxParams params = buildFriendsParam(count, cursor, trim_status);
        params.put("screen_name", screen_name);
        requestAsync(API_BASE_URL + "/friends.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取两个用户之间的共同关注人列表。
     * 
     * @param uid       需要获取共同关注关系的用户UID
     * @param suid      需要获取共同关注关系的用户UID，默认为当前登录用户
     * @param count     单页返回的记录条数，默认为50
     * @param page      返回结果的页码，默认为1
     * @param trim_status 返回值中user字段中的status字段开关，false：返回完整status字段、true：status字段仅返回status_id，默认为true
     * @param callBack  异步请求回调接口
     */
    public void inCommon(long uid, long suid, int count, int page, boolean trim_status, AjaxCallBack<String> callBack) {
        AjaxParams params = buildFriendIDParam(uid, count);
        params.put("suid", String.valueOf(suid));
        params.put("page", String.valueOf(page));
        params.put("trim_status", String.valueOf(trim_status ? 1: 0));
        requestAsync(API_BASE_URL + "/friends/in_common.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取用户的双向关注列表，即互粉列表。
     * 
     * @param uid       需要获取双向关注列表的用户UID
     * @param count     单页返回的记录条数，默认为50
     * @param page      返回结果的页码，默认为1
     * @param callBack  异步请求回调接口
     */
    public void bilateral(long uid, int count, int page, AjaxCallBack<String> callBack) {
        AjaxParams params = buildFriendIDParam(uid, count);
        params.put("page", String.valueOf(page));
        requestAsync(API_BASE_URL + "/friends/bilateral.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取用户双向关注的用户ID列表，即互粉UID列表。
     * 
     * @param uid       需要获取双向关注列表的用户UID
     * @param count     单页返回的记录条数，默认为50，最大不超过2000
     * @param page      返回结果的页码，默认为1
     * @param callBack  异步请求回调接口
     */
    public void bilateralIds(long uid, int count, int page, AjaxCallBack<String> callBack) {
        AjaxParams params = buildFriendIDParam(uid, count);
        params.put("page", String.valueOf(page));
        requestAsync(API_BASE_URL + "/friends/bilateral/ids.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取用户关注的用户UID列表。
     * 
     * @param uid       需要查询的用户UID
     * @param count     单页返回的记录条数，默认为500，最大不超过5000
     * @param cursor    返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param callBack  异步请求回调接口
     */
    public void friendsIds(long uid, int count, int cursor, AjaxCallBack<String> callBack) {
        AjaxParams params = buildFriendIDParam(uid, count);
        params.put("cursor", String.valueOf(cursor));
        requestAsync(API_BASE_URL + "/friends/ids.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取用户关注的用户UID列表。
     * 
     * @param screen_name   需要查询的用户昵称
     * @param count         单页返回的记录条数，默认为500，最大不超过5000
     * @param cursor        返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param callBack      异步请求回调接口
     */
    public void friendsIds(String screen_name, int count, int cursor, AjaxCallBack<String> callBack) {
        AjaxParams params = new AjaxParams();
        params.put("screen_name", screen_name);
        params.put("count",String.valueOf( count));
        params.put("cursor",String.valueOf(cursor));
        requestAsync(API_BASE_URL + "/friends/ids.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取用户的粉丝列表(最多返回5000条数据)。
     * 
     * @param uid       需要查询的用户UID
     * @param count     单页返回的记录条数，默认为50，最大不超过200
     * @param cursor    返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param trim_status 返回值中user字段中的status字段开关，false：返回完整status字段、true：status字段仅返回status_id，默认为false
     * @param callBack  异步请求回调接口
     */
    public void followers(long uid, int count, int cursor, boolean trim_status, AjaxCallBack<String> callBack) {
        AjaxParams params = buildFriendIDParam(uid, count);
        params.put("cursor", String.valueOf(cursor));
        params.put("trim_status", String.valueOf(trim_status ? 1: 0));
        requestAsync(API_BASE_URL + "/followers.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取用户的粉丝列表(最多返回5000条数据)。
     * 
     * @param screen_name   需要查询的用户昵称
     * @param count         单页返回的记录条数，默认为50，最大不超过200
     * @param cursor        返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param trim_status   返回值中user字段中的status字段开关，false：返回完整status字段、true：status字段仅返回status_id，默认为false
     * @param callBack      异步请求回调接口
     */
    public void followers(String screen_name, int count, int cursor, boolean trim_status, AjaxCallBack<String> callBack) {
        AjaxParams params = buildFriendsParam(count, cursor, trim_status);
        params.put("screen_name", screen_name);
        requestAsync(API_BASE_URL + "/followers.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取用户粉丝的用户UID列表。
     * 
     * @param uid       需要查询的用户UID
     * @param count     单页返回的记录条数，默认为500，最大不超过5000
     * @param cursor    返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param callBack  异步请求回调接口
     */
    public void followersIds(long uid, int count, int cursor, AjaxCallBack<String> callBack) {
        AjaxParams params = buildFriendIDParam(uid, count);
        params.put("cursor", String.valueOf(cursor));
        requestAsync(API_BASE_URL + "/followers/ids.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取用户粉丝的用户UID列表。
     * 
     * @param screen_name   需要查询的用户昵称
     * @param count         单页返回的记录条数，默认为500，最大不超过5000
     * @param cursor        返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param callBack      异步请求回调接口
     */
    public void followersIds(String screen_name, int count, int cursor, AjaxCallBack<String> callBack) {
        AjaxParams params = new AjaxParams();
        params.put("screen_name", screen_name);
        params.put("count", String.valueOf(count));
        params.put("cursor",String.valueOf( cursor));
        requestAsync(API_BASE_URL + "/followers/ids.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取用户的活跃粉丝列表。
     * 
     * @param uid       需要查询的用户UID
     * @param count     返回的记录条数，默认为20，最大不超过200
     * @param callBack  异步请求回调接口
     */
    public void followersActive(long uid, int count, AjaxCallBack<String> callBack) {
        AjaxParams params = buildFriendIDParam(uid, count);
        requestAsync(API_BASE_URL + "/followers/active.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取当前登录用户的关注人中又关注了指定用户的用户列表。
     * 
     * @param uid       指定的关注目标用户UID
     * @param count     单页返回的记录条数，默认为50
     * @param page      返回结果的页码，默认为1
     * @param callBack  异步请求回调接口
     */
    public void chainFollowers(long uid, int count, int page, AjaxCallBack<String> callBack) {
        AjaxParams params = buildFriendIDParam(uid, count);
        params.put("page", String.valueOf(page));
        requestAsync(API_BASE_URL + "/friends_chain/followers.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取两个用户之间的详细关注关系情况。
     * 
     * @param source_id     源用户的UID
     * @param target_id     目标用户的UID
     * @param callBack      异步请求回调接口 
     */
    public void show(long source_id, long target_id, AjaxCallBack<String> callBack) {
        AjaxParams params = new AjaxParams();
        params.put("source_id", String.valueOf(source_id));
        params.put("target_id", String.valueOf(target_id));
        requestAsync(API_BASE_URL + "/show.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取两个用户之间的详细关注关系情况。
     * 
     * @param source_id             源用户的UID
     * @param target_screen_name    目标用户的微博昵称
     * @param callBack              异步请求回调接口
     */
    public void show(long source_id, String target_screen_name, AjaxCallBack<String> callBack) {
        AjaxParams params = new AjaxParams();
        params.put("source_id", String.valueOf(source_id));
        params.put("target_screen_name", target_screen_name);
        requestAsync(API_BASE_URL + "/show.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取两个用户之间的详细关注关系情况。
     * 
     * @param source_screen_name    源用户的微博昵称
     * @param target_id             目标用户的UID
     * @param callBack              异步请求回调接口
     */
    public void show(String source_screen_name, long target_id, AjaxCallBack<String> callBack) {
        AjaxParams params = new AjaxParams();
        params.put("source_screen_name", source_screen_name);
        params.put("target_id", String.valueOf(target_id));
        requestAsync(API_BASE_URL + "/show.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 获取两个用户之间的详细关注关系情况。
     * 
     * @param source_screen_name 源用户的微博昵称
     * @param target_screen_name 目标用户的微博昵称
     * @param callBack           异步请求回调接口
     */
    public void show(String source_screen_name, String target_screen_name, AjaxCallBack<String> callBack) {
        AjaxParams params = new AjaxParams();
        params.put("target_screen_name", target_screen_name);
        params.put("source_screen_name", source_screen_name);
        requestAsync(API_BASE_URL + "/show.json", params, HTTPMETHOD_GET, callBack);
    }

    /**
     * 关注一个用户。
     * 
     * @param uid           需要关注的用户ID
     * @param screen_name   需要关注的用户昵称
     * @param callBack      异步请求回调接口
     */
    public void create(long uid, String screen_name, AjaxCallBack<String> callBack) {
        AjaxParams params = new AjaxParams();
        params.put("uid", String.valueOf(uid));
        params.put("screen_name", screen_name);
        requestAsync(API_BASE_URL + "/create.json", params, HTTPMETHOD_POST, callBack);
    }

    /**
     * 关注一个用户
     * 
     * @param screen_name   需要关注的用户昵称
     * @param callBack      异步请求回调接口
     */
    @Deprecated
    public void create(String screen_name, AjaxCallBack<String> callBack) {
        AjaxParams params = new AjaxParams();
        params.put("screen_name", screen_name);
        requestAsync(API_BASE_URL + "/create.json", params, HTTPMETHOD_POST, callBack);
    }

    /**
     * 取消关注一个用户。
     * 
     * @param uid           需要取消关注的用户ID
     * @param screen_name   需要取消关注的用户昵称
     * @param callBack      异步请求回调接口
     */
    public void destroy(long uid, String screen_name, AjaxCallBack<String> callBack) {
        AjaxParams params = new AjaxParams();
        params.put("uid", String.valueOf(uid));
        params.put("screen_name", screen_name);
        requestAsync(API_BASE_URL + "/destroy.json", params, HTTPMETHOD_POST, callBack);
    }

    /**
     * 取消关注一个用户。
     * 
     * @param screen_name 需要取消关注的用户昵称
     * @param callBack    异步请求回调接口
     */
    @Deprecated
    public void destroy(String screen_name, AjaxCallBack<String> callBack) {
        AjaxParams params = new AjaxParams();
        params.put("screen_name", screen_name);
        requestAsync(API_BASE_URL + "/destroy.json", params, HTTPMETHOD_POST, callBack);
    }

    /** 组装FriendsParam的参数 */
    private AjaxParams buildFriendsParam(int count, int cursor, boolean trim_status) {
        AjaxParams params = new AjaxParams();
        params.put("count", String.valueOf(count));
        params.put("cursor", String.valueOf(cursor));
        params.put("trim_status", String.valueOf(trim_status ? 1: 0));
        return params;
    }

    /** 组装FriendsID的参数 */
    private AjaxParams buildFriendIDParam(long uid, int count) {
        AjaxParams params = new AjaxParams();
        params.put("uid", String.valueOf(uid));
        params.put("count", String.valueOf(count));
        return params;
    }
}
