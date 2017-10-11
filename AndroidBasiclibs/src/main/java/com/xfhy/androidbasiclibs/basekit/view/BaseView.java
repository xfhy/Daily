package com.xfhy.androidbasiclibs.basekit.view;

import com.trello.rxlifecycle2.LifecycleTransformer;

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
     * 在Activity或者Fragment中实现bindLifecycle()方法
     * 获取LifecycleTransformer用于在使用RxJava时防止内存泄漏
     */
    LifecycleTransformer bindLifecycle();

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

}
