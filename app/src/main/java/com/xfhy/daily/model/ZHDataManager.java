package com.xfhy.daily.model;

import com.xfhy.daily.model.bean.ColumnDailyBean;
import com.xfhy.daily.model.bean.ColumnDailyDetailsBean;
import com.xfhy.daily.model.bean.DailyCommentBean;
import com.xfhy.daily.model.bean.DailyContentBean;
import com.xfhy.daily.model.bean.DailyExtraInfoBean;
import com.xfhy.daily.model.bean.HotDailyBean;
import com.xfhy.daily.model.bean.LatestDailyListBean;
import com.xfhy.daily.model.bean.PastNewsBean;
import com.xfhy.daily.model.bean.ThemeDailyDetailsBean;
import com.xfhy.daily.model.bean.TopicDailyListBean;
import com.xfhy.daily.model.network.RetrofitHelper;

import io.reactivex.Flowable;

/**
 * Created by feiyang on 2017/12/14 18:41
 * Description : 知乎数据处理管理类
 */
public class ZHDataManager {

    private RetrofitHelper mRetrofitHelper;

    private ZHDataManager() {
        mRetrofitHelper = RetrofitHelper.getInstance();
    }

    /**
     * 单例
     * 懒加载 && 线程安全
     */
    private static class SingletonHolder {
        private static final ZHDataManager INSTANCE = new ZHDataManager();
    }

    public static ZHDataManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取最新日报列表
     *
     * @return 返回最新日报列表  被观察者 发射的类型是LatestDailyListBean
     */
    public Flowable<LatestDailyListBean> getLatestDailyList() {
        return mRetrofitHelper.getZhiHuApi().getLatestDailyList();
    }

    /**
     * 获取日报内容
     *
     * @param id 日报的id
     * @return 返回日报内容
     */
    public Flowable<DailyContentBean> getDailyContent(String id) {
        return mRetrofitHelper.getZhiHuApi().getDailyContent(id);
    }

    /**
     * 获取日报的额外信息
     *
     * @param id 日报的id
     * @return 返回日报的额外信息
     */
    public Flowable<DailyExtraInfoBean> getDailyExtraInfo(String id) {
        return mRetrofitHelper.getZhiHuApi().getDailyExtraInfo(id);
    }

    /**
     * 获取往期日报
     * <p>
     * 注意!!
     * 当url为https://news-at.zhihu.com/api/4/news/before/20170919
     * 实际上服务器返回的数据是20170918的
     * <p>
     * 当url为20170901,服务器返回的数据是20170831的
     *
     * @param date 日期  格式必须为yyyyMMdd  eg:20170924
     * @return 返回往期日报
     */
    public Flowable<PastNewsBean> getPastNews(String date) {
        return mRetrofitHelper.getZhiHuApi().getPastNews(date);
    }

    /**
     * 获取日报长评论
     *
     * @param id 日报id
     * @return 返回日报长评论
     */
    public Flowable<DailyCommentBean> getDailyLongComments(String id) {
        return mRetrofitHelper.getZhiHuApi().getDailyLongComments(id);
    }

    /**
     * 获取日报短评论
     *
     * @param id 日报id
     * @return 返回日报短评论
     */
    public Flowable<DailyCommentBean> getDailyShortComments(String id) {
        return mRetrofitHelper.getZhiHuApi().getDailyShortComments(id);
    }

    /**
     * 获取主题日报列表
     */
    public Flowable<TopicDailyListBean> getTopicDailyList() {
        return mRetrofitHelper.getZhiHuApi().getTopicDailyList();
    }

    /**
     * 获取主题日报详情
     *
     * @param number 主题日报编号
     * @return 返回主题日报详情
     */
    public Flowable<ThemeDailyDetailsBean> getThemeDailyDetails(String number) {
        return mRetrofitHelper.getZhiHuApi().getThemeDailyDetails(number);
    }

    /**
     * 获取专栏日报列表
     */
    public Flowable<ColumnDailyBean> getColumnDailyList() {
        return mRetrofitHelper.getZhiHuApi().getColumnDailyList();
    }

    /**
     * 获取专栏日报详情列表
     *
     * @param id 专栏编号
     * @return 获取专栏日报详情列表
     */
    public Flowable<ColumnDailyDetailsBean> getColumnDailyDetailsList(String id) {
        return mRetrofitHelper.getZhiHuApi().getColumnDailyDetailsList(id);
    }

    /**
     * 获取热门日报文章列表
     */
    public Flowable<HotDailyBean> getHotDailyList() {
        return mRetrofitHelper.getZhiHuApi().getHotDailyList();
    }



}
