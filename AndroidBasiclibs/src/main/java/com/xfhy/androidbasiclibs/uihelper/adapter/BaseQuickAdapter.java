package com.xfhy.androidbasiclibs.uihelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xfhy.androidbasiclibs.uihelper.adapter.loadmore.LoadMoreView;
import com.xfhy.androidbasiclibs.uihelper.adapter.loadmore.SimpleLoadMoreView;

/**
 * author xingyun
 * create at 2017/9/28 18:20
 * description：
 */
public abstract class BaseQuickAdapter<T, K extends BaseViewHolder> extends RecyclerView.Adapter<K> {

    //load more
    private boolean mNextLoadEnable = false;
    private boolean mLoadMoreEnable = false;
    private boolean mLoading = false;
    //设置简单的加载更多布局
    private LoadMoreView mLoadMoreView = new SimpleLoadMoreView();
    //请求加载更多的事件
    private RequestLoadMoreListener mRequestLoadMoreListener;
    private boolean mEnableLoadMoreEndClick = false;

    //用来存储RecyclerView
    private RecyclerView mRecyclerView;

    /**
     * 给mRecyclerView赋值
     *
     * @param recyclerView
     */
    private void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    /**
     * 返回mRecyclerView
     *
     * @return
     */
    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 监测RecyclerView是否为空，为空就抛出运行时异常
     */
    private void checkNotNull() {
        if (getRecyclerView() == null) {
            throw new RuntimeException("please bind recyclerView first!");
        }
    }

    /**
     * 用于其他类初始化RecyclerView
     * @param recyclerView
     */
    public void bindToRecyclerView(RecyclerView recyclerView) {
        if (getRecyclerView() != null) {
            throw new RuntimeException("Don't bind twice");
        }
        setRecyclerView(recyclerView);
        getRecyclerView().setAdapter(this);
    }

    /**
     * 请求加载更多的事件
     */
    public interface RequestLoadMoreListener {
        //外部需要实现的方法
        void onLoadMoreRequested();

    }

    /**
     * 设置加载更多的监听事件的方法
     * @param requestLoadMoreListener
     */
    public void setOnLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener) {
        openLoadMore(requestLoadMoreListener);
    }

    private void openLoadMore(RequestLoadMoreListener requestLoadMoreListener) {

    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(K holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
