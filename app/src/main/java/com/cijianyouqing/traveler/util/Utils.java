package com.cijianyouqing.traveler.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;

import java.util.List;

/**
 * Created by xiangpengfei on 2018/8/29.
 */
public class Utils {

    /**
     * @param position        要显示的位置
     * @param currentPosition 当前显示的位置，第一次调用传负数或者大于等于fragments中元素数的值
     * @param fragmentManager Fragment管理器
     * @param fragments       Fragment集合
     * @param resourceId      外部容器控件id
     * @return 调用该方法之后显示的位置
     */
    public static int showFragment(int position, int currentPosition, FragmentManager fragmentManager, List<Fragment> fragments, int resourceId) {
        try {
            if (fragmentManager != null && fragments != null && position >= 0 && position < fragments.size()) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment currentFragment = null;
                if (currentPosition >= 0 && currentPosition < fragments.size()) {
                    currentFragment = fragments.get(currentPosition);
                }
                if (position != currentPosition) {
                    if (currentFragment != null) {
                        fragmentTransaction.hide(currentFragment);
                    }
                    Fragment fragment = fragments.get(position);
                    if (fragment != null) {
                        if (!fragment.isAdded()) {
                            fragmentTransaction.add(resourceId, fragment, fragment.getClass().getName());
                        } else {
                            fragmentTransaction.show(fragment);
                        }
                        fragmentTransaction.commit();
                        return position;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentPosition;
    }


    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }


}
