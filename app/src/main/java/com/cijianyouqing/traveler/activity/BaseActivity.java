package com.cijianyouqing.traveler.activity;

import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.cijianyouqing.traveler.eventbus.LoginEvent;
import com.cijianyouqing.traveler.util.ShareAppUtil;
import com.cijianyouqing.traveler.util.UserUtil;
import com.cijianyouqing.traveler.view.IToast;
import com.cijianyouqing.traveler.view.SharePopupWindow;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobUser;

/**
 * Created by xiangpengfei on 2018/8/29.
 */
public abstract class BaseActivity extends FragmentActivity {
    private SharePopupWindow sharePopupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void share() {
        if (sharePopupWindow == null) {
            sharePopupWindow = new SharePopupWindow(this);
            sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setBackgroudAlpha(1f);
                }
            });
            sharePopupWindow.setOnItemClickListener(new SharePopupWindow.OnItemClickListener() {
                @Override
                public void onItemClick(String title, int postion) {
                    IToast.show(BaseActivity.this, title);
                    if ("QQ".equals(title)) {
                        ShareAppUtil.shareToQQ(BaseActivity.this);
                    }else if("QQ空间".equals(title)){
                        ShareAppUtil.shareToZONE(BaseActivity.this);
                    }
                    sharePopupWindow.dismiss();
                }
            });
        }
        int height = sharePopupWindow.getHeight();
        sharePopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        setBackgroudAlpha(0.5f);
    }

    protected void logout() {
        try {
            BmobUser.logOut();
            UserUtil.logOut();
            EventBus.getDefault().post(new LoginEvent(false));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setBackgroudAlpha(@FloatRange(from = 0, to = 1) float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }


}
