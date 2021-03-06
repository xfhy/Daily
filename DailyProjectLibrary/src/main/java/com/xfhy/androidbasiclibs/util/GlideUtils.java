package com.xfhy.androidbasiclibs.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by xfhy on 2017/9/24 21:27.
 * Description 用于Glide的加载图片
 * 由于每次加载图片都需要单独地配置,显得很麻烦,所以在这里简单封装一下
 */

public class GlideUtils {

    /*
     * Glide特点
     * 使用简单
     * 可配置度高，自适应程度高
     * 支持常见图片格式 Jpg png gif webp
     * 支持多种数据源  网络、本地、资源、Assets 等
     * 高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半
     * 生命周期集成   根据Activity/Fragment生命周期自动管理请求
     * 高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力
     * 这里默认支持Context，Glide支持Context,Activity,Fragment，FragmentActivity
     */

    /**
     * 图片加载的默认动画时长
     */
    private final static int IMAGE_LOAD_DURATION = 500;

    /**
     * 默认加载方式
     */
    public static void loadImageView(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).into(mImageView);
    }

    /**
     * 加载指定大小
     */
    public static void loadImageViewSize(Context mContext, String path, int width, int height,
                                         ImageView mImageView) {
        //图片加载的配置参数
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(mContext).load(path).apply(requestOptions.override(width, height)).into
                (mImageView);
    }

    /**
     * 设置加载中以及加载失败图片
     */
    public static void loadImageViewLoding(Context mContext, String path, ImageView mImageView,
                                           int loadingImage, int errorImageView) {
        //图片加载的配置参数
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(mContext).load(path).apply(requestOptions.placeholder(loadingImage).error
                (errorImageView)).into(mImageView);
    }

    /**
     * 设置加载中以及加载失败图片并且指定大小
     */
    public static void loadImageViewLoadingSize(Context mContext, String path, int width, int
            height, ImageView mImageView, int loadingImage, int errorImageView) {
        //图片加载的配置参数
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(mContext).load(path).apply(requestOptions.override(width, height).placeholder
                (loadingImage).error(errorImageView)).into(mImageView);
    }

    /**
     * 加载图片 设置跳过内存缓存
     */
    public static void loadImageViewCache(Context mContext, String path, ImageView mImageView) {
        //图片加载的配置参数
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(mContext).load(path).apply(requestOptions.skipMemoryCache(true)).into
                (mImageView);
    }

    /**
     * 设置下载优先级
     */
    public static void loadImageViewPriority(Context mContext, String path, ImageView mImageView) {
        //图片加载的配置参数
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(mContext).load(path).apply(requestOptions.priority(Priority.NORMAL)).into
                (mImageView);
    }

    /*
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * <p>
     * none:不作任何磁盘缓存
     * <p>
     * source:缓存源资源
     * <p>
     * result：缓存转换后的资源
     */

    /**
     * 设置缓存策略
     */
    public static void loadImageViewDiskCache(Context mContext, String path, ImageView mImageView) {
        //图片加载的配置参数
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(mContext).load(path).apply(requestOptions.diskCacheStrategy(DiskCacheStrategy
                .ALL)).into(mImageView);
    }

    /*
     * api也提供了几个常用的动画：比如crossFade()
     */

    /**
     * 加载图片 并设置加载动画
     */
    public static void loadImageViewAnim(Context mContext, String path, int anim, ImageView
            mImageView) {
        Glide.with(mContext).load(path).transition(DrawableTransitionOptions.withCrossFade()).into
                (mImageView);
    }

    /**
     * 设置缩略图支持  会先加载缩略图
     */
    public static void loadImageViewThumbnail(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).thumbnail(0.1f).into(mImageView);
    }

    /*
     * api提供了比如：centerCrop()、fitCenter()等
     */

    /**
     * 加载图片 并设置动态转换
     */
    public static void loadImageViewCrop(Context mContext, String path, ImageView mImageView) {
        //图片加载的配置参数
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(mContext).load(path).apply(requestOptions.centerCrop()).into(mImageView);
    }

    /**
     * 清理磁盘缓存
     */
    public static void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    /**
     * 清理内存缓存
     */
    public static void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }

    /**
     * 加载图片   并设置完所有东西  方便加载  里面设置有加载失败图片,缓存策略,动画
     *
     * @param context   Context
     * @param path      路径
     * @param imageView 需要加载图片到那个ImageView上
     */
    public static void loadCustomImage(Context context, String path, ImageView imageView) {
        //图片加载的配置参数
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .load(path)
                .thumbnail(0.1f)   //缩略图
                .apply(requestOptions
                        //.error(R.drawable.error)   // 加载失败图片   暂时没设置,还没有素材
                        .diskCacheStrategy(DiskCacheStrategy.ALL)) // 缓存策略是:所有的都缓存:内存缓存和磁盘缓存
                .transition(DrawableTransitionOptions.withCrossFade(IMAGE_LOAD_DURATION)) //动画
                .into(imageView);
    }

    /**
     * 加载图片   并设置完所有东西  方便加载  里面设置有加载失败图片,缓存策略,动画
     *
     * @param view   View
     * @param path      路径
     * @param imageView 需要加载图片到那个ImageView上
     */
    public static void loadCustomImage(View view, String path, ImageView imageView) {
        //图片加载的配置参数
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(view)
                .load(path)
                .thumbnail(0.1f)   //缩略图
                .apply(requestOptions
                        //.error(R.drawable.error)   // 加载失败图片   暂时没设置,还没有素材
                        .diskCacheStrategy(DiskCacheStrategy.ALL)) // 缓存策略是:所有的都缓存:内存缓存和磁盘缓存
                .transition(DrawableTransitionOptions.withCrossFade(IMAGE_LOAD_DURATION)) //动画
                .into(imageView);
    }

    /**
     * 加载Gif图片
     */
    public static void loadGif(Context context, int drawable, ImageView imageView) {
        Glide.with(context).load(drawable).into(imageView);
    }

}
