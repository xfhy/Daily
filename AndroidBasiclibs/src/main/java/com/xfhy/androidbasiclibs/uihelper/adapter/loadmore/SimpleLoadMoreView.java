package com.xfhy.androidbasiclibs.uihelper.adapter.loadmore;


import com.xfhy.androidbasiclibs.R;

/**
 * author xingyun
 * create at 2017/9/26 16:54
 * description：简单的加载更多
 */
public final class SimpleLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.quick_view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
