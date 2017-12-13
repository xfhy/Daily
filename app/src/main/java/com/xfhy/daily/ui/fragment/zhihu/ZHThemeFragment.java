package com.xfhy.daily.ui.fragment.zhihu;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xfhy.androidbasiclibs.util.StringUtils;
import com.xfhy.androidbasiclibs.adapter.BaseQuickAdapter;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.TopicDailyListBean;
import com.xfhy.daily.presenter.ZHThemeContract;
import com.xfhy.daily.presenter.impl.ZHThemePresenter;
import com.xfhy.daily.ui.activity.MainActivity;
import com.xfhy.daily.ui.activity.ThemeActivity;
import com.xfhy.daily.ui.adapter.ZHThemeAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * @author xfhy
 *         create at 2017/11/18 23:30
 *         description：知乎主题列表
 */
public class ZHThemeFragment extends BaseStateMVPFragment<ZHThemePresenter> implements ZHThemeContract
        .View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener {

    /**
     * 主题列表列数
     */
    private static final int SPAN_COUNT = 2;

    @BindView(R.id.srl_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_common_list)
    RecyclerView mThemeList;

    private ZHThemeAdapter mThemeAdapter;

    public static ZHThemeFragment newInstance() {

        Bundle args = new Bundle();

        ZHThemeFragment fragment = new ZHThemeFragment();
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

        mThemeAdapter = new ZHThemeAdapter(R.layout.item_zh_theme, null, mActivity);
        mThemeAdapter.openLoadAnimation();
        mThemeAdapter.isFirstOnly(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, SPAN_COUNT);
        mThemeList.setLayoutManager(gridLayoutManager);
        mThemeList.setAdapter(mThemeAdapter);

        mRefreshLayout.setOnRefreshListener(this);
        mThemeAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        mPresenter.reqDataFromNet();
    }

    @Override
    public void closeRefresh() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadSuccess(List<TopicDailyListBean.OthersBean> othersBeans) {
        closeRefresh();
        mStateView.showContent();
        mThemeAdapter.replaceData(othersBeans);
    }

    @Override
    public void initPresenter() {
        mPresenter = new ZHThemePresenter();
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshData();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<TopicDailyListBean.OthersBean> othersBeans = mPresenter.getData();
        if (othersBeans != null && othersBeans.size() > position) {
            ThemeActivity.enterZHThemeDetailsActi(mActivity, othersBeans.get(position).getId());
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //设置标题
            if (mActivity != null && mActivity instanceof MainActivity) {
                MainActivity activity = (MainActivity) mActivity;
                activity.setToolBar(StringUtils.getStringByResId(activity, R.string.zh_theme));
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
