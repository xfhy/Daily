package com.xfhy.daily.ui.fragment.zhihu;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xfhy.androidbasiclibs.basekit.fragment.BaseMVPFragment;
import com.xfhy.androidbasiclibs.common.util.DevicesUtils;
import com.xfhy.androidbasiclibs.common.util.SnackbarUtil;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseQuickAdapter;
import com.xfhy.androidbasiclibs.uihelper.widget.StatefulLayout;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.LatestDailyListBean;
import com.xfhy.daily.network.entity.zhihu.PastNewsBean;
import com.xfhy.daily.presenter.ZhihuDailyLatestContract;
import com.xfhy.daily.presenter.impl.ZhihuDailyLatestPresenter;
import com.xfhy.daily.ui.adapter.ZhihuLatestDailyAdapter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author feiyang
 * create at 2017/9/30 16:38
 * description：知乎最新日报fragment
 */
public class ZhihuLatestDailyFragment extends BaseMVPFragment<ZhihuDailyLatestPresenter>
        implements ZhihuDailyLatestContract.View, SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.sl_state_view)
    StatefulLayout mStateView;
    @BindView(R.id.srl_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_latest_daily_list)
    RecyclerView mDailyRecyclerView;
    private ZhihuLatestDailyAdapter mDailyAdapter;

    private int testDays = 1;

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
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_zhihu_latest_daily;
    }

    @Override
    protected void initView() {
        //下拉刷新颜色
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mDailyAdapter = new ZhihuLatestDailyAdapter(mActivity,
                null);
        // 开启RecyclerView动画
        mDailyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        // 循环播放动画
        mDailyAdapter.isFirstOnly(false);
        // 可加载更多
        mDailyAdapter.setEnableLoadMore(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mDailyRecyclerView.setLayoutManager(linearLayoutManager);
        mDailyRecyclerView.setAdapter(mDailyAdapter);
        //设置加载更多监听器
        mDailyAdapter.setOnLoadMoreListener(this, mDailyRecyclerView);
        // 当未满一屏幕时不刷新
        mDailyAdapter.disableLoadMoreIfNotFullPage();
        mDailyAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void lazyLoad() {
        mPresenter.reqDailyDataFromNet();
    }

    @Override
    public void showLatestData(LatestDailyListBean latestDailyListBean) {
        if (latestDailyListBean == null) {
            mStateView.showEmpty(R.string.stfEmptyMessage, R.string.stfButtonRetry);
            return;
        }
        mDailyAdapter.setNewData(latestDailyListBean.getStories());
    }

    @Override
    public void onLoading() {
        mStateView.showLoading();
    }

    @Override
    public void closeLoading() {
        mStateView.showContent();
    }

    @Override
    public void showContent() {
        closeRefresh();
        mStateView.showContent();
    }

    @Override
    public void showErrorMsg(String msg) {
        closeRefresh();
        SnackbarUtil.showBarLongTime(mDailyRecyclerView, msg, SnackbarUtil.ALERT);
    }

    @Override
    public void showEmptyView() {
        closeRefresh();
        mStateView.showEmpty(R.string.stfEmptyMessage, R.string.stfButtonRetry);
    }

    @Override
    public void showOffline() {
        closeRefresh();
        mStateView.showOffline(R.string.stfOfflineMessage, R.string.stfButtonSetting, new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                //未联网  跳转到设置界面
                DevicesUtils.goSetting(mActivity);
            }
        });
    }

    @Override
    public void loadMoreSuccess(String groupTitle, PastNewsBean pastNewsBean) {
        mDailyAdapter.loadMoreComplete();
        if (pastNewsBean == null) {
            return;
        }
        mDailyAdapter.addData(pastNewsBean.getStories());
    }

    @Override
    public void loadMoreFailed() {
        mDailyAdapter.loadMoreFail();
    }

    @Override
    public LifecycleTransformer<LatestDailyListBean> bindLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void onRefresh() {
        mPresenter.reqDailyDataFromNet();
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadMoreData(testDays++);
    }

    /**
     * 停止刷新
     */
    private void closeRefresh() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
