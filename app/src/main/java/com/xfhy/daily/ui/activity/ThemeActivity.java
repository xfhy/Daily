package com.xfhy.daily.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xfhy.androidbasiclibs.basekit.activity.BaseMvpActivity;
import com.xfhy.androidbasiclibs.common.util.DevicesUtils;
import com.xfhy.androidbasiclibs.common.util.GlideUtils;
import com.xfhy.androidbasiclibs.common.util.SnackbarUtil;
import com.xfhy.androidbasiclibs.common.util.StringUtils;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseQuickAdapter;
import com.xfhy.androidbasiclibs.uihelper.widget.StatefulLayout;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.ThemeDailyDetailsBean;
import com.xfhy.daily.presenter.ZHThemeDetailsContract;
import com.xfhy.daily.presenter.impl.ZHThemeDetailsPresenter;
import com.xfhy.daily.ui.adapter.ZHThemeDetailsAdapter;

import java.util.List;

import butterknife.BindView;

public class ThemeActivity extends BaseMvpActivity<ZHThemeDetailsContract.Presenter> implements
        ZHThemeDetailsContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter
        .OnItemClickListener {

    private static final String THEME_ID = "theme_id";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.sfl_state_view)
    StatefulLayout mStateView;
    @BindView(R.id.cl_theme_top_des)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.abl_daily_details)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.iv_theme_background)
    ImageView ivThemeBackground;
    @BindView(R.id.tv_theme_description)
    TextView tvThemeDescription;
    @BindView(R.id.rv_th_details_list)
    RecyclerView rvThDetailsList;
    @BindView(R.id.sfl_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    /**
     * 当前日报类别的id
     */
    private int mThemeId = -1;
    private ZHThemeDetailsAdapter mThemeDetailsAdapter;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_theme;
    }

    @Override
    protected void initView() {
        //设置默认标题栏文字
        setToolBar(mToolbar, "...");
        //下拉刷新颜色
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mThemeDetailsAdapter = new ZHThemeDetailsAdapter(R.layout
                .item_zh_theme_details, null, mContext);
        rvThDetailsList.setLayoutManager(new LinearLayoutManager(this));
        rvThDetailsList.setAdapter(mThemeDetailsAdapter);

        //动画
        mThemeDetailsAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        // 循环播放动画
        mThemeDetailsAdapter.isFirstOnly(false);

        mRefreshLayout.setOnRefreshListener(this);
        mThemeDetailsAdapter.setOnItemClickListener(this);

        //为解决SwipeRefreshLayout和RecyclerView冲突  详情见项目doc文档
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                /*
                * 如果竖直的偏移大于0,则设置SwipeRefreshLayout为可用.
                    当我在AppBarLayout可见时,手机由下往上滑动时,verticalOffset<0;
                    当手指由上往下,滑动到顶部,这时手指再由上往下滑动时,verticalOffset=0,
                    这时可以设置SwipeRefreshLayout为可用了.
                * */
                mRefreshLayout.setEnabled(verticalOffset >= 0);
            }
        });

    }

    @Override
    protected void initIntentData() {
        super.initIntentData();
        Intent intent = getIntent();
        if (intent != null) {
            mThemeId = intent.getIntExtra(THEME_ID, -1);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.reqDataFromNet(String.valueOf(mThemeId));
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
    public void showOffline() {
        closeRefresh();
        closeLoading();
        SnackbarUtil.showBarLongTime(mStateView, StringUtils
                        .getStringByResId(mContext, R.string.stfOfflineMessage), SnackbarUtil
                        .WARNING, StringUtils.getStringByResId(mContext, R.string.stfButtonSetting),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //未联网  跳转到设置界面
                        DevicesUtils.goSetting(mContext);
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
        mPresenter = new ZHThemeDetailsPresenter(mContext);
    }

    @Override
    public void loadSuccess(ThemeDailyDetailsBean themeDailyDetailsBean) {
        closeRefresh();
        mStateView.showContent();
        setToolBar(mToolbar, themeDailyDetailsBean.getName());
        mThemeDetailsAdapter.replaceData(themeDailyDetailsBean.getStories());
        GlideUtils.loadCustomImage(mContext, themeDailyDetailsBean.getBackground(),
                ivThemeBackground);
        tvThemeDescription.setText(themeDailyDetailsBean.getDescription());
    }

    @Override
    public int getmThemeId() {
        return mThemeId;
    }

    @Override
    protected void initViewEvent() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 进入知乎主题详情 Activity
     *
     * @param context Context
     * @param id      日报id
     */
    public static void enterZHThemeDetailsActi(Context context, int id) {
        Intent intent = new Intent(context, ThemeActivity.class);
        intent.putExtra(THEME_ID, id);
        context.startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mPresenter.reqDataFromNet(String.valueOf(mThemeId));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //进入日报详情页
        ThemeDailyDetailsBean themeDailyDetailsBean = mPresenter.getData();
        if (themeDailyDetailsBean != null) {
            List<ThemeDailyDetailsBean.StoriesBean> stories = themeDailyDetailsBean.getStories();
            if (stories != null && stories.size() > position) {
                ZHDailyDetailsActivity.enterZHDailyDetailsActi(mContext, stories.get(position)
                        .getId());
            }
        }
    }
}
