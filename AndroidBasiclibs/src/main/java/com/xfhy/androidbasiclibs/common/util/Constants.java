package com.xfhy.androidbasiclibs.common.util;

/**
 * @author feiyang
 * @time create at 2017/11/1 18:37
 * @description 存放一些常量
 */
public class Constants {


    /*-----------------界面的一些状态---------------------*/
    public static final int STATE_LOADING = 0x000011;
    public static final int STATE_REFRESH = 0x000012;
    public static final int STATE_LOAD_MORE = 0x000013;
    public static final int STATE_ERROR = 0x000014;
    public static final int STATE_NO_DATA = 0x000015;
    public static final int STATE_NORMAL = 0x000016;

    /*-----------------存储在SharedPreferences文件中的键----------------------*/
    /**
     * 夜间模式
     */
    public static final String IS_NIGHT_MODE = "is_night_mode";
    /**
     * 无图模式
     */
    public static final String IS_NO_IMAGE = "is_no_image";
    /**
     * 自动缓存
     */
    public static final String IS_AUTO_CACHE = "is_auto_cache";


}
