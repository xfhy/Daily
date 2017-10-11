package com.xfhy.androidbasiclibs.basekit.presenter;

import android.content.Context;

import com.xfhy.androidbasiclibs.basekit.view.BaseView;

/**
 * author feiyang
 * create at 2017/9/30 14:56
 * description：presenter 的 父类
 */
public abstract class AbstractPresenter<V extends BaseView> implements BasePresenter<V> {

    protected V view;
    protected Context mContext;

    public AbstractPresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void setView(V view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

}
