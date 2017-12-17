package com.xfhy.daily.presenter.impl;

import com.xfhy.androidbasiclibs.basekit.presenter.RxPresenter;
import com.xfhy.androidbasiclibs.common.CommonSubscriber;
import com.xfhy.androidbasiclibs.db.CollectBean;
import com.xfhy.androidbasiclibs.db.CollectDao;
import com.xfhy.androidbasiclibs.db.DBConstants;
import com.xfhy.androidbasiclibs.util.Constants;
import com.xfhy.androidbasiclibs.util.DateUtils;
import com.xfhy.androidbasiclibs.util.LogUtils;
import com.xfhy.androidbasiclibs.util.SpUtil;
import com.xfhy.daily.NewsApplication;
import com.xfhy.daily.model.ZHDataManager;
import com.xfhy.daily.model.bean.DailyContentBean;
import com.xfhy.daily.model.bean.DailyExtraInfoBean;
import com.xfhy.daily.presenter.ZHDailyDetailsContract;

import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author feiyang
 * @time create at 2017/11/7 13:51
 * @description 知乎最新日报详情
 */
public class ZHDailyDetailsPresenter extends RxPresenter<ZHDailyDetailsContract.View>
        implements ZHDailyDetailsContract.Presenter {

    /**
     * 知乎数据管理类 model
     */
    private ZHDataManager mZHDataManager;
    /**
     * 日报数据
     */
    private DailyContentBean mDailyContentBean;
    /**
     * 日报额外信息
     */
    private DailyExtraInfoBean mDailyExtraInfoBean;

    public ZHDailyDetailsPresenter() {
        mZHDataManager = ZHDataManager.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDailyContentFromNet(String id) {
        getView().onLoading();
        addSubscribe(mZHDataManager.getDailyContent(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CommonSubscriber<DailyContentBean>(getView()) {
                    @Override
                    public void onNext(DailyContentBean dailyContentBean) {
                        mDailyContentBean = dailyContentBean;
                        if (mDailyContentBean != null) {
                            getView().showContent();
                            getView().loadSuccess(mDailyContentBean);
                        } else {
                            getView().showEmptyView();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        getView().loadError();
                    }
                })
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDailyExtraInfoFromNet(String id) {
        addSubscribe(mZHDataManager.getDailyExtraInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CommonSubscriber<DailyExtraInfoBean>(getView()) {
                    @Override
                    public void onNext(DailyExtraInfoBean dailyExtraInfoBean) {
                        mDailyExtraInfoBean = dailyExtraInfoBean;
                        if (mDailyExtraInfoBean != null) {
                            getView().setExtraInfo(mDailyExtraInfoBean.getPopularity(),
                                    mDailyExtraInfoBean.getComments());
                        } else {
                            getView().showErrorMsg("日报评论信息请求失败");
                            LogUtils.e("mDailyContentBean == null");
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                    }
                })
        );
    }

    @Override
    public void collectArticle(String id) {
        CollectBean collectBean = new CollectBean();
        collectBean.setFrom(DBConstants.COLLECT_ZHIHU_LATEST_DAILY);
        collectBean.setKey(id);
        collectBean.setCollectionDate(DateUtils.getDateFormatText(new Date(), "yyyy-MM-dd"));

        //先判断数据库中是否存在该日报,存在则删除  不存在则插入

        List<CollectBean> collectBeans = CollectDao.queryCacheByKey(id);
        if (collectBeans == null || collectBeans.size() == 0) {
            CollectDao.insertCollect(collectBean);
        } else {
            CollectDao.deleteCache(collectBeans.get(0).getId());
        }

    }

    @Override
    public boolean isCollected(String id) {
        //先判断数据库中是否存在该日报,存在则曾经被收藏过

        List<CollectBean> collectBeans = CollectDao.queryCacheByKey(id);
        if (collectBeans == null || collectBeans.size() == 0) {
            getView().setCollectBtnSelState(false);
            return false;
        } else {
            getView().setCollectBtnSelState(true);
            return true;
        }
    }

    @Override
    public DailyContentBean getData() {
        return mDailyContentBean;
    }

    @Override
    public int getCommentCount() {
        if (mDailyExtraInfoBean != null) {
            return mDailyExtraInfoBean.getComments();
        }
        return 0;
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
