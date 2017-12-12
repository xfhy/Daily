package com.xfhy.androidbasiclibs.basekit.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

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
    /**
     * 当前Activity
     */
    protected Context mContext;
    /**
     * 标记当前界面是否是MVP
     */
    protected boolean isMVP = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());

        mUnbinder = ButterKnife.bind(this);
        mContext = this;
        AppManager.getInstance().addActivity(this);

        if (!isMVP) {
            doOnCreate();
        }
    }

    /**
     * 初始化
     */
    protected void doOnCreate() {
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

    /**
     * 设置toolbar的标题
     *
     * @param mToolbar Toolbar
     * @param title    标题
     */
    protected void setToolBar(Toolbar mToolbar, String title) {
        //setSupportActionBar之前设置标题
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            //让导航按钮显示出来
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            //设置导航按钮图标
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
    }

}
