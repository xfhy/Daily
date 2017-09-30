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
        super.onCreate(savedInstanceState);

        if (mPresenter != null) {
            mPresenter.setView(this);
            mPresenter.onCreate();
        }
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
