package com.xfhy.daily.ui.fragment.zhihu;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xfhy.androidbasiclibs.basekit.fragment.BaseMVPFragment;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.LatestDailyListBean;
import com.xfhy.daily.network.entity.zhihu.PastNewsBean;
import com.xfhy.daily.presenter.ZhihuDailyLatestContract;
import com.xfhy.daily.presenter.impl.ZhihuDailyLatestPresenter;

import butterknife.BindView;

/**
 * author feiyang
 * create at 2017/9/30 16:38
 * description：知乎最新日报fragment
 */
public class ZhihuLatestDailyFragment extends BaseMVPFragment<ZhihuDailyLatestPresenter>
        implements ZhihuDailyLatestContract.View {

    @BindView(R.id.rv_latest_daily_list)
    RecyclerView rvLatestDailyList;

    public static ZhihuLatestDailyFragment newInstance() {

        Bundle args = new Bundle();

        ZhihuLatestDailyFragment fragment = new ZhihuLatestDailyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ZhihuDailyLatestPresenter(mActivity);
    }

    @Override
    protected void initViewEvent() {
        /*-------------测试-----------------
        EmptyView emptyView = mRootView.findViewById(R.id.ev_empty_view);
        emptyView.setOnRetryListener(new EmptyView.OnRetryListener() {
            @Override
            public void onClick() {
                SnackbarUtil.showBarShortTime(mRootView, "测试dada", SnackbarUtil.RED, 0xffffc107);
//                SnackbarUtil.showBarLongTime(mRootView,"测试",SnackbarUtil.CONFIRM);
//                SnackbarUtil.showBarLongTime(mRootView, "测试", SnackbarUtil.ORANGE, SnackbarUtil
// .BLUE);
//                SnackbarUtil.showBarLongTime(mRootView,"网络不给力,请检查网络设置",SnackbarUtil.ALERT,
// "去设置",new
//                        View
//                        .OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        ToastUtil.showMessage(mActivity,"点我了...");
//                    }
//                });
            }
        });*/


        //SnackbarUtil.showBarShortTime(mRootView,"测试",SnackbarUtil.INFO);
        //SnackbarUtil.showBarShortTime(mRootView,"测试",SnackbarUtil.INFO);
        //SnackbarUtil.showBarShortTime(mRootView,"测试",SnackbarUtil.INFO);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_zhihu_latest_daily;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void lazyLoad() {
        mPresenter.reqDailyDataFromNet();
    }

    @Override
    public void showLatestData(LatestDailyListBean latestDailyListBean) {

    }

    @Override
    public void onLoading() {

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
    public void showMoreData(String groupTitle, PastNewsBean pastNewsBean) {

    }

    @Override
    public LifecycleTransformer<LatestDailyListBean> bindLifecycle() {
        return bindToLifecycle();
    }
}
