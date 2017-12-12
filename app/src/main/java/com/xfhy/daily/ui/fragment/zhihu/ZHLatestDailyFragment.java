package com.xfhy.daily.ui.fragment.zhihu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xfhy.androidbasiclibs.util.DensityUtil;
import com.xfhy.androidbasiclibs.util.GlideUtils;
import com.xfhy.androidbasiclibs.util.StringUtils;
import com.xfhy.androidbasiclibs.adapter.BaseQuickAdapter;
import com.xfhy.androidbasiclibs.widget.EasyBanner;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.LatestDailyListBean;
import com.xfhy.daily.network.entity.zhihu.PastNewsBean;
import com.xfhy.daily.presenter.ZHDailyLatestContract;
import com.xfhy.daily.presenter.impl.ZHDailyLatestPresenter;
import com.xfhy.daily.ui.activity.MainActivity;
import com.xfhy.daily.ui.activity.ZHDailyDetailsActivity;
import com.xfhy.daily.ui.adapter.ZHLatestDailyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author feiyang
 * create at 2017/9/30 16:38
 * description：知乎最新日报fragment
 */
public class ZHLatestDailyFragment extends BaseStateMVPFragment<ZHDailyLatestPresenter>
        implements ZHDailyLatestContract.View, SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
        EasyBanner.OnItemClickListener, ZHLatestDailyAdapter.HeaderChangeListener {

    @BindView(R.id.srl_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_common_list)
    RecyclerView mDailyRecyclerView;

    private ZHLatestDailyAdapter mDailyAdapter;
    /**
     * 过去的天数
     */
    private int pastDays = 1;
    /**
     * 顶部轮播图
     */
    private EasyBanner mBanner;
    /**
     * banner所占高度
     */
    private static final int BANNER_HEIGHT = 200;

    public static ZHLatestDailyFragment newInstance() {

        Bundle args = new Bundle();

        ZHLatestDailyFragment fragment = new ZHLatestDailyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreated = true;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ZHDailyLatestPresenter(mActivity);
    }

    @Override
    protected void initViewEvent() {
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_zh_latest_daily;
    }

    @Override
    protected void initView() {
        //下拉刷新颜色
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mDailyAdapter = new ZHLatestDailyAdapter(mActivity,
                null);
        // 开启RecyclerView动画
        mDailyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        // 循环播放动画
        mDailyAdapter.isFirstOnly(false);
        // 可加载更多
        mDailyAdapter.setEnableLoadMore(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mDailyRecyclerView.setLayoutManager(linearLayoutManager);
        mDailyRecyclerView.setAdapter(mDailyAdapter);
        //设置加载更多监听器
        mDailyAdapter.setOnLoadMoreListener(this, mDailyRecyclerView);
        // 当未满一屏幕时不刷新
        mDailyAdapter.disableLoadMoreIfNotFullPage();
        // 设置RecyclerView的item监听
        mDailyAdapter.setOnItemClickListener(this);

        initBanner();

        //设置标题改变的listener
        mDailyAdapter.setOnHeaderChangeListener(this);
    }

    /**
     * 初始化banner
     */
    private void initBanner() {
        // 动态生成banner
        mBanner = new EasyBanner(mActivity);
        // 设置banner的大小
        LinearLayout.LayoutParams bannerLayoutParams = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mActivity, BANNER_HEIGHT));
        mBanner.setLayoutParams(bannerLayoutParams);
        //设置banner图片加载器
        mBanner.setImageLoader(new EasyBanner.ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                GlideUtils.loadCustomImage(mActivity, url, imageView);
            }
        });
        // 设置bannerItem监听事件
        mBanner.setOnItemClickListener(this);
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        mPresenter.reqDailyDataFromNet();
    }

    @Override
    public void showLatestData(LatestDailyListBean latestDailyListBean) {
        if (latestDailyListBean == null) {
            mStateView.showEmpty(R.string.stfEmptyMessage, R.string.stfButtonRetry);
            return;
        }

        //提取数据源中的image地址和title
        List<LatestDailyListBean.TopStoriesBean> topStories = latestDailyListBean.getTopStories();
        List<String> topImageUrls = new ArrayList<>();
        List<String> topContentData = new ArrayList<>();
        for (LatestDailyListBean.TopStoriesBean topStory : topStories) {
            topImageUrls.add(topStory.getImage());
            topContentData.add(topStory.getTitle());
        }

        if (mDailyAdapter.getHeaderLayoutCount() == 0) {
            // 添加banner
            mDailyAdapter.addHeaderView(mBanner);
            //设置banner图片url和图片标题
            mBanner.initBanner(topImageUrls, topContentData);
        } else {
            //设置banner图片url和图片标题
            mBanner.resetData(topImageUrls, topContentData);
        }
        mBanner.start();

        mDailyAdapter.setNewData(latestDailyListBean.getStories());

    }

    @Override
    public void loadMoreSuccess(String groupTitle, PastNewsBean pastNewsBean) {
        mDailyAdapter.loadMoreComplete();
        if (pastNewsBean == null) {
            return;
        }
        mDailyAdapter.addData(pastNewsBean.getStories());
    }

    @Override
    public void loadMoreFailed() {
        mDailyAdapter.loadMoreFail();
    }

    @Override
    public void onRefresh() {
        mPresenter.reqDailyDataFromNet();
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadMoreData(pastDays++);
    }

    @Override
    public void closeRefresh() {
        mRefreshLayout.setRefreshing(false);
    }

    // RecyclerView的item点击事件
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ZHDailyDetailsActivity.enterZHDailyDetailsActi(mActivity, mPresenter.getClickItemId
                (position));
    }

    // mBanner的点击事件
    @Override
    public void onItemClick(int position, String title) {
        ZHDailyDetailsActivity.enterZHDailyDetailsActi(mActivity, mPresenter.getHeaderClickItemId
                (position));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //设置标题
            if (mActivity != null && mActivity instanceof MainActivity) {
                MainActivity activity = (MainActivity) mActivity;
                activity.setToolBar(StringUtils.getStringByResId(activity, R.string.zh_daily));
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
        if (!isCreated) {
            return;
        }
        if (mBanner == null) {
            return;
        }
        if (isVisibleToUser) {
            mBanner.start();
        } else {
            mBanner.stop();
        }
    }

    @Override
    public void onHeaderChanged(String title) {
        if (mActivity != null && mActivity instanceof MainActivity) {
            MainActivity activity = (MainActivity) mActivity;
            activity.setToolBar(title);
        }
    }
}
