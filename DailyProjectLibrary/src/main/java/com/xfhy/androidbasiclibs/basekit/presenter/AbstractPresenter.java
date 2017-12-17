package com.xfhy.androidbasiclibs.basekit.presenter;

import com.xfhy.androidbasiclibs.basekit.view.BaseView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * author feiyang
 * create at 2017/9/30 14:56
 * description：presenter 的 父类
 */
public abstract class AbstractPresenter<V extends BaseView> implements BasePresenter<V> {

    protected Reference<V> view;

    @Override
    public void attachView(V view) {
        this.view = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        if (view != null) {
            view.clear();
            view = null;
        }
    }

    @Override
    public V getView() {
        return view.get();
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
