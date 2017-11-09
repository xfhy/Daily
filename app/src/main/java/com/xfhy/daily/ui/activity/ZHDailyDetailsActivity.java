package com.xfhy.daily.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xfhy.androidbasiclibs.basekit.activity.BaseMvpActivity;
import com.xfhy.androidbasiclibs.common.util.DevicesUtils;
import com.xfhy.androidbasiclibs.common.util.SnackbarUtil;
import com.xfhy.androidbasiclibs.common.util.ToastUtil;
import com.xfhy.androidbasiclibs.uihelper.widget.StatefulLayout;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.DailyContentBean;
import com.xfhy.daily.presenter.ZHDailyDetailsContract;
import com.xfhy.daily.presenter.impl.ZHDailyDetailsPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author feiyang
 * @create 2017年11月7日11:14:19
 * @description 知乎最新日报详情页
 */
public class ZHDailyDetailsActivity extends BaseMvpActivity<ZHDailyDetailsContract.Presenter>
        implements ZHDailyDetailsContract.View {

    private static final String DAILY_ID = "daily_id";

    @BindView(R.id.iv_top_picture)
    ImageView ivTopPicture;
    @BindView(R.id.tv_image_source)
    TextView tvImageSource;
    @BindView(R.id.toolbar_daily_details)
    Toolbar mToolbar;
    @BindView(R.id.abl_daily_details)
    AppBarLayout ablDailyDetails;
    @BindView(R.id.wv_daily_content)
    WebView mWebView;
    @BindView(R.id.sl_state_content_view)
    StatefulLayout mStateView;
    @BindView(R.id.fabtn_like_daily)
    FloatingActionButton btnLikeDaily;
    @BindView(R.id.ll_daily_bottom_view)
    LinearLayout llDailyBottomView;
    @BindView(R.id.tv_daily_like_count)
    TextView tvDailyLikeCount;
    @BindView(R.id.tv_daily_comment_count)
    TextView tvDailyCommentCount;
    @BindView(R.id.tv_daily_share)
    TextView tvDailyShare;

    /**
     * 当前日报文章id
     */
    private int dailyId = -1;

    @Override
    protected void initIntentData() {
        super.initIntentData();
        Intent intent = getIntent();
        if (intent != null) {
            this.dailyId = intent.getIntExtra(DAILY_ID, -1);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.reqDailyContentFromNet(String.valueOf(dailyId));
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
        SnackbarUtil.showBarLongTime(mStateView, msg, SnackbarUtil.ALERT);
    }

    @Override
    public void showEmptyView() {
        mStateView.showEmpty(R.string.load_failed, R.string.stfButtonRetry);
    }

    @Override
    public void showOffline() {
        mStateView.showOffline(R.string.stfOfflineMessage, R.string.stfButtonSetting, new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                //未联网  跳转到设置界面
                DevicesUtils.goSetting(mContext);
            }
        });
    }

    @Override
    public void showContent() {
        mStateView.showContent();
    }

    @Override
    public void initPresenter() {
        mPresenter = new ZHDailyDetailsPresenter(mContext);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_zh_daily_details;
    }

    @Override
    protected void initViewEvent() {

    }

    @Override
    public void goToBack() {

    }

    @Override
    public void loadTopPicture(String url) {

    }

    @Override
    public void setImageSource(String source) {

    }

    @Override
    public void setExtraInfo(int likeCount, int commentCount) {

    }

    @Override
    public void loadUrl(String url) {

    }

    @Override
    public void loadSuccess(DailyContentBean dailyContentBean) {

    }

    @Override
    public void loadError() {
        mStateView.showEmpty(R.string.load_failed, R.string.stfButtonRetry);
    }

    /**
     * 进入知乎日报详情 Activity
     *
     * @param context Context
     * @param id      日报id
     */
    public static void enterZHDailyDetailsActi(Context context, int id) {
        Intent intent = new Intent(context, ZHDailyDetailsActivity.class);
        intent.putExtra(DAILY_ID, id);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_daily_like_count)
    public void likeDaily(){
        ToastUtil.showMessage(mContext,"likeDaily");
    }

    @OnClick(R.id.tv_daily_comment_count)
    public void seeCommentDetails(){
        ToastUtil.showMessage(mContext,"seeCommentDetails");
    }

    @OnClick(R.id.tv_daily_share)
    public void shareDaily(){
        ToastUtil.showMessage(mContext,"shareDaily");
    }

}
