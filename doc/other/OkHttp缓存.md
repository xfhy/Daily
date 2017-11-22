# Retorfit2和OkHttp3缓存

> 需求如下:当用户在有网络的情况下,设置一个"缓冲时间",比如在请求了一次网络数据之后,接下来60s内进行网络请求其实是拿的缓存,这样做可以减少服务器负担,节约流量.
> 当无网络时,用缓存读取数据,数据默认保存2周.这样就可以进行离线阅读.

## 1.首先来配置拦截器

配置好时间等属性,再结合判断是否有网络来进行设置

```java
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
            //有网的时候连接服务器请求,缓存60s
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + MAX_AGE)
                    .removeHeader("Pragma")
                    .build();
        } else {
            //网络断开时读取缓存
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                    .removeHeader("Pragma")
                    .build();
        }
    }
}

```

## 2. 初始化OkHttp3

将拦截器设置进去即可.

```java
/**
* 初始化OkHttp
*/
public static void initOkHttp(Context context) {
    //拦截器
    RewriteCacheControlInterceptor mRewriteCacheControlInterceptor = new
            RewriteCacheControlInterceptor(context);

    //缓存文件
    File cacheFile = context.getCacheDir();
    //设置缓存大小
    Cache cache = new Cache(cacheFile, MAX_CACHE_SIZE);
    if (cacheFile != null) {
        okHttpClient = new OkHttpClient.Builder()
                //超时设置
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                //错误重连
                .retryOnConnectionFailure(true)
                //拦截器
                .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                .addInterceptor(mRewriteCacheControlInterceptor)
                //缓存
                .cache(cache)
                .build();
    } else {
        okHttpClient = new OkHttpClient.Builder().build();
    }

}
```
