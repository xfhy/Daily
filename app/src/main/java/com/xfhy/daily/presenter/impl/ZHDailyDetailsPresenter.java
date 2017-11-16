package com.xfhy.daily.presenter.impl;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.xfhy.androidbasiclibs.basekit.presenter.AbstractPresenter;
import com.xfhy.androidbasiclibs.common.db.CollectBean;
import com.xfhy.androidbasiclibs.common.db.CollectBeanDao;
import com.xfhy.androidbasiclibs.common.db.CollectDao;
import com.xfhy.androidbasiclibs.common.db.DBConstants;
import com.xfhy.androidbasiclibs.common.util.Constants;
import com.xfhy.androidbasiclibs.common.util.DateUtils;
import com.xfhy.androidbasiclibs.common.util.DevicesUtils;
import com.xfhy.androidbasiclibs.common.util.LogUtils;
import com.xfhy.androidbasiclibs.common.util.SpUtil;
import com.xfhy.daily.NewsApplication;
import com.xfhy.daily.network.RetrofitHelper;
import com.xfhy.daily.network.entity.zhihu.DailyContentBean;
import com.xfhy.daily.network.entity.zhihu.DailyExtraInfoBean;
import com.xfhy.daily.presenter.ZHDailyDetailsContract;
import com.xfhy.daily.ui.activity.ZHDailyDetailsActivity;

import java.util.Date;
import java.util.List;

import io.reactivex.FlowableTransformer;
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
    /**
     * 日报数据
     */
    private DailyContentBean mDailyContentBean;
    /**
     * 日报额外信息
     */
    private DailyExtraInfoBean mDailyExtraInfoBean;

    public ZHDailyDetailsPresenter(Context context) {
        super(context);
        mRetrofitHelper = RetrofitHelper.getInstance();
    }

    @Override
    public void reqDailyContentFromNet(String id) {
        view.onLoading();
        LifecycleTransformer lifecycleTransformer = view.bindLifecycle();
        if (DevicesUtils.hasNetworkConnected(mContext)) {
            mRetrofitHelper.getZhiHuApi().getDailyContent(id)
                    .compose(lifecycleTransformer)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<DailyContentBean>() {
                        @Override
                        public void accept(DailyContentBean dailyContentBean) throws Exception {
                            mDailyContentBean = dailyContentBean;
                            if (mDailyContentBean != null) {
                                view.showContent();
                                view.loadSuccess(mDailyContentBean);
                            } else {
                                view.showEmptyView();
                            }

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e("从网络请求日报内容失败" + throwable.getCause() + throwable
                                    .getLocalizedMessage());
                            view.loadError();
                        }
                    });
        } else {
            view.showOffline();
        }

    }

    @Override
    public void reqDailyExtraInfoFromNet(String id) {
        if (DevicesUtils.hasNetworkConnected(mContext)) {
            mRetrofitHelper.getZhiHuApi().getDailyExtraInfo(id)
                    .compose(view.bindLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<DailyExtraInfoBean>() {
                        @Override
                        public void accept(DailyExtraInfoBean dailyExtraInfoBean) throws Exception {
                            mDailyExtraInfoBean = dailyExtraInfoBean;
                            if (mDailyExtraInfoBean != null) {
                                view.setExtraInfo(mDailyExtraInfoBean.getPopularity(),
                                        mDailyExtraInfoBean.getComments());
                            } else {
                                view.showErrorMsg("日报评论信息请求失败");
                                LogUtils.e("mDailyContentBean == null");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e("日报评论信息请求失败" + throwable.getCause() + throwable
                                    .getLocalizedMessage());
                            view.showErrorMsg("日报评论信息请求失败");
                        }
                    });
        }
    }

    @Override
    public void collectArticle(String id) {
        CollectBean collectBean = new CollectBean();
        collectBean.setFrom(DBConstants.COLLECT_ZHIHU_LATEST_DAILY);
        collectBean.setKey(id);
        collectBean.setCollectionDate(DateUtils.getDateFormatText(new Date(), "yyyy-MM-dd"));

        //先判断数据库中是否存在该日报,存在则删除  不存在则插入

        List<CollectBean> collectBeans = CollectDao.queryCacheByKey(NewsApplication.getDaoSession
                (), id);
        if (collectBeans == null || collectBeans.size() == 0) {
            CollectDao.insertCollect(NewsApplication.getDaoSession(), collectBean);
        } else {
            CollectDao.deleteCache(NewsApplication.getDaoSession(), collectBeans.get(0).getId());
        }

    }

    @Override
    public boolean isCollected(String id) {
        //先判断数据库中是否存在该日报,存在则曾经被收藏过

        List<CollectBean> collectBeans = CollectDao.queryCacheByKey(NewsApplication.getDaoSession
                (), id);
        if (collectBeans == null || collectBeans.size() == 0) {
            view.setCollectBtnSelState(false);
            return false;
        } else {
            view.setCollectBtnSelState(true);
            return true;
        }
    }

    @Override
    public DailyContentBean getData() {
        return mDailyContentBean;
    }

    @Override
    public boolean getNoImageState() {
        return SpUtil.getBoolan(NewsApplication.getAppContext(), Constants.IS_NO_IMAGE, false);
    }

    @Override
    public boolean getAutoCacheState() {
        return SpUtil.getBoolan(NewsApplication.getAppContext(), Constants.IS_AUTO_CACHE, false);
    }
}
