package com.xfhy.daily.network.api;

import com.xfhy.daily.network.entity.zhihu.LatestDailyListBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;

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
     * @return 返回最新日报列表
     */
    @GET("news/latest")
    Flowable<LatestDailyListBean> getLatestDailyList();

    //日报内容   https://news-at.zhihu.com/api/4/news/3892357

    //日报的额外信息   https://news-at.zhihu.com/api/4/story-extra/#{id}

    //往期日报 https://news-at.zhihu.com/api/4/news/before/20131119

    //日报的长评论  https://news-at.zhihu.com/api/4/story/8997528/long-comments

    //日报的短评论   https://news-at.zhihu.com/api/4/story/4232852/short-comments

    //主题日报列表查看   https://news-at.zhihu.com/api/4/themes

    //主题日报详情   https://news-at.zhihu.com/api/4/theme/11

    //专栏日报   https://news-at.zhihu.com/api/4/sections

    //专栏日报详情  https://news-at.zhihu.com/api/4/section/{id}

    //热门日报 https://news-at.zhihu.com/api/4/news/hot

}
