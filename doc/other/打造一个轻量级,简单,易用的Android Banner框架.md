# 打造一个轻量级,简单,易用的Android Banner框架

由于本人在写一个项目(项目地址: https://github.com/xfhy/Daily )的时候,需要用到banner,用于显示首页的轮播图,但是我又不想让项目导入过多的开源框架,于是自己动手撸一个.我设计得很简单,可能该存在很多的不足,欢迎大家批评指正.

no Picture no bb
![](http://olg7c0d2n.bkt.clouddn.com/17-10-15/60958465.jpg)

### 已实现的功能
* 自动轮播
* 无限左划右划
* 加载网络图片交给外部调用者（实现解耦），加载标题
* 底部小白点可切换，大小可换，数量可动态增加
* 触摸时不能滑动
* 实现banner的item点击事件
* 广告数目可以随意
* 可以在代码中生成banner，也可以在xml布局中生成

## 简单使用

> 使用前将该EasyBanner 这个module导入到自己的项目，并在app主项目中依赖此module.

### 1.在xml中使用

``` xml
<com.xfhy.easybanner.ui.EasyBanner
    android:id="@+id/eb_banner"
    android:layout_width="match_parent"
    android:layout_height="200dp"/>
```

``` java
//可以在布局里面写
mBanner = (EasyBanner) findViewById(R.id.eb_banner);
//设置图片url和图片标题
        mBanner.initBanner(getImageUrlData(), getContentData());
```

### 2.在代码中使用banner

``` java
//也可以直接动态生成
EasyBanner easyBanner = new EasyBanner(this);
//设置图片url和图片标题
easyBanner.initBanner(getImageUrlData(), getContentData());
mRootView.addView(easyBanner,new LinearLayout.LayoutParams(LinearLayout
        .LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this,200)));
```


### 3.设置图片加载器（必须）

``` java
//设置图片加载器
        mBanner.setImageLoader(new EasyBanner.ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Glide.with(mContext).load(url).into(imageView);
            }
        });
```

### 4.实现点击事件(非必须)

``` java

	//监听banner的item点击事件
	mBanner.setOnItemClickListener(new EasyBanner.OnItemClickListener() {
	    @Override
	    public void onItemClick(int position, String title) {
	        Toast.makeText(MainActivity.this, "position:"+position+"   title:"+title, Toast
	                .LENGTH_SHORT).show();
	    }
	});

```

基本使用就是这样，详情请看demo: https://github.com/xfhy/EasyBanner

## 原理

1. 首先我们分析,banner是可以随便左右滑动的,而且可以点击.所以我想到用ViewPager来实现,然后banner里面的图片我就在ViewPager的每个页面中放一个ImageView;接下来就是上面的标题了,我直接用的TextView,因为同时显示的标题只有1个,所以用一个textView就够了;然后最底部的小圆点,我是这样做的:用一个LinearLayout将它们全部装进来,有多少我装多少,这样就可以动态扩展.至于小圆点可以变换颜色直接用selector实现.

请看布局代码
``` xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/banner_root_view"
    android:layout_width="match_parent"
    android:layout_height="200dp">

    <!--轮播图布局-->

    <!--用于播放广告轮播图-->
    <android.support.v4.view.ViewPager
        android:id="@+id/vp_banner"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

    <!--标题-->
    <TextView
        android:id="@+id/tv_banner_content"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="10dp"
        android:layout_marginStart="10dp"
        android:clickable="false"
        android:ellipsize="end"
        android:enabled="false"
        android:maxLines="2"
        android:textColor="@android:color/white"
        android:textSize="20sp"
        app:layout_constraintBottom_toTopOf="@+id/ll_banner_point"/>

    <!--下方的小圆点布局-->
    <LinearLayout
        android:id="@+id/ll_banner_point"
        android:layout_width="match_parent"
        android:layout_height="20dp"
        android:layout_marginBottom="10dp"
        android:clickable="false"
        android:gravity="center"
        android:orientation="horizontal"
        app:layout_constraintBottom_toBottomOf="parent"/>

</android.support.constraint.ConstraintLayout>
```

2.初始化:传入了多少个数据就初始化多少个ImageView,然后放进图片List中先保存起来,并且循环遍历让外部去实现该图片的加载(加载网络图片);循环遍历时将小圆点依次加入到底部的LinearLayout中;设置ViewPager的页面切换监听器,为了切换选择界面的小圆点的颜色;并设置ViewPager的当前item为一个很大的数,这样就可以无限地左滑右滑了(哈哈,有点投机取巧....)

下面是初始化代码
```java
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

        //延时
        mHandler.postDelayed(delayRunnable, BANNER_SWITCH_DELAY_MILLIS);

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
```

3.实现自动延时切换,这个就比较简单了,直接用Handler的postDelayed()就行.当执行完这次任务,又进行一次postDelayed(),这样就可以无限轮播了.

```java
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
            //继续延迟切换广告
            mHandler.postDelayed(delayRunnable, BANNER_SWITCH_DELAY_MILLIS);
        }
    };
```

4.最后是ViewPager的adapter

首先,由于之前我们设置的初始item值比较大,所以getCount()也返回一个很大的数.这样就可以随便滑动了.
然后再设置ImageView的点击事件,即可简单地实现ViewPager的item点击事件....这里感觉有点不妥.
之前我是用onTouchEvent()来实现的点击事件,但是这样也不是很妥.
关于ViewPager的item点击事件,暂时没有想到其他的好方法,大家有什么好方法么?

``` java
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
```

## 总结

基本情况就是这样,实现得比较简单,可能还有很多问题存在.欢迎大家指正.框架地址:https://github.com/xfhy/EasyBanner

