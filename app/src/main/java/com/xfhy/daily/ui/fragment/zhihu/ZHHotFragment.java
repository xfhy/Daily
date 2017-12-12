package com.xfhy.daily.ui.fragment.zhihu;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xfhy.androidbasiclibs.util.StringUtils;
import com.xfhy.androidbasiclibs.adapter.BaseQuickAdapter;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.HotDailyBean;
import com.xfhy.daily.presenter.ZHHotContract;
import com.xfhy.daily.presenter.impl.ZHHotPresenter;
import com.xfhy.daily.ui.activity.MainActivity;
import com.xfhy.daily.ui.activity.ZHDailyDetailsActivity;
import com.xfhy.daily.ui.adapter.ZHHotAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * @author xfhy
 *         create at 2017/11/26 16:21
 *         description：知乎最热文章fragment
 */
public class ZHHotFragment extends BaseStateMVPFragment<ZHHotContract.Presenter> implements
        ZHHotContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter
        .OnItemClickListener {

    @BindView(R.id.rv_common_list)
    RecyclerView mHotList;
    @BindView(R.id.srl_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private ZHHotAdapter mHotAdapter;

    public static ZHHotFragment newInstance() {

        Bundle args = new Bundle();

        ZHHotFragment fragment = new ZHHotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_refresh_common_list;
    }

    @Override
    protected void initView() {
        //下拉刷新颜色
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mHotAdapter = new ZHHotAdapter(R.layout.item_zh_common_daily_list, null);
        mHotAdapter.openLoadAnimation();
        mHotAdapter.isFirstOnly(false);
        mHotList.setLayoutManager(new LinearLayoutManager(mActivity));
        mHotList.setAdapter(mHotAdapter);

        mRefreshLayout.setOnRefreshListener(this);
        mHotAdapter.setOnItemClickListener(this);
    }

    @Override
    public void initPresenter() {
        mPresenter = new ZHHotPresenter(mActivity);
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        mPresenter.reqDataFromDB();
    }

    @Override
    public void closeRefresh() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadSuccess(List<HotDailyBean.RecentBean> dataBeans) {
        closeRefresh();
        mStateView.showContent();
        mHotAdapter.replaceData(dataBeans);
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshData();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ZHDailyDetailsActivity.enterZHDailyDetailsActi(mActivity, mPresenter.getDailyId(position));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //设置标题
            if (mActivity != null && mActivity instanceof MainActivity) {
                MainActivity activity = (MainActivity) mActivity;
                activity.setToolBar(StringUtils.getStringByResId(activity, R.string.zh_hot));
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

}
