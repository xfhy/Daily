package com.xfhy.androidbasiclibs.basekit.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xfhy.androidbasiclibs.basekit.presenter.BasePresenter;
import com.xfhy.androidbasiclibs.basekit.view.BaseView;

/**
 * author feiyang
 * create at 2017/9/15 13:44
 * description：MVP Activity的父类
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity implements
        BaseView {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isMVP = true;
        super.onCreate(savedInstanceState);

        initPresenter();
        if (mPresenter != null) {
            mPresenter.attach(this);
            mPresenter.onCreate();
        }
        // 这样做的目的是为了在初始化的时候,presenter已经初始化好了
        doOnCreate();
    }

    public abstract void initPresenter();

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
