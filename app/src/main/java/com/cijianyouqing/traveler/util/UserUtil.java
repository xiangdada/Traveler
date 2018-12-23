package com.cijianyouqing.traveler.util;

import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.Log;

import com.cijianyouqing.traveler.application.MyApplication;
import com.cijianyouqing.traveler.bmobbean.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.bmob.v3.BmobUser;

/**
 * Created by xiangpengfei on 2018/11/7.
 */
public class UserUtil {
    public static final String CURRENTPASSWORD = "currentPassword";
    public static final String THIRDPARTYLOGININFO = "thirdPartyLoginInfo";
    public static final String USERINFO = "userInfo";
    public static final String USERNAME = "userName";
    public static final String NICKNAME = "nickname";
    public static final String SEX = "sex";
    public static final String ACCOUNTTYPE = "accountType";


    public static final int APP = 0;    // app注册
    public static final int QQ = 1; // qq
    public static final int WEIXIN = 2; // 微信
    public static final int WEIBO = 3;  // 微博

    @IntDef({APP, QQ, WEIXIN, WEIBO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AccountType {
    }

    /**
     * 当前用户信息
     *
     * @return
     */
    public static UserBean getCurrentUser() {
        return BmobUser.getCurrentUser(UserBean.class);
    }

    /**
     * 用户是否登录
     *
     * @return
     */
    public static boolean isLoginin() {
        return getCurrentUser() != null;
    }

    public static void setUsername(String username) {
        SharedPreferencesUtils.putString(MyApplication.getInstance(), USERNAME, ConvertUtils.Obj2Str(username));
    }

    public static String getUsername() {
        String username = SharedPreferencesUtils.getString(MyApplication.getInstance(), USERNAME, "");  // 获取单独保存的用户名
        if (TextUtils.isEmpty(username)) {  // 如果单独保存的用户名为空，则根据账号类型从用户数据中读取
            JSONObject userInfo = getUserInfo();
            if (userInfo != null) {
                int accountType = userInfo.optInt(ACCOUNTTYPE, APP);
                if (accountType == APP) {
                    JSONObject partyResponseUserInfo = userInfo.optJSONObject(USERINFO);
                    if (partyResponseUserInfo != null) {
                        username = partyResponseUserInfo.optString("username", "");
                    }
                }
            }
            setUsername(username);
        }
        return ConvertUtils.Obj2Str(username);
    }

    public static void setPassword(String password) {
        SharedPreferencesUtils.putString(MyApplication.getInstance(), CURRENTPASSWORD, password);
    }

    public static String getPassword() {
        String password = SharedPreferencesUtils.getString(MyApplication.getInstance(), CURRENTPASSWORD, "");
        return ConvertUtils.Obj2Str(password);
    }

    public static void setSex(String sex) {
        SharedPreferencesUtils.putString(MyApplication.getInstance(), SEX, ConvertUtils.Obj2Str(sex));
    }

    public static String getSex() {
        String sex = SharedPreferencesUtils.getString(MyApplication.getInstance(), SEX, "");  // 获取单独保存的性别
        if (TextUtils.isEmpty(sex)) {  // 如果单独保存的性别为空，则根据账号类型从用户数据中读取
            JSONObject userInfo = getUserInfo();
            if (userInfo != null) {
                int accountType = userInfo.optInt(ACCOUNTTYPE, APP);
                if (accountType == APP) {
                    JSONObject partyResponseUserInfo = userInfo.optJSONObject(USERINFO);
                    if (partyResponseUserInfo != null) {
                        sex = partyResponseUserInfo.optString("sex", "");
                    }
                } else if (accountType == QQ) {
                    JSONObject partyResponseUserInfo = userInfo.optJSONObject(USERINFO);
                    if (partyResponseUserInfo != null) {
                        sex = partyResponseUserInfo.optString("gender", "");
                    }
                }
            }
            setSex(sex);
        }
        return ConvertUtils.Obj2Str(sex);
    }

    public static void setNickname(String nickname) {
        SharedPreferencesUtils.putString(MyApplication.getInstance(), NICKNAME, ConvertUtils.Obj2Str(nickname));
    }

    public static String getNickname() {
        String nickname = SharedPreferencesUtils.getString(MyApplication.getInstance(), NICKNAME, "");  // 获取单独保存的昵称
        if (TextUtils.isEmpty(nickname)) {  // 如果单独保存的昵称为空，则根据账号类型从用户数据中读取
            JSONObject userInfo = getUserInfo();
            if (userInfo != null) {
                int accountType = userInfo.optInt(ACCOUNTTYPE, APP);
                if (accountType == APP) {
                    JSONObject partyResponseUserInfo = userInfo.optJSONObject(USERINFO);
                    if (partyResponseUserInfo != null) {
                        nickname = partyResponseUserInfo.optString("nickname", "");
                    }
                } else if (accountType == QQ) {
                    JSONObject partyResponseUserInfo = userInfo.optJSONObject(USERINFO);
                    if (partyResponseUserInfo != null) {
                        Log.e("LoginActivity","解密:" +partyResponseUserInfo.toString());
                        nickname = partyResponseUserInfo.optString("nickname", "");
                    }
                }
            }
            setNickname(nickname);
        }
        return ConvertUtils.Obj2Str(nickname);
    }

    public static String getVipLevel() {
        String vipLevel = (String) BmobUser.getObjectByKey("vipLevel");
        return ConvertUtils.Obj2Str(vipLevel);
    }


    /**
     * 保存第三方登录平台登录认证成功之后，再接入到Bmob进行第三方关联登录返回的数据
     * 示例：{"qq":{"openid":"xxxxxxxxxxxx","access_token":"xxxxxxxxx","expires_in":111111111}}
     * 第三方平台主要有三种：qq、weixin、weibo
     * openid：用户身份的唯一标识，对应微博授权信息中的uid,对应qq和微信授权信息中的openid
     * access_token：接口调用凭证
     * expires_in：access_token的有效时间
     *
     * @param jsonObject
     */
    public static void setThirdPartyToBmobResponseInfo(JSONObject jsonObject) {
        String info = "";
        if (jsonObject != null) {
            info = jsonObject.toString();
        }
        SharedPreferencesUtils.putString(MyApplication.getInstance(), THIRDPARTYLOGININFO, info);
    }

    /**
     * 获取第三方登录平台登录认证成功之后，再接入到Bmob进行第三方关联登录返回的数据
     * 示例：{"qq":{"openid":"xxxxxxxxxxxx","access_token":"xxxxxxxxx","expires_in":111111111}}
     * 第三方平台主要有三种：qq、weixin、weibo
     * openid：用户身份的唯一标识，对应微博授权信息中的uid,对应qq和微信授权信息中的openid
     * access_token：接口调用凭证
     * expires_in：access_token的有效时间
     */
    public static JSONObject getThirdPartyToBmobResponseInfo() {
        String info = SharedPreferencesUtils.getString(MyApplication.getInstance(), THIRDPARTYLOGININFO, "");
        if (!TextUtils.isEmpty(info)) {
            try {
                JSONObject jsonObject = new JSONObject(info);
                return jsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 设置用户信息
     *
     * @param accountType
     * @param partyResponseUserInfo
     */
    public static void setUserInfo(@AccountType int accountType, JSONObject partyResponseUserInfo) {
        if (partyResponseUserInfo == null) {
            partyResponseUserInfo = new JSONObject();
        }
        JSONObject userInfo = new JSONObject();
        try {
            userInfo.put(ACCOUNTTYPE, accountType);
            userInfo.put(USERINFO, partyResponseUserInfo);
            SharedPreferencesUtils.putString(MyApplication.getInstance(), USERINFO, userInfo.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static JSONObject getUserInfo() {
        String s = SharedPreferencesUtils.getString(MyApplication.getInstance(), USERINFO, "");
        if (!TextUtils.isEmpty(s)) {
            try {
                JSONObject userInfo = new JSONObject(s);
                return userInfo;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取登录账号的类型，app注册，qq，微信，微博
     *
     * @return
     */
    public static int getAccountType() {
        int accountType = APP;
        JSONObject userInfo = getUserInfo();
        if (userInfo != null) {
            accountType = userInfo.optInt(ACCOUNTTYPE, APP);
        }
        return accountType;
    }

    public static void reSetUserInfo(){
        SharedPreferencesUtils.putString(MyApplication.getInstance(),USERINFO,"");
    }

    public static void logOut(){
        setNickname("");
        setSex("");
        setUsername("");
        setPassword("");
        setThirdPartyToBmobResponseInfo(null);
        reSetUserInfo();
    }


}
