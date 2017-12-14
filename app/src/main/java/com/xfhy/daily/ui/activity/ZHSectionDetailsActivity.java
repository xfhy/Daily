package com.xfhy.daily.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xfhy.androidbasiclibs.basekit.activity.BaseMvpActivity;
import com.xfhy.androidbasiclibs.util.DevicesUtils;
import com.xfhy.androidbasiclibs.util.SnackbarUtil;
import com.xfhy.androidbasiclibs.util.StringUtils;
import com.xfhy.androidbasiclibs.adapter.BaseQuickAdapter;
import com.xfhy.androidbasiclibs.widget.StatefulLayout;
import com.xfhy.daily.R;
import com.xfhy.daily.model.bean.ColumnDailyDetailsBean;
import com.xfhy.daily.presenter.ZHSectionDetailsContract;
import com.xfhy.daily.presenter.impl.ZHSectionDetailsPresenter;
import com.xfhy.daily.ui.adapter.ZHSectionDetailsAdapter;

import java.util.List;

import butterknife.BindView;

public class ZHSectionDetailsActivity extends BaseMvpActivity<ZHSectionDetailsPresenter>
        implements ZHSectionDetailsContract.View, SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.OnItemClickListener {

    private final static String SECTION_ID = "section_id";
    private final static String SECTION_NAME = "section_name";


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_common_list)
    RecyclerView rvDailyList;
    @BindView(R.id.sfl_state_view)
    StatefulLayout mStateView;
    @BindView(R.id.srl_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    /**
     * 专栏id
     */
    private String mSectionId;
    private ZHSectionDetailsAdapter mSectionsAdapter;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_zh_section_details;
    }

    @Override
    protected void initView() {
        //设置刷新颜色
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSectionsAdapter = new ZHSectionDetailsAdapter(R.layout
                .item_zh_common_daily_list, null, mContext);
        mSectionsAdapter.openLoadAnimation();
        // 循环播放动画
        mSectionsAdapter.isFirstOnly(false);
        rvDailyList.setLayoutManager(new LinearLayoutManager(this));
        rvDailyList.setAdapter(mSectionsAdapter);

        mRefreshLayout.setOnRefreshListener(this);
        mSectionsAdapter.setOnItemClickListener(this);

    }

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mSectionId = String.valueOf(intent.getIntExtra(SECTION_ID, -1));
            String sectionName = intent.getStringExtra(SECTION_NAME);
            setToolBar(mToolbar, sectionName);
        } else {
            setToolBar(mToolbar, "...");
        }
    }

    @Override
    protected void initViewEvent() {
    }

    @Override
    public void initPresenter() {
        mPresenter = new ZHSectionDetailsPresenter();
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.reqDataFromNet(mSectionId);
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
    public void loadSuccess(List<ColumnDailyDetailsBean.StoriesBean> dataBeans) {
        closeRefresh();
        mStateView.showContent();
        mSectionsAdapter.replaceData(dataBeans);
    }

    @Override
    public String getSectionId() {
        return mSectionId;
    }

    /**
     * 进入知乎专栏详情列表 Activity
     *
     * @param context   Context
     * @param sectionId 日报id
     */
    public static void enterZHSectionDetailsActi(Context context, int sectionId, String
            sectionName) {
        Intent intent = new Intent(context, ZHSectionDetailsActivity.class);
        intent.putExtra(SECTION_NAME, sectionName);
        intent.putExtra(SECTION_ID, sectionId);
        context.startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshData(mSectionId);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ZHDailyDetailsActivity.enterZHDailyDetailsActi(mContext, mPresenter.getDailyId(position));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
