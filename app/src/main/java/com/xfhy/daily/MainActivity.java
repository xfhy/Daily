package com.xfhy.daily;

import com.xfhy.androidbasiclibs.basekit.activity.BaseActivity;
import com.xfhy.androidbasiclibs.common.utils.LogUtils;
import com.xfhy.daily.network.RetrofitHelper;
import com.xfhy.daily.network.entity.zhihu.LatestDailyListBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * author xfhy
 * create at 2017/9/12 21:11
 * description：主界面
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void initView() {
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewEvent() {

    }

    @Override
    protected void initData() {
        RetrofitHelper retrofitHelper = RetrofitHelper.getInstance();

        retrofitHelper.getZhiHuApi().getLatestDailyList().compose(this
                .<LatestDailyListBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LatestDailyListBean>() {
                    @Override
                    public void accept(LatestDailyListBean s) throws Exception {
                        LogUtils.e(s.toString());
                    }
                });
    }
}
