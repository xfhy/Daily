package com.xfhy.daily.ui.activity;


import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xfhy.androidbasiclibs.basekit.activity.BaseMvpActivity;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.DailyContentBean;
import com.xfhy.daily.network.entity.zhihu.DailyExtraInfoBean;
import com.xfhy.daily.presenter.ZHDailyDetailsContract;
import com.xfhy.daily.presenter.impl.ZHDailyDetailsPresenter;

/**
 * @author feiyang
 * @create 2017年11月7日11:14:19
 * @description 知乎最新日报详情页
 */
public class ZHDailyDetailsActivity extends BaseMvpActivity<ZHDailyDetailsContract.Presenter>
        implements ZHDailyDetailsContract.View{

    @Override
    protected void initIntentData() {
        super.initIntentData();

    }

    @Override
    protected void initData() {
        super.initData();
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

    }
}
