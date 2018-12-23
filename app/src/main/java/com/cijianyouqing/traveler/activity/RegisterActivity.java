package com.cijianyouqing.traveler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cijianyouqing.traveler.R;
import com.cijianyouqing.traveler.bmobbean.UserBean;
import com.cijianyouqing.traveler.common.ToolBar;
import com.cijianyouqing.traveler.util.TextUtil;
import com.cijianyouqing.traveler.view.IToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by xiangpengfei on 2018/11/5.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_nickname)
    EditText et_nickname;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_confirmpassword)
    EditText et_confirmpassword;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.rb0)
    RadioButton rb0;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.bt_register)
    Button bt_register;

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        new ToolBar.Builder(this)
                .setTitle("注册")
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

        addListener();
    }

    private void addListener() {
        bt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_register:
                if (TextUtil.empty(et_username.getText().toString().trim())) {
                    IToast.show(this, "用户名不能为空");
                    return;
                }
                if (TextUtil.empty(et_nickname.getText().toString().trim())) {
                    IToast.show(this, "昵称不能为空");
                    return;
                }
                if (TextUtil.empty(et_password.getText().toString().trim())) {
                    IToast.show(this, "密码不能为空");
                    return;
                }
                if (TextUtil.empty(et_confirmpassword.getText().toString().trim())) {
                    IToast.show(this, "密码不能为空");
                    return;
                }
                if (!et_password.getText().toString().trim().equals(et_confirmpassword.getText().toString().trim())) {
                    IToast.show(this, "两次密码不一致");
                    return;
                }
                register();
                break;
        }
    }

    private void register() {
        UserBean userBean = new UserBean();
        userBean.setUsername(et_username.getText().toString().trim());
        userBean.setNickname(et_nickname.getText().toString().trim());
        userBean.setPassword(et_password.getText().toString().trim());
        userBean.setSex(radiogroup.getCheckedRadioButtonId() == rb0.getId() ? "男" : "女");
        userBean.signUp(new SaveListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e) {
                if (userBean != null) {
                    IToast.show(RegisterActivity.this, "注册成功");
                    Log.e(TAG, "userBean:" + userBean.toString());
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", et_username.getText().toString().trim());
                    bundle.putString("password", et_password.getText().toString().trim());
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    IToast.show(RegisterActivity.this, "注册失败");
                    Log.e(TAG, "e:" + e.getMessage());
                }
            }
        });
    }
}
