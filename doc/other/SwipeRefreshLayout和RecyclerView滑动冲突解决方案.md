# SwipeRefreshLayout和RecyclerView滑动冲突解决方案

参考:http://www.jianshu.com/p/34cbaddb668b

## 1.界面内只有RecyclerView和SwipeRefreshLayout

```java
mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //判断RecyclerView第一项是否是当前的第一项
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 :
                                recyclerView.getChildAt(0).getTop();
                mRefreshLayout.setEnabled(topRowVerticalPosition >= 0);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
```
## 2.界面内还有其他view

```xml
<android.support.v4.widget.SwipeRefreshLayout>
        <android.support.design.widget.CoordinatorLayout>
            <android.support.design.widget.AppBarLayout>
                <android.support.design.widget.CollapsingToolbarLayout>
                </android.support.design.widget.CollapsingToolbarLayout>
            </android.support.design.widget.AppBarLayout>
            <android.support.v7.widget.RecyclerView>
            </android.support.v7.widget.RecyclerView>
        </android.support.design.widget.CoordinatorLayout>

    </android.support.v4.widget.SwipeRefreshLayout>
```
比如是上面这张结构的,RecyclerView上面还有AppBarLayout.

则只需要给AppBarLayout添加偏移改变监听器,如果竖直的偏移大于0,则设置SwipeRefreshLayout为可用.
当我在AppBarLayout可见时,手机由下往上滑动时,verticalOffset<0;
当手指由上往下,滑动到顶部,这时手指再由上往下滑动时,verticalOffset=0,
这时可以设置SwipeRefreshLayout为可用了.

```java
mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mRefreshLayout.setEnabled(verticalOffset >= 0);
            }
        });
```