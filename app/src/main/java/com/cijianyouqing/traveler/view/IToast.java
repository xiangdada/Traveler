package com.cijianyouqing.traveler.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Created by xiangpengfei on 2018/9/11.
 */
public class IToast {

    public static void show(Context context, @NonNull String content) {
        if (content == null) {
            content = "";
        }
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
