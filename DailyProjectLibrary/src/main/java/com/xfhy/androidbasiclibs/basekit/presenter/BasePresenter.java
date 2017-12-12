package com.xfhy.androidbasiclibs.basekit.presenter;

import com.xfhy.androidbasiclibs.basekit.view.BaseView;

/**
 * author feiyang
 * create at 2017/9/15 13:44
 * description：MVP Presenter的父接口
 */
public interface BasePresenter<V extends BaseView> {

    /**
     *
     * @param view 该presenter的View
     */
    void setView(V view);

    /**
     * 模拟界面的生命周期 onCreate()
     */
    void onCreate();

    /**
     * 模拟界面的生命周期 onResume()
     */
    void onResume();

    /**
     * 模拟界面的生命周期 onDestroy()
     */
    void onDestroy();

}
