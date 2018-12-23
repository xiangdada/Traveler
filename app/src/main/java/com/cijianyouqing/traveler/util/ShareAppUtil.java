package com.cijianyouqing.traveler.util;

import android.app.Activity;
import android.os.Bundle;

import com.cijianyouqing.traveler.R;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * Created by xiangpengfei on 2018/12/7.
 */
public class ShareAppUtil {

    /**
     * 分享到QQ
     *
     * @param activity
     */
    public static void shareToQQ(Activity activity) {
        Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);  // 必填
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");  // 必填
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");    // 选填
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://img1.imgtn.bdimg.com/it/u=907362241,1082096526&fm=200&gp=0.jpg");   // 选填
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getApplicationContext().getResources().getString(R.string.app_name));   // 选填
        Tencent tencent = Tencent.createInstance(activity.getApplicationContext().getResources().getString(R.string.qqAppId), activity.getApplicationContext());
        tencent.shareToQQ(activity, bundle, new IUiListener() {

            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 分享到QQ空间，当前API只支持分享图文，图片不能超过9张，预览图只会选择第一张F
     *
     * @param activity
     */
    public static void shareToZONE(Activity activity) {
        ArrayList<String> images = new ArrayList<>();
        images.add("http://img1.imgtn.bdimg.com/it/u=907362241,1082096526&fm=200&gp=0.jpg");
        images.add("http://www.jituwang.com/uploads/allimg/111218/6443-11121Q0412063.jpg");
        Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);   // 选填
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, "要分享的标题");   // 必填
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要"); // 选填
        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://www.baidu.com");    // 必填
        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);    // 选填
        Tencent tencent = Tencent.createInstance(activity.getApplicationContext().getResources().getString(R.string.qqAppId), activity.getApplicationContext());
        tencent.shareToQzone(activity, bundle, new IUiListener() {

            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }
}
