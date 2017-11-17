package com.xfhy.daily.presenter.impl;

import android.content.Context;
import android.support.annotation.Nullable;

import com.xfhy.androidbasiclibs.basekit.presenter.AbstractPresenter;
import com.xfhy.androidbasiclibs.common.util.DevicesUtils;
import com.xfhy.androidbasiclibs.common.util.LogUtils;
import com.xfhy.daily.network.RetrofitHelper;
import com.xfhy.daily.network.entity.zhihu.DailyCommentBean;
import com.xfhy.daily.presenter.ZHCommentContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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
     * 长评论数据
     */
    private List<DailyCommentBean.CommentsBean> mLongComments;
    /**
     * 短评论数据
     */
    private List<DailyCommentBean.CommentsBean> mShortComments;

    public ZHCommentPresenter(Context context) {
        super(context);
        mRetrofitHelper = RetrofitHelper.getInstance();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reqLongComFromNet(String id) {
        view.onLoading();
        if (DevicesUtils.hasNetworkConnected(mContext)) {
            mRetrofitHelper.getZhiHuApi().getDailyLongComments(id)
                    .compose(view.bindLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<DailyCommentBean>() {
                        @Override
                        public void accept(DailyCommentBean dailyCommentBean) throws Exception {
                            if (dailyCommentBean != null && dailyCommentBean
                                    .getComments() != null) {
                                List<DailyCommentBean.CommentsBean> longComments = dailyCommentBean
                                        .getComments();
                                DailyCommentBean.CommentsBean headerBean = new DailyCommentBean
                                        .CommentsBean(true);
                                headerBean.header = longComments.size() + "条长评";
                                longComments.add(0, headerBean);
                                view.loadLongComSuccess(longComments);
                                LogUtils.e(dailyCommentBean.toString());
                            } else {
                                view.showEmptyView();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e("日报评论信息请求失败" + throwable.getCause() + throwable
                                    .getLocalizedMessage());
                            view.showErrorMsg("日报长评论信息请求失败");
                        }
                    });
        } else {
            view.showOffline();
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public void reqShortComFromNet(String id) {
        if (DevicesUtils.hasNetworkConnected(mContext)) {
            mRetrofitHelper.getZhiHuApi().getDailyShortComments(id)
                    .compose(view.bindLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<DailyCommentBean>() {
                        @Override
                        public void accept(DailyCommentBean dailyCommentBean) throws Exception {
                            if (dailyCommentBean != null && dailyCommentBean.getComments() !=
                                    null) {
                                List<DailyCommentBean.CommentsBean> shortComments = dailyCommentBean
                                        .getComments();
                                DailyCommentBean.CommentsBean headerBean = new DailyCommentBean
                                        .CommentsBean(true);
                                headerBean.header = shortComments.size() + "条短评";
                                shortComments.add(0, headerBean);
                                view.loadShortComSuccess(dailyCommentBean.getComments());
                                LogUtils.e(dailyCommentBean.toString());
                            } else {
                                view.loadShortComError("该日报无短评论");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e("日报评论信息请求失败" + throwable.getCause() + throwable
                                    .getLocalizedMessage());
                            view.showErrorMsg("日报短评论信息请求失败");
                        }
                    });
        } else {
            view.showOffline();
        }
    }

    @Nullable
    @Override
    public List<DailyCommentBean.CommentsBean> getLongComments() {
        return mLongComments;
    }

    @Nullable
    @Override
    public List<DailyCommentBean.CommentsBean> getShortComments() {
        return mShortComments;
    }
}
