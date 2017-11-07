package com.xfhy.daily.presenter.impl;

import android.content.Context;

import com.trello.rxlifecycle2.RxLifecycle;
import com.xfhy.androidbasiclibs.basekit.presenter.AbstractPresenter;
import com.xfhy.androidbasiclibs.common.util.LogUtils;
import com.xfhy.daily.network.RetrofitHelper;
import com.xfhy.daily.network.entity.zhihu.DailyContentBean;
import com.xfhy.daily.presenter.ZHDailyDetailsContract;
import com.xfhy.daily.ui.activity.ZHDailyDetailsActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author feiyang
 * @time create at 2017/11/7 13:51
 * @description 知乎最新日报详情
 */
public class ZHDailyDetailsPresenter extends AbstractPresenter<ZHDailyDetailsContract.View>
        implements ZHDailyDetailsContract.Presenter {

    /**
     * Retrofit帮助类
     */
    private RetrofitHelper mRetrofitHelper;
    private ZHDailyDetailsActivity mActivity;

    public ZHDailyDetailsPresenter(Context context) {
        super(context);
        if (context != null && context instanceof ZHDailyDetailsActivity) {
            mActivity = (ZHDailyDetailsActivity) context;
        }
        mRetrofitHelper = RetrofitHelper.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void reqDailyContentFromNet(String id) {
        mRetrofitHelper.getZhiHuApi().getDailyContent(id)
                .compose(mActivity.bindLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DailyContentBean>() {
                    @Override
                    public void accept(DailyContentBean dailyContentBean) throws Exception {
                        if (dailyContentBean != null) {
                            LogUtils.e(dailyContentBean.toString());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }

    @Override
    public void reqDailyExtraInfoFromNet(String id) {

    }

    @Override
    public void collectArticle() {

    }

}
