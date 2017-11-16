package com.xfhy.daily.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xfhy.androidbasiclibs.basekit.activity.BaseMvpActivity;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.DailyCommentBean;
import com.xfhy.daily.presenter.ZHCommentContract;
import com.xfhy.daily.presenter.impl.ZHCommentPresenter;

import java.util.List;

import butterknife.BindView;

public class ZHCommentActivity extends BaseMvpActivity<ZHCommentContract.Presenter> implements
        ZHCommentContract.View {

    private static final String DAILY_ID = "daily_id";
    private static final String COMMENT_COUNT = "comment_count";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
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

    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.reqLongComFromNet(String.valueOf(4232852));
        mPresenter.reqShortComFromNet(String.valueOf(4232852));
    }

    @Override
    public void loadShortComError(String errorMsg) {

    }

    @Override
    protected void initViewEvent() {

    }

    @Override
    public void onLoading() {

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

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showOffline() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void initPresenter() {
        mPresenter = new ZHCommentPresenter(mContext);
    }

    @Override
    public void loadLongComSuccess(List<DailyCommentBean.CommentsBean> commentsBean) {

    }

    @Override
    public void loadLongComError(String errorMsg) {

    }

    @Override
    public void loadShortComSuccess(List<DailyCommentBean.CommentsBean> commentsBean) {

    }

    @Override
    public void loadingShortCom() {

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
}
