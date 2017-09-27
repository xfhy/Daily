package com.xfhy.androidbasiclibs.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by xfhy on 2017/9/24 22:16.
 * Description Toast的简单封装,方便使用
 */

public class ToastUtil {

    /**
     * 用Toast简单吐出文字  时间是:Toast.LENGTH_SHORT
     *
     * @param context Context
     * @param message 提示的文字
     */
    public static void showMessage(Context context, String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
