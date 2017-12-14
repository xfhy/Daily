package com.xfhy.daily.model.network.api;

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

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * author feiyang
 * create at 2017/9/25 18:51
 * description：知乎相关API
 */
public interface ZhiHuService {

    //最新日报列表   https://news-at.zhihu.com/api/4/news/latest

    /**
     * 获取最新日报列表
     *
     * @return 返回最新日报列表  被观察者 发射的类型是LatestDailyListBean
     */
    @GET("news/latest")
    Flowable<LatestDailyListBean> getLatestDailyList();

    //日报内容   https://news-at.zhihu.com/api/4/news/9632777

    /**
     * 获取日报内容
     *
     * @param id 日报的id
     * @return 返回日报内容
     */
    @GET("news/{id}")
    Flowable<DailyContentBean> getDailyContent(@Path("id") String id);

    //日报的额外信息   https://news-at.zhihu.com/api/4/story-extra/9632777

    /**
     * 获取日报的额外信息
     *
     * @param id 日报的id
     * @return 返回日报的额外信息
     */
    @GET("story-extra/{id}")
    Flowable<DailyExtraInfoBean> getDailyExtraInfo(@Path("id") String id);

    //往期日报 https://news-at.zhihu.com/api/4/news/before/20131119

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
    @GET("news/before/{date}")
    Flowable<PastNewsBean> getPastNews(@Path("date") String date);

    //日报的长评论  https://news-at.zhihu.com/api/4/story/8997528/long-comments

    /**
     * 获取日报长评论
     *
     * @param id 日报id
     * @return 返回日报长评论
     */
    @GET("story/{id}/long-comments")
    Flowable<DailyCommentBean> getDailyLongComments(@Path("id") String id);

    //日报的短评论   https://news-at.zhihu.com/api/4/story/4232852/short-comments

    /**
     * 获取日报短评论
     *
     * @param id 日报id
     * @return 返回日报短评论
     */
    @GET("story/{id}/short-comments")
    Flowable<DailyCommentBean> getDailyShortComments(@Path("id") String id);

    //主题日报列表查看   https://news-at.zhihu.com/api/4/themes

    /**
     * 获取主题日报列表
     */
    @GET("themes")
    Flowable<TopicDailyListBean> getTopicDailyList();

    //主题日报详情   https://news-at.zhihu.com/api/4/theme/11

    /**
     * 获取主题日报详情
     *
     * @param number 主题日报编号
     * @return 返回主题日报详情
     */
    @GET("theme/{number}")
    Flowable<ThemeDailyDetailsBean> getThemeDailyDetails(@Path("number") String number);

    //专栏日报   https://news-at.zhihu.com/api/4/sections

    /**
     * 获取专栏日报列表
     */
    @GET("sections")
    Flowable<ColumnDailyBean> getColumnDailyList();

    //专栏日报详情  也是一个列表  https://news-at.zhihu.com/api/4/section/1

    /**
     * 获取专栏日报详情列表
     *
     * @param id 专栏编号
     * @return 获取专栏日报详情列表
     */
    @GET("section/{id}")
    Flowable<ColumnDailyDetailsBean> getColumnDailyDetailsList(@Path("id") String id);

    //热门日报 https://news-at.zhihu.com/api/4/news/hot

    /**
     * 获取热门日报文章列表
     */
    @GET("news/hot")
    Flowable<HotDailyBean> getHotDailyList();

}
