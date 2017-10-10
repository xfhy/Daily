package com.xfhy.daily.ui.fragment.zhihu;

import android.os.Bundle;

import com.xfhy.androidbasiclibs.basekit.fragment.BaseMVPFragment;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.LatestDailyListBean;
import com.xfhy.daily.presenter.ZhihuDailyLatestContract;
import com.xfhy.daily.presenter.impl.ZhihuDailyLatestPresenter;

/**
 * author feiyang
 * create at 2017/9/30 16:38
 * description：知乎最新日报fragment
 */
public class ZhihuLatestDailyFragment extends BaseMVPFragment<ZhihuDailyLatestPresenter>
        implements ZhihuDailyLatestContract.View {

    public static ZhihuLatestDailyFragment newInstance() {

        Bundle args = new Bundle();

        ZhihuLatestDailyFragment fragment = new ZhihuLatestDailyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ZhihuDailyLatestPresenter(mActivity);
    }

    @Override
    protected void initViewEvent() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_zhihu_latest_daily;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void refreshSuccess(LatestDailyListBean latestDailyListBean) {

    }

    @Override
    public void refreshError(String error) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public void showErrorMsg(String msg) {

    }
}
