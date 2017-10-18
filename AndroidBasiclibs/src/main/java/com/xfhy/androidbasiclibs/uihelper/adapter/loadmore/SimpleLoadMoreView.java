package com.xfhy.androidbasiclibs.uihelper.adapter.loadmore;

import com.xfhy.androidbasiclibs.R;

/**
 * Created by Leezp on 2017/10/18 0018.
 */

public class SimpleLoadMoreView extends LoadMoreView {
    @Override
    protected int getLoadEndViewId() {
        return R.id.loading_more_end_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.loading_more_fail_view;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.quick_view_load_more;
    }
}
