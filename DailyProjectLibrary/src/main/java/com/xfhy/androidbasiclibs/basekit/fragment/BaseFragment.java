package com.xfhy.androidbasiclibs.basekit.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author feiyang
 * create at 2017/9/15 13:43
 * description：所有的fragment父类
 */
public abstract class BaseFragment extends RxFragment {

    /**
     * 该fragment所对应的布局
     */
    protected View mRootView;
    private Unbinder mUnbinder;

    /**
     * 当前fragment所依附的Activity
     */
    protected FragmentActivity mActivity;
    /**
     * 视图是否已经初始化
     */
    protected boolean isInit = false;
    /**
     * 数据是否已经加载
     */
    protected boolean isLoad = false;
    /**
     * 是否已经执行完onCreate()
     * 子类在执行完OnCreate()时记得设置为true  如果需要用此标志
     */
    protected boolean isCreated = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        //需要返回页面布局   所有子类需要返回view
        mRootView = inflater.inflate(getLayoutResId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, mRootView);

        isInit = true;  //视图已加载
        isCanLoadData(); //初始化的时候去加载数据
        initArguments();
        initView();
        initViewEvent();
        initData();
    }

    /**
     * 视图是否已经对用户可见,系统的方法
     *
     * @param isVisibleToUser fragment对用用户是否可见了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //去看看是否可以加载数据了
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }

        //如果用户可见并且之前没有加载过数据  则去加载数据
        if (getUserVisibleHint() && !isLoad) {
            lazyLoad();
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /**
     * 方便获取传递过来的参数(可选)
     */
    protected void initArguments() {

    }

    /**
     * 初始化view的点击事件
     */
    protected void initViewEvent(){}

    /**
     * 设置布局数据
     */
    protected void initData() {

    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     * 在加载完数据之后需要把isLoad置true
     */
    protected void lazyLoad() {
        isLoad = true;
    }

    /**
     * 设置布局的id
     *
     * @return 返回子类布局id
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 当视图已经对用户不可见并且加载过数据,如果需要在切换到其他页面时停止加载,则可以重写该方法
     */
    protected void stopLoad() {
    }

}
