package com.xfhy.daily.presenter.impl;

import com.xfhy.androidbasiclibs.basekit.presenter.RxPresenter;
import com.xfhy.androidbasiclibs.common.CommonSubscriber;
import com.xfhy.androidbasiclibs.util.DevicesUtils;
import com.xfhy.daily.model.ZHDataManager;
import com.xfhy.daily.model.bean.DailyCommentBean;
import com.xfhy.daily.presenter.ZHCommentContract;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * author feiyang
 * time create at 2017/11/16 14:55
 * description 知乎评论页Presenter
 */
public class ZHCommentPresenter extends RxPresenter<ZHCommentContract.View> implements
        ZHCommentContract.Presenter {

    /**
     * 知乎数据管理类 model
     */
    private ZHDataManager mZHDataManager;
    /**
     * 评论数据
     */
    private List<DailyCommentBean.CommentsBean> mComments;
    /**
     * 当前请求的是哪个请求  长评论,短评论
     */
    private int reqStep = 1;

    public ZHCommentPresenter() {
        mZHDataManager = ZHDataManager.getInstance();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reqLongComFromNet(final String id) {
        getView().onLoading();
        if (DevicesUtils.hasNetworkConnected()) {
            Flowable<DailyCommentBean> longCommentFlowable = mZHDataManager.getDailyLongComments(id);
            Flowable<DailyCommentBean> shortCommentFlowable = mZHDataManager.getDailyShortComments(id);
            addSubscribe(Flowable.concat(longCommentFlowable, shortCommentFlowable)   //必须按顺序
                    .map(new Function<DailyCommentBean, List<DailyCommentBean.CommentsBean>>() {
                        @Override
                        public List<DailyCommentBean.CommentsBean> apply(DailyCommentBean dailyCommentBean)
                                throws Exception {
                            return dailyCommentBean.getComments();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new CommonSubscriber<List<DailyCommentBean.CommentsBean>>(getView(), "日报评论信息请求失败") {
                        @Override
                        public void onNext(List<DailyCommentBean.CommentsBean> commentsBeanList) {
                            //header
                            DailyCommentBean.CommentsBean headerBean = new DailyCommentBean
                                    .CommentsBean(true);
                            //区分长评论和短评论
                            if (reqStep == 1) {
                                headerBean.header = commentsBeanList.size() + "条长评";
                            } else {
                                headerBean.header = commentsBeanList.size() + "条短评";
                                reqStep--;
                            }
                            commentsBeanList.add(0, headerBean);
                            mComments = commentsBeanList;
                            getView().loadCommentSuccess(commentsBeanList);
                            reqStep++;
                        }
                    })
            );
        } else {
            getView().showOffline();
        }
    }

    @Override
    public List<DailyCommentBean.CommentsBean> getComments() {
        return mComments;
    }
}
