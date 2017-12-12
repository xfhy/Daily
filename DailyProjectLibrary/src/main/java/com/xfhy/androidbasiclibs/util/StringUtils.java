package com.xfhy.androidbasiclibs.util;

import android.content.Context;
import android.support.annotation.StringRes;

/**
 * @author feiyang
 * @time create at 2017/11/1 18:00
 * @description 字符串工具
 */
public class StringUtils {

    /**
     * 通过resId获取字符串
     *
     * @param context Context
     * @param resId   字符串的id
     */
    public static String getStringByResId(Context context, @StringRes int resId) {
        return context.getString(resId);
    }

}
