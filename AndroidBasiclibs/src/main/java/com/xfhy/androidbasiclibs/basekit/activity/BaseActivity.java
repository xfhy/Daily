package com.xfhy.androidbasiclibs.basekit.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.xfhy.androidbasiclibs.common.AppManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author feiyang
 * create at 2017/9/15 13:42
 * description：所有Activity的父类
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    private Unbinder mUnbinder;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());

        mContext = this;
        mUnbinder = ButterKnife.bind(this);
        AppManager.getInstance().addActivity(this);

        initView();
        initIntentData();
        initViewEvent();
        initData();
    }

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 设置该Activity的布局id
     */
    protected abstract int getContentViewResId();

    /**
     * 初始化Intent传递过来的数据
     */
    protected void initIntentData() {

    }

    /**
     * 初始化view的点击事件
     */
    protected abstract void initViewEvent();

    /**
     * 初始化自己的数据
     */
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        AppManager.getInstance().finishActivity(this);
    }

}
