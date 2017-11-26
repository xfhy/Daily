package com.xfhy.daily.ui.fragment.zhihu;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
public class ZHThemeFragment extends BaseMVPFragment<ZHThemePresenter> implements ZHThemeContract
        .View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener {

    /**
     * 主题列表列数
     */
    private static final int SPAN_COUNT = 2;

    @BindView(R.id.srl_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_common_list)
    RecyclerView mThemeList;
    @BindView(R.id.sfl_state_view)
    StatefulLayout mStateView;

    private ZHThemeAdapter mThemeAdapter;

    public static ZHThemeFragment newInstance() {

        Bundle args = new Bundle();

        ZHThemeFragment fragment = new ZHThemeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_zh_theme;
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
    public void onLoading() {
        mStateView.showLoading();
    }


    @Override
    public LifecycleTransformer bindLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void closeLoading() {
        mStateView.showContent();
    }

    @Override
    public void showErrorMsg(String msg) {
        closeLoading();
        SnackbarUtil.showBarLongTime(mStateView, msg, SnackbarUtil.ALERT);
    }

    @Override
    public void showEmptyView() {
        closeRefresh();
        mStateView.showEmpty(R.string.stfEmptyMessage, R.string.stfButtonRetry);
    }

    private void closeRefresh() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadSuccess(List<TopicDailyListBean.OthersBean> othersBeans) {
        closeRefresh();
        mStateView.showContent();
        mThemeAdapter.replaceData(othersBeans);
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
    public void showContent() {
        closeRefresh();
        mStateView.showContent();
    }

    @Override
    public void initPresenter() {
        mPresenter = new ZHThemePresenter(mActivity);
    }

    @Override
    protected void initViewEvent() {

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
