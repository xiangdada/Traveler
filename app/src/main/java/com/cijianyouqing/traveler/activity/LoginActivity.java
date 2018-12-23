package com.cijianyouqing.traveler.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cijianyouqing.traveler.R;
import com.cijianyouqing.traveler.bmobbean.UserBean;
import com.cijianyouqing.traveler.common.ToolBar;
import com.cijianyouqing.traveler.eventbus.LoginEvent;
import com.cijianyouqing.traveler.util.TextUtil;
import com.cijianyouqing.traveler.util.UserUtil;
import com.cijianyouqing.traveler.view.IToast;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by xiangpengfei on 2018/11/5.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.bt_login)
    Button bt_login;
    @BindView(R.id.tv_passwordRecovery)
    TextView tv_passwordRecovery;
    @BindView(R.id.tv_registeredAccount)
    TextView tv_registeredAccount;
    @BindView(R.id.iv_qq)
    ImageView iv_qq;
    @BindView(R.id.iv_weixixn)
    ImageView iv_weixixn;
    @BindView(R.id.iv_weibo)
    ImageView iv_weibo;

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int registerAccountRequestCode = 1000;
    private ProgressDialog progressDialog;

    private JSONObject partyResponseUserInfo;   // 平台返回的用户信息

    private Tencent mTencent;
    private QQUiListener qqUiListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        new ToolBar.Builder(this)
                .setTitle("登录")
                .setBackgroudResourceId(R.drawable.bg_white)
                .setImageViewL1(R.drawable.ic_back_black)
                .setOnToolBarClickListener(new ToolBar.OnToolBarClickListener() {
                    @Override
                    public void onToolBarClick(View clickView, ToolBar toolBar) {
                        if (clickView.getId() == toolBar.getImageViewL1().getId()) {
                            finish();
                        }
                    }
                })
                .build();

        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage("登录中，请稍后...");

        qqUiListener = new QQUiListener();

        addListener();


    }

    private void addListener() {
        bt_login.setOnClickListener(this);
        tv_passwordRecovery.setOnClickListener(this);
        tv_registeredAccount.setOnClickListener(this);
        iv_qq.setOnClickListener(this);
        iv_weixixn.setOnClickListener(this);
        iv_weibo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_login:
                if (TextUtil.empty(et_username.getText().toString().trim())) {
                    IToast.show(this, "用户名不能为空");
                    return;
                }
                if (TextUtil.empty(et_password.getText().toString().trim())) {
                    IToast.show(this, "密码不能为空");
                    return;
                }
                login();
                break;
            case R.id.tv_passwordRecovery:
                break;
            case R.id.tv_registeredAccount:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, registerAccountRequestCode);
                break;
            case R.id.iv_qq:
                QQLogin();
                break;
            case R.id.iv_weixixn:
                break;
            case R.id.iv_weibo:
                break;
        }
    }

    private void login() {
        UserBean userBean = new UserBean();
        userBean.setUsername(et_username.getText().toString().trim());
        userBean.setPassword(et_password.getText().toString().trim());
        progressDialog.show();
        userBean.login(new SaveListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e) {
                progressDialog.dismiss();
                if (userBean != null) {

                    try {
                        partyResponseUserInfo = new JSONObject(new Gson().toJson(userBean));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    UserUtil.setPassword(et_password.getText().toString().trim());
                    UserUtil.setUserInfo(UserUtil.APP, partyResponseUserInfo);
                    UserUtil.setThirdPartyToBmobResponseInfo(null);
                    toNextActivity();
                } else {
                    IToast.show(LoginActivity.this, "用户名或密码不正确");
                    Log.e(TAG, "e:" + e.getMessage() + "       " + e.toString());
                }
            }
        });
    }

    /**
     * 第三方平台账号登录
     *
     * @param snsType     只能是三种取值中的一种：weibo、qq、weixin
     * @param accessToken 接口调用凭证
     * @param expiresIn   access_token的有效时间
     * @param userId      用户身份的唯一标识，对应微博授权信息中的uid,对应qq和微信授权信息中的openid
     */
    private void thirdPartyLogin(final String snsType, String accessToken, String expiresIn, String userId) {
        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(snsType, accessToken, expiresIn, userId);
        BmobUser.loginWithAuthData(authInfo, new LogInListener<JSONObject>() {
            @Override
            public void done(JSONObject jsonObject, BmobException e) {
                Log.e("LoginActivity","第三方登录Bmob返回："+jsonObject.toString());
                int accountType = UserUtil.APP;
                if ("qq".equals(snsType)) {
                    accountType = UserUtil.QQ;
                } else if ("weixin".equals(snsType)) {
                    accountType = UserUtil.WEIXIN;
                } else if ("weibo".equals(snsType)) {
                    accountType = UserUtil.WEIBO;
                }
                UserUtil.setPassword("");
                UserUtil.setUserInfo(accountType, partyResponseUserInfo);
                UserUtil.setThirdPartyToBmobResponseInfo(jsonObject);

                toNextActivity();
            }
        });
    }

    private void QQLogin() {
        mTencent = Tencent.createInstance(getResources().getString(R.string.qqAppId), getApplicationContext());
        mTencent.login(this, "all", qqUiListener);
    }


    private void toNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new LoginEvent(true));
                finish();
            }
        }, 500);
    }

    /**
     * 从QQ、微信、微博获取用户信息之后再登录到Bmob
     *
     * @param accountType
     * @param snsType
     * @param accessToken
     * @param expiresIn
     * @param userId
     */
    private void initUserInfo(@UserUtil.AccountType int accountType, final String snsType, final String accessToken, final String expiresIn, final String userId) {
        if (accountType == UserUtil.QQ) {
          
            QQToken qqToken = mTencent.getQQToken();
            UserInfo userInfo = new UserInfo(getApplicationContext(), qqToken);
            userInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    Log.e("LoginActivity","QQ getUserInfo:"+o.toString());
                    partyResponseUserInfo = (JSONObject) o;
                    thirdPartyLogin(snsType, accessToken, expiresIn, userId);
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        } else if (accountType == UserUtil.WEIXIN) {

        } else if (accountType == UserUtil.WEIBO) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, new QQUiListener());
        if (resultCode == RESULT_OK) {
            if (requestCode == registerAccountRequestCode) {
                if (data != null && data.getExtras() != null) {
                    Bundle bundle = data.getExtras();
                    et_username.setText(bundle.getString("username", ""));
                    et_password.setText(bundle.getString("password", ""));
                }
            }
        }
    }

    /**
     * QQ认证回调接口
     */
    private class QQUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            IToast.show(LoginActivity.this, "登录成功");
            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject != null) {
                Log.e("LoginActivity", "QQ授权返回信息：" + jsonObject.toString());
                String snsType = "qq";
                String accessToken = jsonObject.optString("access_token", "");
                String expiresIn = jsonObject.optString("expires_in", "");
                String userId = jsonObject.optString("openid", "");
                mTencent.setOpenId(userId);
                mTencent.setAccessToken(accessToken,expiresIn);
                initUserInfo(UserUtil.QQ, snsType, accessToken, expiresIn, userId);
            }

        }

        @Override
        public void onError(UiError e) {
            IToast.show(LoginActivity.this, "登录失败");
            Log.e("onError", "code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {
            IToast.show(LoginActivity.this, "取消登录");

        }
    }


}
