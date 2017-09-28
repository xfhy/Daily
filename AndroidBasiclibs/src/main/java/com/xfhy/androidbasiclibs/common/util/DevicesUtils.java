package com.xfhy.androidbasiclibs.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by xfhy on 2017/9/24 21:57.
 * Description: 设备的工具类
 * 用来存放诸如:设备屏幕宽度  设置连接网络状态...
 */

public class DevicesUtils {

    /**
     * 判断网络连接是否正常
     *
     * @param context Context
     * @return 返回网络连接是否正常  true:正常 false:无网络连接
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取屏幕的大小(宽高)
     * @param context Context
     * @return DisplayMetrics实例
     *
     * displayMetrics.heightPixels是高度(单位是像素)
     * displayMetrics.heightPixels是宽度(单位是像素)
     */
    public static DisplayMetrics getDevicesSize(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (context != null) {
            WindowManager windowManager = (WindowManager) context.getApplicationContext()
                    .getSystemService(Context
                    .WINDOW_SERVICE);
            Display defaultDisplay = windowManager.getDefaultDisplay();
            defaultDisplay.getMetrics(displayMetrics);
        }
        return displayMetrics;
    }

}
