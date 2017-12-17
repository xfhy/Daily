> 2017年12月17日15:25:23

## 项目中加入了RxPresenter,没有使用RxLife了.

在RxPresenter中加入CompositeDisposable.如果有多个Disposable , 
RxJava中已经内置了一个容器CompositeDisposable,每当我们得到一个Disposable时就调用
CompositeDisposable.add()将它添加到容器中, 在退出的时候, 调用CompositeDisposable
.clear() 即可切断所有的水管.

大体代码如下:

```java
public class RxPresenter<V extends BaseView> implements BasePresenter<V> {

    protected Reference<V> mView;
    /*
    * 如果有多个Disposable , RxJava中已经内置了一个容器CompositeDisposable,
    * 每当我们得到一个Disposable时就调用CompositeDisposable.add()将它添加到容器中, 在退出的时候, 调用CompositeDisposable
    * .clear() 即可切断所有的水管.
    * */
    protected CompositeDisposable mCompositeDisposable;

    @Override
    public void attachView(V view) {
        this.mView = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView.clear();
            mView = null;
        }
        unSubscribe();
    }

    @Override
    public V getView() {
        return mView.get();
    }

    /**
     * 切断水管,使得下游收不到事件
     */
    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    /**
     * 添加到容器中,方便控制
     *
     * @param disposable 用于解除订阅
     */
    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}

```

这样设计的好处是,可以让Presenter去继承该类,然后在访问网络或者数据库的时候将Disposable添加
到容器(addSubscribe())中,然后在BaseMvpActivity中的onDestroy()中调用Presenter的detachView()切断所有水管.
防止造成内存泄露.