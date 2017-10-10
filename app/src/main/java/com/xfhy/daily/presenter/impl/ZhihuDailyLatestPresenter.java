package com.xfhy.daily.presenter.impl;

import android.content.Context;

import com.xfhy.androidbasiclibs.basekit.presenter.AbstractPresenter;
import com.xfhy.androidbasiclibs.common.util.DevicesUtils;
import com.xfhy.daily.network.RetrofitHelper;
import com.xfhy.daily.presenter.ZhihuDailyLatestContract;

/**
 * author feiyang
 * create at 2017/9/30 16:52
 * description：知乎最新日报的presenter
 */
public class ZhihuDailyLatestPresenter extends AbstractPresenter<ZhihuDailyLatestContract.View>
        implements ZhihuDailyLatestContract.Presenter {
    /**
     * Retrofit帮助类
     */
    private RetrofitHelper mRetrofitHelper;
    private Context mContext;

    public ZhihuDailyLatestPresenter(Context context) {
        this.mContext = context;
        mRetrofitHelper = RetrofitHelper.getInstance();
    }

    @Override
    public void reqDailyDataFromNet() {
        /*
        请求网络的demo   以后需要写在presenter里面的

        RetrofitHelper retrofitHelper = RetrofitHelper.getInstance();
        if (DevicesUtils.isNetworkConnected(this)) {
            retrofitHelper.getZhiHuApi().getHotDailyList()
                    .compose(this.<HotDailyBean>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<HotDailyBean>() {
                        @Override
                        public void accept(HotDailyBean s) throws Exception {
                            LogUtils.e(s.toString());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e(throwable.getLocalizedMessage());
                        }
                    });
        } else {
            ToastUtil.showMessage(this, "没有网络");
        }*/

    }

    @Override
    public void reqDailyDataFromDB() {

    }

    @Override
    public boolean saveDailyDataToDB() {
        return false;
    }

    @Override
    public void refreshDataFromNet() {

    }
}
