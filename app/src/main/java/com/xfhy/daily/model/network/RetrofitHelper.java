package com.xfhy.daily.model.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xfhy.androidbasiclibs.net.converter.FastJsonConverterFactory;
import com.xfhy.androidbasiclibs.net.OkHttpUtils;
import com.xfhy.daily.model.network.api.ZhiHuService;

import retrofit2.Retrofit;

/**
 * author feiyang
 * create at 2017/9/25 18:59
 * description：Retrofit使用帮助类
 */
public class RetrofitHelper {

    private RetrofitHelper() {
    }

    /**
     * 单例
     * 懒加载 && 线程安全
     */
    private static class SingletonHolder {
        private static final RetrofitHelper INSTANCE = new RetrofitHelper();
    }

    public static RetrofitHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取知乎的Service实例
     */
    public ZhiHuService getZhiHuApi() {
        return createApi(ZhiHuService.class, ApiConstants.ZHIHU_BASE_URL);
    }

    /**
     * 拿到对应API接口的实例
     *
     * @param clazz   API接口class
     * @param baseUrl BaseUrl
     * @param <T>     API接口
     * @return API接口实现
     */
    private <T> T createApi(Class<T> clazz, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(OkHttpUtils.getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //适配器,
                // 用于将Call对象转为RxJava的观察者
                .addConverterFactory(FastJsonConverterFactory.create())  //转换器,  用于将
                .build();

        //获取对应的Call对象  创建由服务接口定义的API端点的实现。  有了实现就可以进行网络访问了
        return retrofit.create(clazz);
    }

}
