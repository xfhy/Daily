package com.xfhy.androidbasiclibs.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.xfhy.androidbasiclibs.BaseApplication;

/**
 * Created by xfhy on 2017/9/24 21:57.
 * Description: 设备的工具类
 * 用来存放诸如:设备屏幕宽度  设置连接网络状态...
 */

public class DevicesUtils {

    /**
     * 判断网络连接是否正常
     *
     * @return 返回网络连接是否正常  true:正常 false:无网络连接
     */
    public static boolean hasNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getApplication()
                .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取屏幕的大小(宽高)
     *
     * @return DisplayMetrics实例
     * <p>
     * displayMetrics.heightPixels是高度(单位是像素)
     * displayMetrics.heightPixels是宽度(单位是像素)
     */
    public static DisplayMetrics getDevicesSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) BaseApplication.getApplication().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display defaultDisplay = windowManager.getDefaultDisplay();
            defaultDisplay.getMetrics(displayMetrics);
            return displayMetrics;
        }
        return new DisplayMetrics();
    }

    /**
     * 跳转到设置界面
     *
     * @param context Context
     */
    public static void goSetting(Context context) {
        if (context != null) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }

}
