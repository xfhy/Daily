package com.xfhy.androidbasiclibs.basekit.presenter;

import com.xfhy.androidbasiclibs.basekit.view.BaseView;

/**
 * author feiyang
 * create at 2017/9/15 13:44
 * description：
 */
public abstract class BasePresenter<V extends BaseView> {

    protected V mView;

    /**
     * 初始化View
     * @param view 该presenter的View
     */
    public void setView(V view){
        this.mView = view;
    }

    /**
     * 模拟界面的生命周期 onCreate()
     */
    public abstract void onCreate();

    /**
     * 模拟界面的生命周期 onResume()
     */
    public abstract void onResume();

    /**
     * 模拟界面的生命周期 onDestroy()
     */
    public abstract void onDestroy();

}
