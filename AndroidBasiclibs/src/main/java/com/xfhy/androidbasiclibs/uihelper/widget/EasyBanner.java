package com.xfhy.androidbasiclibs.uihelper.widget;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfhy.androidbasiclibs.R;
import com.xfhy.androidbasiclibs.common.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author xfhy
 * create at 17-10-3 下午1:19
 * description：自定义的Banner   广告轮播图
 * <p>
 * 需要实现的东西：
 * 自动轮播
 * 无限左划右划
 * 传入广告条目
 * 加载网络图片（Glide），加载文字
 * 底部小白点可切换，大小可换，数量可动态增加
 * 监听点击事件
 * 设置滑动事件
 * PagerAdapter
 * 触摸时不能自动滑动
 * 实现ViewPager点击事件
 * 让外部去实现图片加载，实现解耦
 * <p>
 * <p>
 * 遇到的坑：ViewPager把触摸事件消费了，外层重写onTouchEvent没用的，已经被子View消费的事件，是没用回传回来的，所以我直接在dispatchTouchEvent()
 * 分发事件的时候就获取到该事件，并停止ViewPager的滚动   当用户抬起手指时，继续滚动。
 */

public class EasyBanner extends FrameLayout implements ViewPager.OnPageChangeListener {
    /**
     * 每个广告条目的图片地址
     */
    private List<String> imageUrlList;
    /**
     * 每个广告条目的文字内容
     */
    private List<String> contentList;
    /**
     * 用来盛放广告条目的
     */
    private ViewPager mViewPager;
    /**
     * 当前广告条目的文字内容
     */
    private TextView mContent;
    /**
     * 底部小圆点整个布局
     */
    private LinearLayout mPointLayout;
    /**
     * 用来加载banner图片的
     */
    private List<ImageView> imageViewList;
    /**
     * 小圆点上一次的位置
     */
    private int lastPosition;
    /**
     * 底部小圆点默认大小
     */
    private final static float POINT_DEFAULT_SIZE = 5f;
    /**
     * 切换广告的时长  单位：ms
     */
    private final static int BANNER_SWITCH_DELAY_MILLIS = 3000;
    /**
     * 用户是否正在触摸banner
     */
    private boolean isTouched = false;
    private PollingHandler mHandler = new PollingHandler();
    /**
     * banner点击事件监听器
     */
    private OnItemClickListener listener;
    /**
     * 图片加载器
     */
    private ImageLoader imageLoader;

    private static class PollingHandler extends Handler {
    }

    /**
     * 开启轮询?
     */
    private boolean pollingEnable = false;

    public EasyBanner(@NonNull Context context) {
        super(context);
        initView();
    }

    public EasyBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EasyBanner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        //加载布局   子View个数==0  则还没有加载布局
        if (getChildCount() == 0) {
            View.inflate(getContext(), R.layout.layout_banner, this);
            mViewPager = (ViewPager) findViewById(R.id.vp_banner);
            mContent = (TextView) findViewById(R.id.tv_banner_content);
            mPointLayout = (LinearLayout) findViewById(R.id.ll_banner_point);
        }
    }

    /**
     * 初始化banner
     * 图片地址数必须和文字内容条目数相同
     *
     * @param imageUrlList 每个广告条目的图片地址
     * @param contentList  每个广告条目的文字内容
     */
    public void initBanner(@NonNull List<String> imageUrlList, @NonNull List<String> contentList) {
        this.imageUrlList = imageUrlList;
        this.contentList = contentList;
        if (imageUrlList == null || contentList == null || imageUrlList.size() == 0 || contentList
                .size() == 0) {
            throw new IllegalArgumentException("传入图片地址或广告内容不能为空");
        }

        if (imageUrlList.size() != contentList.size()) {
            throw new IllegalArgumentException("传入图片地址或广告内容数量必须一致");
        }

        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        imageViewList = new ArrayList<>();
        View pointView;

        int bannerSize = imageUrlList.size();
        for (int i = 0; i < bannerSize; i++) {
            //加载图片
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //让外部去实现图片加载，实现解耦
            if (imageLoader != null) {
                imageLoader.loadImage(imageView, imageUrlList.get(i));
            }

            imageViewList.add(imageView);

            //底部的小白点
            pointView = new View(getContext());
            //设置背景
            pointView.setBackgroundResource(R.drawable.selector_banner_point);
            //设置小圆点的大小
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtil
                    .dip2px(getContext(), POINT_DEFAULT_SIZE), DensityUtil.dip2px(getContext(),
                    POINT_DEFAULT_SIZE));

            //除第一个以外，其他小白点都需要设置左边距
            if (i != 0) {
                layoutParams.leftMargin = DensityUtil.dip2px(getContext(), POINT_DEFAULT_SIZE / 2);
                pointView.setEnabled(false); //默认小白点是不可用的
            }

            pointView.setLayoutParams(layoutParams);
            mPointLayout.addView(pointView);  //添加到linearLayout中
        }

        BannerAdapter bannerAdapter = new BannerAdapter();
        mViewPager.setAdapter(bannerAdapter);
        //页面切换监听器
        mViewPager.addOnPageChangeListener(this);

        //将ViewPager的起始位置放在  一个很大的数处，那么一开始就可以往左划动了   那个数必须是imageUrlList.size()的倍数
        int remainder = (Integer.MAX_VALUE / 2) % imageUrlList.size();
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - remainder);
        //文本默认为第一项
        mContent.setText(contentList.get(0));
        mPointLayout.getChildAt(0).setEnabled(true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        int newPosition = position % imageUrlList.size();

        //当页面切换时，将底部白点的背景颜色换掉
        mPointLayout.getChildAt(newPosition).setEnabled(true);
        mPointLayout.getChildAt(lastPosition).setEnabled(false);
        //文字内容替换掉
        mContent.setText(contentList.get(newPosition));
        lastPosition = newPosition;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouched = true;   //正在触摸  按下
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isTouched = false;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 延时的任务
     */
    Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {
            //用户在触摸时不能进行自动滑动
            if (!isTouched) {
                //ViewPager设置为下一项
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
            if (pollingEnable) {
                //继续延迟切换广告
                mHandler.postDelayed(delayRunnable, BANNER_SWITCH_DELAY_MILLIS);
            }
        }
    };

    /**
     * banner中ViewPager的adapter
     */
    private class BannerAdapter extends PagerAdapter {

        /**
         * 返回资源一共有的条目数
         */
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        /**
         * 复用判断逻辑
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final int newPosition = position % imageUrlList.size();
            ImageView imageView = imageViewList.get(newPosition);

            //设置点击事件
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //回调
                    if (listener != null) {
                        listener.onItemClick(newPosition, contentList.get(newPosition));
                    }
                }
            });
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 设置banner的item点击事件
     *
     * @param listener OnItemClickListener
     */
    public void setOnItemClickListener(@NonNull OnItemClickListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Item监听器不能为空！");
        }
        this.listener = listener;
    }

    /**
     * 设置图片加载器  --必须设置 否则图片不会加载出来
     *
     * @param imageLoader
     */
    public void setImageLoader(@NonNull ImageLoader imageLoader) {
        if (imageLoader == null) {
            throw new IllegalArgumentException("图片加载器不能为空");
        }
        this.imageLoader = imageLoader;
        if (imageViewList != null) {
            int imageSize = imageViewList.size();
            for (int i = 0; i < imageSize; i++) {
                imageLoader.loadImage(imageViewList.get(i), imageUrlList.get(i));
            }
        }
    }

    /**
     * Item点击的”监听器“
     */
    public interface OnItemClickListener {
        /**
         * 点击item时的回调函数
         *
         * @param position 当前点击item的索引
         * @param title    当前点击item的标题
         */
        void onItemClick(int position, String title);
    }

    /**
     * 向外部暴露的图片加载器，外界需要通过Glide或者其他方式来进行网络加载图片
     */
    public interface ImageLoader {
        /**
         * 加载图片
         *
         * @param imageView ImageView
         * @param url       图片地址
         */
        void loadImage(ImageView imageView, String url);
    }

    /**
     * 获取当前轮播图是否在轮播
     * true:正在轮播  false:没有在轮播
     */
    public boolean isPollingEnable() {
        return pollingEnable;
    }

    /**
     * 开始轮播
     */
    public void start() {
        // 之前已经开启轮播  无需再开启
        if (pollingEnable){
            return;
        }
        pollingEnable = true;
        mHandler.postDelayed(delayRunnable, BANNER_SWITCH_DELAY_MILLIS);
    }

    /**
     * 结束轮播
     */
    public void stop() {
        pollingEnable = false;
        isTouched = false;
        //移除Handler Callback 和 Message 防止内存泄漏
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 重新设置数据
     * @param imageUrlList 图片地址集合
     * @param contentList 标题集合
     */
    public void resetData(@NonNull List<String> imageUrlList, @NonNull List<String> contentList) {
        this.imageUrlList = imageUrlList;
        this.contentList = contentList;
        if (imageUrlList == null || contentList == null || imageUrlList.size() == 0 || contentList
                .size() == 0) {
            throw new IllegalArgumentException("传入图片地址或广告内容不能为空");
        }

        if (imageUrlList.size() != contentList.size()) {
            throw new IllegalArgumentException("传入图片地址或广告内容数量必须一致");
        }

        //判断是否之前在轮播
        if (pollingEnable) {
            //停止之前的轮播
            stop();
        }
        //开始新的轮播
        start();
    }
}
