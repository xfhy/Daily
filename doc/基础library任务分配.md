# 任务分配

## 1.activity

- BaseActivity
	- 销毁时取消网络访问
	- 封装fragment返回栈
	- 继承自RxAppCompatActivity(Android 性能优化之利用 Rxlifecycle 解决 RxJava 内存泄漏 [参考地址](https://juejin.im/entry/58290ea2570c35005878ce8f))
- AppManager 管理所有的Activity
	- 单例模式
	- 添加Activity
	- 查找Activity
	- 获取当前Activity
	- 结束指定Activity
	- 结束多个Activity
	- 结束所有Activity
	- 结束指定之外的Activity
- BaseMvpActivity  MVP界面的
	- 泛型:BasePresenter
	- 继承:BaseActivity
	- 实现:BaseView

## 2.fragment

- BaseFragment
- BaseMVPFragment 
	- 泛型:P extends BasePresenter
	- 继承:BaseFragment
	- 实现:BaseView

## 3.presenter

- BasePresenter (接口)
	- 泛型:V extends View
- BaseView 
	- void showErrorMsg(String msg);
	- void showErrorView(String msg);

## 4.adapter

> 由于这个项目太多地方需要用到RecyclerView,所以封装一下Adapter很有必要.

## 写一个 BaseViewHolder 

这里对TextView、ImageView等常用控件的一些常用方法进行了封装。有了这个通用ViewHolder，再多的Adapter也只需要这一个ViewHolder，不用每写一个Adapter就要写一个ViewHolder。当然,需要支持自定义.

- [参考地址1](https://juejin.im/entry/57f89cc8a0bb9f00582da719)
- [参考地址2](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/library/src/main/java/com/chad/library/adapter/base/BaseViewHolder.java)

## 写一个 BaseQuickAdapter

	public abstract class BaseQuickAdapter<T, K extends BaseViewHolder> extends RecyclerView.Adapter<K>

- [参考地址1](http://www.jianshu.com/p/b343fcff51b0)
- [参考地址2](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/library/src/main/java/com/chad/library/adapter/base/BaseQuickAdapter.java)

- 添加Item事件


	Item的点击事件
	
	Item的长按事件
	
	Item子控件的点击事件
	
	Item子控件的长按事件

- 添加列表加载动画

	一行代码轻松开启动画。

- 添加头部、尾部

	一行代码搞定，感觉又回到ListView时代。

- 自动加载

	上拉加载无需监听滑动事件,可自定义加载布局，显示异常提示，自定义异常提示。同时支持下拉加载。

- 分组布局

	随心定义分组头部。

- 设置空布局

	比Listview的setEmptyView还要好用。

- 添加拖拽、滑动删除

	开启，监听即可，就是这么简单。

- 自定义ViewHolder

	支持自定义ViewHolder，让开发者随心所欲。


