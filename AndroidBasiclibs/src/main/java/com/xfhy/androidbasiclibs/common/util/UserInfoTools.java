package com.xfhy.androidbasiclibs.common.util;

import android.content.Context;

import static com.xfhy.androidbasiclibs.common.util.Constants.IS_NIGHT_MODE;

/**
 * @author xfhy
 * @create at 2017/11/4 20:07
 * description：用于用户信息的获取和管理工具类
 */
public class UserInfoTools {

    /**
     * 判断当前是否是夜间模式
     * return true:是   false:不是
     */
    public static boolean isNightMode(Context context) {
        return SpUtil.getBoolan(context,IS_NIGHT_MODE,false);
    }
}
