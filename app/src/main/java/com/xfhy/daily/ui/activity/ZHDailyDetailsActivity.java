package com.xfhy.daily.ui.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xfhy.androidbasiclibs.basekit.activity.BaseMvpActivity;
import com.xfhy.androidbasiclibs.util.DevicesUtils;
import com.xfhy.androidbasiclibs.util.GlideUtils;
import com.xfhy.androidbasiclibs.util.HtmlUtil;
import com.xfhy.androidbasiclibs.util.LoadDialogUtil;
import com.xfhy.androidbasiclibs.util.ShareUtil;
import com.xfhy.androidbasiclibs.util.SnackbarUtil;
import com.xfhy.androidbasiclibs.util.StringUtils;
import com.xfhy.daily.NewsApplication;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.DailyContentBean;
import com.xfhy.daily.presenter.ZHDailyDetailsContract;
import com.xfhy.daily.presenter.impl.ZHDailyDetailsPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author feiyang
 *         create 2017年11月7日11:14:19
 *         description 知乎最新日报详情页
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
    @BindView(R.id.nsv_scroller)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.wv_daily_content)
    WebView mWebView;
    @BindView(R.id.ll_bottom_view)
    LinearLayout mBottomView;
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

    private boolean isBottomShow = true;

    /**
     * 当前日报文章id
     */
    private int mDailyId = -1;

    @Override
    protected void initIntentData() {
        super.initIntentData();
        Intent intent = getIntent();
        //获取日报id
        if (intent != null) {
            this.mDailyId = intent.getIntExtra(DAILY_ID, -1);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        //从网络请求日报数据
        mPresenter.reqDailyContentFromNet(String.valueOf(mDailyId));
        mPresenter.reqDailyExtraInfoFromNet(String.valueOf(mDailyId));
        //判断当前文章是否被收藏
        mPresenter.isCollected(String.valueOf(mDailyId));
    }

    @Override
    public void onLoading() {
        LoadDialogUtil.show(mContext);
    }

    @Override
    public LifecycleTransformer bindLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void closeLoading() {
        LoadDialogUtil.dismiss();
    }

    @Override
    public void showErrorMsg(String msg) {
        SnackbarUtil.showBarLongTime(mWebView, msg, SnackbarUtil.ALERT);
    }

    @Override
    public void showEmptyView() {
        LoadDialogUtil.dismiss();
    }

    @Override
    public void showOffline() {
        setToolBar(mToolbar, "...");
        LoadDialogUtil.dismiss();
        SnackbarUtil.showBarLongTime(mWebView, StringUtils
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
        LoadDialogUtil.dismiss();
    }

    @Override
    public void initPresenter() {
        mPresenter = new ZHDailyDetailsPresenter(mContext);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        WebSettings settings = mWebView.getSettings();
        if (mPresenter.getNoImageState()) {
            //设置为无图模式
            settings.setBlockNetworkImage(true);
        }
        //判断用户是否设置了自动缓存
        if (mPresenter.getAutoCacheState()) {
            //设置是否应该启用应用程序缓存API。 默认值是false
            settings.setAppCacheEnabled(true);
            //设置是否启用DOM存储API。 默认值是false。
            settings.setDomStorageEnabled(true);
            //设置是否启用数据库存储API。 默认值是false。
            settings.setDatabaseEnabled(true);
            if (DevicesUtils.hasNetworkConnected(NewsApplication.getAppContext())) {
                //默认缓存使用模式。
                settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            } else {
                //不要使用网络，从缓存中加载。
                settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
            }
        }
        //是否启用JS
        settings.setJavaScriptEnabled(true);
        //设置自适应屏幕  缩小宽度以适合屏幕的内容
        settings.setLoadWithOverviewMode(true);
        //设置布局算法,将所有内容移动到视图宽度的一列中。
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //设置是否允许进行缩放
        settings.setSupportZoom(true);
        //设置在该WebView中加载网页
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_zh_daily_details;
    }

    @Override
    protected void initViewEvent() {
        //为使底部栏能滑动隐藏,直接监听NestedScrollView的滑动事件  向下滑则隐藏   上滑则显示
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int
                    oldScrollX, int oldScrollY) {

                //上滑 并且 正在显示底部栏
                if (scrollY - oldScrollY > 0 && isBottomShow) {
                    isBottomShow = false;
                    //将Y属性变为底部栏高度  (相当于隐藏了)
                    mBottomView.animate().translationY(mBottomView.getHeight());
                } else if (scrollY - oldScrollY < 0 && !isBottomShow) {
                    isBottomShow = true;
                    mBottomView.animate().translationY(0);
                }
            }
        });
    }

    @Override
    public void goToBack() {
        finish();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setExtraInfo(int likeCount, int commentCount) {
        tvDailyLikeCount.setText(String.format("%d个赞", likeCount));
        tvDailyCommentCount.setText(String.format("%d条评论", commentCount));
    }

    @Override
    public void loadSuccess(DailyContentBean dailyContentBean) {
        //标题
        setToolBar(mToolbar, dailyContentBean.getTitle());
        //顶部图片
        GlideUtils.loadCustomImage(mContext, dailyContentBean.getImage(), ivTopPicture);
        //图片来源
        tvImageSource.setText(dailyContentBean.getImageSource());
        //加载html
        String htmlData = HtmlUtil.INSTANCE.createHtmlData(dailyContentBean.getBody(),
                dailyContentBean.getCss(), dailyContentBean.getJs());
        mWebView.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
    }

    @Override
    public void loadError() {
        setToolBar(mToolbar, "...");
        LoadDialogUtil.dismiss();
        SnackbarUtil.showBarLongTime(mWebView, StringUtils
                        .getStringByResId(mContext, R.string.stfErrorMessageNormal), SnackbarUtil
                        .ALERT, StringUtils.getStringByResId(mContext, R.string.empty_view_retry),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.reqDailyContentFromNet(String.valueOf(mDailyId));
                        mPresenter.reqDailyExtraInfoFromNet(String.valueOf(mDailyId));
                    }
                });
    }

    @Override
    public void setCollectBtnSelState(boolean state) {
        btnLikeDaily.setSelected(state);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //这个是HomeAsUp按钮的id永远都是android.R.id.home
            case android.R.id.home:
                goToBack();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
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
    public void likeDaily() {
        SnackbarUtil.showBarShortTime(mNestedScrollView, "Sorry,暂未实现此功能", SnackbarUtil.CONFIRM);
    }

    @OnClick(R.id.tv_daily_comment_count)
    public void seeCommentDetails() {
        ZHCommentActivity.enterZHCommentActi(mContext, mDailyId, mPresenter.getCommentCount());
    }

    @OnClick(R.id.tv_daily_share)
    public void shareDaily() {
        DailyContentBean data = mPresenter.getData();
        if (data != null) {
            ShareUtil.INSTANCE.shareText(this, "我正在使用Daily,看到一篇文章很不错,分享给大家: " + data.getShareUrl());
        } else {
            SnackbarUtil.showBarShortTime(mWebView, "数据未加载成功,不能分享", SnackbarUtil.WARNING);
        }

    }

    @OnClick(R.id.fabtn_like_daily)
    public void btnLikeDaily() {
        mPresenter.collectArticle(String.valueOf(mDailyId));
        btnLikeDaily.setSelected(!btnLikeDaily.isSelected());
    }

}
