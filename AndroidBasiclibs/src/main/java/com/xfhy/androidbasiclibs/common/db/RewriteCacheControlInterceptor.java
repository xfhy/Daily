package com.xfhy.androidbasiclibs.common.db;

import android.content.Context;
import android.text.TextUtils;

import com.xfhy.androidbasiclibs.common.util.DevicesUtils;
import com.xfhy.androidbasiclibs.common.util.LogUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author feiyang
 *         time create at 2017/11/20 15:49
 *         description 拦截器
 *         有网络,走网络请求数据
 *         无网络,则从缓存加载数据
 */
public class RewriteCacheControlInterceptor implements Interceptor {

    private Context mContext;
    /**
     * 无网络,设缓存有效期为两周
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 14;
    /**
     * 有网 缓存60s
     */
    private static final long MAX_AGE = 60;

    public RewriteCacheControlInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String cacheControl = request.cacheControl().toString();
        if (!DevicesUtils.hasNetworkConnected(mContext)) {
            request = request.newBuilder()
                    .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl.FORCE_CACHE :
                            CacheControl.FORCE_NETWORK)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        if (DevicesUtils.hasNetworkConnected(mContext)) {

            LogUtils.e("有网络");
            //有网的时候连接服务器请求,缓存60s
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + MAX_AGE)
                    .removeHeader("Pragma")
                    .build();
        } else {

            LogUtils.e("无网络");

            //网络断开时读取缓存
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                    .removeHeader("Pragma")
                    .build();
        }
    }
}
