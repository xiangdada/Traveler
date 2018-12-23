package com.cijianyouqing.traveler.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by xiangpengfei on 2018/9/11.
 */
public class SharedPreferencesUtils {
    private static final String name = "com.cijianyouqing.traveler.util.SharedPreferencesUtils";

    public static void putBoolean(Context context, @NonNull String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        String encrypt = CipherHelper.getInstence().encrypt(String.valueOf(value));
        editor.putString(key, encrypt);
        boolean result = editor.commit();
        if (!result) {
            editor.apply();
        }
    }

    public static void putInt(Context context, @NonNull String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        String encrypt = CipherHelper.getInstence().encrypt(String.valueOf(value));
        editor.putString(key, encrypt);
        boolean result = editor.commit();
        if (!result) {
            editor.apply();
        }
    }

    public static void putFloat(Context context, @NonNull String key, float value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        String encrypt = CipherHelper.getInstence().encrypt(String.valueOf(value));
        editor.putString(key, encrypt);
        boolean result = editor.commit();
        if (!result) {
            editor.apply();
        }
    }

    public static void putLong(Context context, @NonNull String key, long value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        String encrypt = CipherHelper.getInstence().encrypt(String.valueOf(value));
        editor.putString(key, encrypt);
        boolean result = editor.commit();
        if (!result) {
            editor.apply();
        }
    }

    public static void putString(Context context, @NonNull String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        String encrypt = CipherHelper.getInstence().encrypt(value);
        editor.putString(key, encrypt);
        boolean result = editor.commit();
        if (!result) {
            editor.apply();
        }
    }

    public static boolean getBoolean(Context context, @NonNull String key, boolean valueDef) {
        if (!TextUtil.empty(key)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            String encrypt = sharedPreferences.getString(key, "");  // 获取加密储存的数据
            if (!TextUtils.isEmpty(encrypt)) {
                String decrypt = CipherHelper.getInstence().decrypt(encrypt);   // 将储存的数据进行解密
                if (!TextUtils.isEmpty(decrypt)) {
                    try {
                        boolean value = Boolean.parseBoolean(decrypt);
                        return value;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return valueDef;
    }

    public static int getInt(Context context, @NonNull String key, int valueDef) {
        if (!TextUtil.empty(key)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            String encrypt = sharedPreferences.getString(key, "");  // 获取加密储存的数据
            if (!TextUtils.isEmpty(encrypt)) {
                String decrypt = CipherHelper.getInstence().decrypt(encrypt);   // 将储存的数据进行解密
                if (!TextUtils.isEmpty(decrypt)) {
                    try {
                        int value = Integer.parseInt(decrypt);
                        return value;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return valueDef;
    }

    public static float getFloat(Context context, @NonNull String key, float valueDef) {
        if (!TextUtil.empty(key)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            String encrypt = sharedPreferences.getString(key, "");  // 获取加密储存的数据
            if (!TextUtils.isEmpty(encrypt)) {
                String decrypt = CipherHelper.getInstence().decrypt(encrypt);   // 将储存的数据进行解密
                if (!TextUtils.isEmpty(decrypt)) {
                    try {
                        float value = Float.parseFloat(decrypt);
                        return value;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return valueDef;
    }

    public static long getLong(Context context, @NonNull String key, long valueDef) {
        if (!TextUtil.empty(key)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            String encrypt = sharedPreferences.getString(key, "");  // 获取加密储存的数据
            if (!TextUtils.isEmpty(encrypt)) {
                String decrypt = CipherHelper.getInstence().decrypt(encrypt);   // 将储存的数据进行解密
                if (!TextUtils.isEmpty(decrypt)) {
                    try {
                        long value = Long.parseLong(decrypt);
                        return value;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return valueDef;
    }

    public static String getString(Context context, @NonNull String key, String valueDef) {
        if (!TextUtil.empty(key)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            String encrypt = sharedPreferences.getString(key, "");  // 获取加密储存的数据
            if (!TextUtils.isEmpty(encrypt)) {
                String decrypt = CipherHelper.getInstence().decrypt(encrypt);   // 将储存的数据进行解密
                if (!TextUtils.isEmpty(decrypt)) {
                    return decrypt;
                }
            }
        }
        return valueDef;
    }

}
