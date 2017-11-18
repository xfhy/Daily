package com.xfhy.daily.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xfhy.androidbasiclibs.basekit.activity.BaseMvpActivity;
import com.xfhy.androidbasiclibs.common.util.DevicesUtils;
import com.xfhy.androidbasiclibs.common.util.SnackbarUtil;
import com.xfhy.androidbasiclibs.common.util.StringUtils;
import com.xfhy.androidbasiclibs.common.util.ToastUtil;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseQuickAdapter;
import com.xfhy.androidbasiclibs.uihelper.widget.StatefulLayout;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.DailyCommentBean;
import com.xfhy.daily.presenter.ZHCommentContract;
import com.xfhy.daily.presenter.impl.ZHCommentPresenter;
import com.xfhy.daily.ui.adapter.ZHCommentAdapter;

import java.util.List;

import butterknife.BindView;

public class ZHCommentActivity extends BaseMvpActivity<ZHCommentContract.Presenter> implements
        ZHCommentContract.View, BaseQuickAdapter.OnItemClickListener {

    private static final String DAILY_ID = "daily_id";
    private static final String COMMENT_COUNT = "comment_count";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.sl_state_view)
    StatefulLayout mStateView;
    @BindView(R.id.rv_daily_comment)
    RecyclerView rvDailyComment;

    /**
     * 当前日报文章id
     */
    private int mDailyId = -1;
    /**
     * 评论总数
     */
    private int mCommentCount = 0;
    private ZHCommentAdapter mCommentAdapter;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_zh_comment;
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void initIntentData() {
        super.initIntentData();
        Intent intent = getIntent();
        //获取日报id
        if (intent != null) {
            this.mDailyId = intent.getIntExtra(DAILY_ID, -1);
            this.mCommentCount = intent.getIntExtra(COMMENT_COUNT, 0);
        }
        setToolBar(mToolbar, String.format("%d条评论", mCommentCount));
    }

    @Override
    protected void initView() {
        //初始化adapter
        mCommentAdapter = new ZHCommentAdapter(mContext, null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvDailyComment.setLayoutManager(linearLayoutManager);
        rvDailyComment.setAdapter(mCommentAdapter);

        //设置RecyclerView分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvDailyComment
                .getContext(),
                linearLayoutManager.getOrientation());
        rvDailyComment.addItemDecoration(dividerItemDecoration);

        // 设置RecyclerView的item监听
        mCommentAdapter.setOnItemClickListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.reqLongComFromNet(String.valueOf(mDailyId));
    }

    @Override
    protected void initViewEvent() {

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
    }

    @Override
    public void showErrorMsg(String msg) {
        mStateView.showContent();
        SnackbarUtil.showBarShortTime(rvDailyComment, msg, SnackbarUtil.WARNING);
    }

    @Override
    public void showEmptyView() {
    }

    @Override
    public void showOffline() {
        setToolBar(mToolbar, "...");
        mStateView.showOffline(StringUtils.getStringByResId(mContext, R.string.stfOfflineMessage),
                StringUtils.getStringByResId(mContext, R.string.stfButtonSetting), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //未联网  跳转到设置界面
                        DevicesUtils.goSetting(mContext);
                    }
                });
        SnackbarUtil.showBarLongTime(rvDailyComment, StringUtils
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
    }

    @Override
    public void initPresenter() {
        mPresenter = new ZHCommentPresenter(mContext);
    }

    @Override
    public void loadCommentSuccess(List<DailyCommentBean.CommentsBean> commentsBean) {
        mStateView.showContent();
        mCommentAdapter.addData(commentsBean);
    }

    @Override
    public void loadCommentError(String errorMsg) {
        SnackbarUtil.showBarShortTime(rvDailyComment, errorMsg, SnackbarUtil.WARNING);
    }

    /**
     * 进入知乎日报评论页 Activity
     *
     * @param context      Context
     * @param id           日报id
     * @param commentCount 评论总数
     */
    public static void enterZHCommentActi(Context context, int id, int commentCount) {
        Intent intent = new Intent(context, ZHCommentActivity.class);
        intent.putExtra(DAILY_ID, id);
        intent.putExtra(COMMENT_COUNT, commentCount);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ToastUtil.showMessage(mContext, "点我了...");
    }
}
