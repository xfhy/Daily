package com.xfhy.androidbasiclibs.uihelper.adapter.loadmore;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

import com.xfhy.androidbasiclibs.uihelper.adapter.BaseViewHolder;

/**
 * Created by Leezp on 2017/10/18 0018.
 */

public abstract class LoadMoreView {

    /**
     * 加载更多视图的四种状态
     * 1. 默认
     * 2. 加载
     * 3. 失败
     * 4. 加载已到最底部
     */
    public static final int STATUS_DEFAULT = 1;
    public static final int STATUS_LOADING = 2;
    public static final int STATUS_FAIL = 3;
    public static final int STATUS_END = 4;

    //加载视图默认状态
    private int mLoadMoreStatus = STATUS_DEFAULT;
    //加载已到最底部视图是否显示
    private boolean mLoadMoreEndGone = false;

    /**
     * 获取当前加载更多视图的状态
     *
     * @return
     */
    public int getmLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    /**
     * 设置当前加载更多视图的状态
     *
     * @param mLoadMoreStatus
     */
    public void setmLoadMoreStatus(int mLoadMoreStatus) {
        this.mLoadMoreStatus = mLoadMoreStatus;
    }

    /**
     * 通过调用这个方法实现显示加载更多布局
     *
     * @param holder
     */
    public void convert(BaseViewHolder holder) {
        switch (mLoadMoreStatus) {
            case STATUS_LOADING:
                visibleLoading(holder, true);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, false);
                break;
            case STATUS_FAIL:
                visibleLoading(holder, false);
                visibleLoadFail(holder, true);
                visibleLoadEnd(holder, false);
                break;
            case STATUS_END:
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, true);
                break;
            case STATUS_DEFAULT:
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, false);
                break;
        }
    }

    /**
     * 设置加载到数据最底部时，是否显示
     * @param holder
     * @param visible
     */
    private void visibleLoadEnd(BaseViewHolder holder, boolean visible) {
        final int loadEndViewId = getLoadEndViewId();
        if (loadEndViewId != 0) {
            holder.setVisible(loadEndViewId, visible);
        }
    }

    /**
     * 获取加载到数据最底部的视图id
     * @return
     */
    protected abstract @IdRes int getLoadEndViewId();


    /**
     * 设置加载失败的视图是否显示
     * @param holder
     * @param visible
     */
    private void visibleLoadFail(BaseViewHolder holder, boolean visible) {
        holder.setVisible(getLoadFailViewId(), visible);
    }

    /**
     * 获取加载失败的视图id
     * @return
     */
    protected abstract @IdRes int getLoadFailViewId();

    /**
     * 设置正在加载的视图是否显示
     * @param holder
     * @param visible
     */
    private void visibleLoading(BaseViewHolder holder, boolean visible) {
        holder.setVisible(getLoadingViewId(), visible);
    }

    /**
     * 获取正在加载的视图id
     * @return
     */
    protected abstract @IdRes int getLoadingViewId();

    /**
     * 获取加载更多的布局
     * @return
     */
    public abstract @LayoutRes int getLayoutId();

    /**
     * 返回加载更多到最底部视图是否显示
     *
     * Deprecated是指方法已过时或者不建议重写该方法
     *
     * @return
     */
    @Deprecated
    public boolean ismLoadMoreEndGone() {
        return mLoadMoreEndGone;
    }

    /**
     * 设置加载更多到最底部视图的值
     *
     * @param mLoadMoreEndGone
     */
    public void setmLoadMoreEndGone(boolean mLoadMoreEndGone) {
        this.mLoadMoreEndGone = mLoadMoreEndGone;
    }

    /**
     * 返回加载更多到最底部视图是否显示
     *
     * @return
     */
    public final boolean ismLoadEndMoreGone() {
        if (getLoadEndViewId() == 0) {
            return true;
        }
        return mLoadMoreEndGone;
    }
}
