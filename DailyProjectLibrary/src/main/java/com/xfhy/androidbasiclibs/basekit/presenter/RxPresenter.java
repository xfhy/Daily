package com.xfhy.androidbasiclibs.basekit.presenter;

import com.xfhy.androidbasiclibs.basekit.view.BaseView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author xfhy
 *         create at 2017/12/17 9:23
 *         description：基于RxJava2的Presenter封装,控制订阅的生命周期
 */
public class RxPresenter<V extends BaseView> implements BasePresenter<V> {

    protected Reference<V> mView;
    /*
    * 如果有多个Disposable , RxJava中已经内置了一个容器CompositeDisposable,
    * 每当我们得到一个Disposable时就调用CompositeDisposable.add()将它添加到容器中, 在退出的时候, 调用CompositeDisposable
    * .clear() 即可切断所有的水管.
    * */
    protected CompositeDisposable mCompositeDisposable;

    @Override
    public void attachView(V view) {
        this.mView = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView.clear();
            mView = null;
        }
        unSubscribe();
    }

    @Override
    public V getView() {
        return mView.get();
    }

    /**
     * 切断水管,使得下游收不到事件
     */
    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    /**
     * 添加到容器中,方便控制
     *
     * @param disposable 用于解除订阅
     */
    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
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
