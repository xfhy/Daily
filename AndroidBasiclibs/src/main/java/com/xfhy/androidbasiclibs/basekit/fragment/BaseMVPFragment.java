package com.xfhy.androidbasiclibs.basekit.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xfhy.androidbasiclibs.basekit.presenter.BasePresenter;
import com.xfhy.androidbasiclibs.basekit.view.BaseView;

/**
 * author feiyang
 * create at 2017/9/15 13:44
 * description：
 */
public abstract class BaseMVPFragment<P extends BasePresenter> extends BaseFragment implements
        BaseView {

    protected P mPresenter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.setView(this);
            mPresenter.onCreate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    /**
     * 初始化presenter
     */
    public abstract void initPresenter();

}
