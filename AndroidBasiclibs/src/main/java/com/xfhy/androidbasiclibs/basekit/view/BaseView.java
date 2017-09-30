package com.xfhy.androidbasiclibs.basekit.view;

/**
 * author feiyang
 * create at 2017/9/15 13:44
 * description：View公共接口
 */
public interface BaseView<T> {

    /**
     * 显示正在加载的布局
     */
    void onLoading();

    /**
     * 关闭正在加载
     */
    void closeLoading();

    /**
     * 显示错误信息
     * @param msg 错误信息
     */
    void showErrorMsg(String msg);
}
