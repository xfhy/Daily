# APP架构

> 参考一:https://juejin.im/entry/58b39f01b123db0052d1764c

> 参考二:https://github.com/wwah/AndroidBasicLibs

# 1.底层依赖库

## AndroidBasicLibs

首先，我们整体的思路是让现在开发的APP去依赖AndroidBasicLibs，我们尽量做到精简只需要在Gradle依赖它就行了。

AndroidBasicLibs里面有3个东西,basekit、common、uihelper

## basekit

basekit是一个基于MVP+RXjava的的基础框架，它承载了MVP的设计风格，我们的上层APP只需要继承它的BaseActivity、BaseModel 、IBaseView 、BasePresenter 就可以了。上层要做的仅仅只是往对应的层填充独有的业务。如下图5是Base。


## common

common是一个通用型的工具箱集，它可以对上层所依赖的第三方框架做解耦操作。如何理解这句话呢。假如我们要做图片加载，图片加载框架你肯定不会自己重复造轮子。在没有common层时。项目1.0的依赖的是ImageLoader，2.0的时候发现这个库有BUG需要将ImageLoader替换成主流的glide框架。但因为项目中多处都有使用到ImageLoader的API。无法做到加载图片处只改一行代码，就能让所有的业务都换了新的加载工具。这时common的威力就显而易见。，中间做解耦，上层调用common。common调用第三方依赖。当然这只是一个列子。还有很多地方都可以进行二次封装。比如网络请求、视图动画等我们把这些封装都放在common。

### uihelper

uihelper是一个自定义View的依赖。所有不含业务型的View都应该放在这个module。

# 2. 主界面

全部放在MainActivity中

### 知乎模块

整体一个fragment包含一个ViewPager,然后一个ViewPager包含4个fragment.
分别为日报,主题,专栏,热门.
