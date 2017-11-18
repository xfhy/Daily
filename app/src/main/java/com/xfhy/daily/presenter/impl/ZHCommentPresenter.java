package com.xfhy.daily.presenter.impl;

import android.content.Context;

import com.xfhy.androidbasiclibs.basekit.presenter.AbstractPresenter;
import com.xfhy.androidbasiclibs.common.util.DevicesUtils;
import com.xfhy.androidbasiclibs.common.util.LogUtils;
import com.xfhy.daily.network.RetrofitHelper;
import com.xfhy.daily.network.entity.zhihu.DailyCommentBean;
import com.xfhy.daily.presenter.ZHCommentContract;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * author feiyang
 * time create at 2017/11/16 14:55
 * description 知乎评论页Presenter
 */
public class ZHCommentPresenter extends AbstractPresenter<ZHCommentContract.View> implements
        ZHCommentContract.Presenter {

    /**
     * Retrofit帮助类
     */
    private RetrofitHelper mRetrofitHelper;
    /**
     * 评论数据
     */
    private List<DailyCommentBean.CommentsBean> mComments;
    /**
     * 当前请求的是哪个请求  长评论,短评论
     */
    private int reqStep = 1;

    public ZHCommentPresenter(Context context) {
        super(context);
        mRetrofitHelper = RetrofitHelper.getInstance();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reqLongComFromNet(final String id) {
        view.onLoading();
        if (DevicesUtils.hasNetworkConnected(mContext)) {
            Flowable<DailyCommentBean> longCommentFlowable = mRetrofitHelper.getZhiHuApi()
                    .getDailyLongComments(id);
            Flowable<DailyCommentBean> shortCommentFlowable = mRetrofitHelper.getZhiHuApi()
                    .getDailyShortComments(id);
            Flowable.concat(longCommentFlowable, shortCommentFlowable)   //必须按顺序
                    .compose(view.bindLifecycle())
                    .map(new Function<DailyCommentBean, List<DailyCommentBean.CommentsBean>>() {
                        @Override
                        public List<DailyCommentBean.CommentsBean> apply(DailyCommentBean
                                                                                 dailyCommentBean)
                                throws Exception {
                            return dailyCommentBean.getComments();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<DailyCommentBean.CommentsBean>>() {
                        @Override
                        public void accept(List<DailyCommentBean.CommentsBean> commentsBeanList)
                                throws Exception {
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
                            view.loadCommentSuccess(commentsBeanList);
                            reqStep++;
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e("日报评论信息请求失败" + throwable.getCause() + throwable
                                    .getLocalizedMessage());
                            view.showErrorMsg("日报评论信息请求失败");
                        }
                    });
        } else {
            view.showOffline();
        }
    }

    @Override
    public List<DailyCommentBean.CommentsBean> getComments() {
        return mComments;
    }
}
