package com.xfhy.daily.ui.fragment.zhihu;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xfhy.androidbasiclibs.basekit.fragment.BaseMVPFragment;
import com.xfhy.androidbasiclibs.common.util.DevicesUtils;
import com.xfhy.androidbasiclibs.common.util.SnackbarUtil;
import com.xfhy.androidbasiclibs.common.util.StringUtils;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseQuickAdapter;
import com.xfhy.androidbasiclibs.uihelper.widget.StatefulLayout;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.ColumnDailyBean;
import com.xfhy.daily.presenter.ZHSectionContract;
import com.xfhy.daily.presenter.impl.ZHSectionPresenter;
import com.xfhy.daily.ui.activity.MainActivity;
import com.xfhy.daily.ui.activity.ZHSectionDetailsActivity;
import com.xfhy.daily.ui.adapter.ZHSectionAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * @author feiyang
 *         time create at 2017/11/22 15:04
 *         description 知乎专栏fragment
 */
public class ZHSectionFragment extends BaseStateMVPFragment<ZHSectionPresenter> implements
        ZHSectionContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter
        .OnItemClickListener {


    @BindView(R.id.rv_common_list)
    RecyclerView mSectionRv;
    @BindView(R.id.srl_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private ZHSectionAdapter mSectionAdapter;

    public static ZHSectionFragment newInstance() {

        Bundle args = new Bundle();

        ZHSectionFragment fragment = new ZHSectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_refresh_common_list;
    }

    @Override
    protected void initView() {

        //设置标题
        if (mActivity != null && mActivity instanceof MainActivity) {
            MainActivity activity = (MainActivity) mActivity;
            activity.setToolBar(StringUtils.getStringByResId(activity, R.string.zh_section));
        }

        //下拉刷新颜色
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mSectionAdapter = new ZHSectionAdapter(R.layout.item_zh_section, null, mActivity);
        //循环 动画
        mSectionAdapter.openLoadAnimation();
        mSectionAdapter.isFirstOnly(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mSectionRv.setLayoutManager(linearLayoutManager);
        mSectionRv.setAdapter(mSectionAdapter);

        mRefreshLayout.setOnRefreshListener(this);
        mSectionAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        mPresenter.reqDataFromNet();
    }

    @Override
    public void loadSuccess(List<ColumnDailyBean.DataBean> dataBeans) {
        closeRefresh();
        mStateView.showContent();
        mSectionAdapter.replaceData(dataBeans);
    }

    @Override
    public void closeRefresh() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void initPresenter() {
        mPresenter = new ZHSectionPresenter(mActivity);
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshData();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ZHSectionDetailsActivity.enterZHSectionDetailsActi(mActivity, mPresenter.getSectionId
                (position), mPresenter.getSectionTitle(position));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //设置标题
            if (mActivity != null && mActivity instanceof MainActivity) {
                MainActivity activity = (MainActivity) mActivity;
                activity.setToolBar(StringUtils.getStringByResId(activity, R.string.zh_section));
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

}
