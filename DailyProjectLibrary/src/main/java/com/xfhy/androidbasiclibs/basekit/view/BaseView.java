package com.xfhy.androidbasiclibs.basekit.view;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * author feiyang
 * create at 2017/9/15 13:44
 * description：View公共接口
 */
public interface BaseView {

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

    /**
     * 显示空布局
     */
    void showEmptyView();

    /**
     * 显示未联网布局
     */
    void showOffline();

    /**
     * 显示内容区域
     */
    void showContent();

}
