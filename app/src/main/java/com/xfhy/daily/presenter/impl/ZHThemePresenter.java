package com.xfhy.daily.presenter.impl;

import com.xfhy.androidbasiclibs.basekit.presenter.RxPresenter;
import com.xfhy.androidbasiclibs.common.CommonSubscriber;
import com.xfhy.androidbasiclibs.util.Constants;
import com.xfhy.daily.model.ZHDataManager;
import com.xfhy.daily.model.bean.TopicDailyListBean;
import com.xfhy.daily.presenter.ZHThemeContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xfhy
 *         create at 2017/11/18 23:32
 *         description：知乎主题presenter
 */
public class ZHThemePresenter extends RxPresenter<ZHThemeContract.View> implements
        ZHThemeContract.Presenter {

    /**
     * 知乎数据管理类 model
     */
    private ZHDataManager mZHDataManager;
    private List<TopicDailyListBean.OthersBean> mData;
    private int mStep;

    public ZHThemePresenter() {
        mZHDataManager = ZHDataManager.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromNet() {
        getView().onLoading();
        mStep = Constants.STATE_LOADING;
        addSubscribe(
                mZHDataManager.getTopicDailyList()
                        .map(new Function<TopicDailyListBean, List<TopicDailyListBean.OthersBean>>() {
                            @Override
                            public List<TopicDailyListBean.OthersBean> apply(TopicDailyListBean
                                                                                     topicDailyListBean)
                                    throws Exception {
                                return topicDailyListBean.getOthers();
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new CommonSubscriber<List<TopicDailyListBean.OthersBean>>(getView()) {
                            @Override
                            public void onNext(List<TopicDailyListBean.OthersBean> othersBeans) {
                                if (othersBeans != null) {
                                    getView().loadSuccess(othersBeans);
                                    mData = othersBeans;
                                } else {
                                    getView().showErrorMsg("主题列表加载失败....");
                                    getView().showEmptyView();
                                }
                                mStep = Constants.STATE_NORMAL;
                            }
                        })
        );
    }

    @Override
    public void refreshData() {
        if (mStep == Constants.STATE_LOADING) {
            return;
        }
        reqDataFromNet();
    }

    @Override
    public List<TopicDailyListBean.OthersBean> getData() {
        return mData;
    }
}
