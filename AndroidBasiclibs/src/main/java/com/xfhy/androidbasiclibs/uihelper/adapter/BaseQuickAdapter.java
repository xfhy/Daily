package com.xfhy.androidbasiclibs.uihelper.adapter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xfhy.androidbasiclibs.uihelper.adapter.loadmore.LoadMoreView;
import com.xfhy.androidbasiclibs.uihelper.adapter.loadmore.SimpleLoadMoreView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

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

    public static final int ALPHAIN = 0x00000001;
    public static final int SCALEIN = 0x00000002;
    public static final int SLIDEIN_BOTTOM = 0x00000003;
    public static final int SLIDEIN_LEFT = 0x00000004;
    public static final int SLIDEIN_RIGHT = 0x00000005;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemChildClickListener mOnItemChildClickListener;
    private onItemChildLongClickListener mOnItemChildLongClickListener;

    @IntDef({ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }

    private boolean mFirstOnlyEnable = true;
    private boolean mOpenAnimationEnable = false;
    private Interpolator mInterpolator = new LinearInterpolator();
    //设置动画时间
    private int mDuration = 300;
    //
    private int mLastPosition = -1;

    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();

    //RecyclerView的头部、底部
    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;

    private FrameLayout mEmptyLayout;
    private boolean mIsUseEmpty = true;
    private boolean mHeadAndEmptyEnable;
    private boolean mFootAndEmptyEnable;

    protected static final String TAG = BaseQuickAdapter.class.getSimpleName();
    protected Context context;
    protected int mLayoutResId;
    protected LayoutInflater mLayoutInflater;
    //数据源
    protected List<T> mData;
    public static final int HEADER_VIEW = 0x00000111;
    public static final int LOADING_VIEW = 0x00000222;
    public static final int FOOTER_VIEW = 0x00000333;
    public static final int EMPTY_VIEW = 0x0000555;

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
     *
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
     *
     * @param requestLoadMoreListener
     */
    public void setOnLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener) {
        openLoadMore(requestLoadMoreListener);
    }

    /**
     * 初始化加载更多
     *
     * @param requestLoadMoreListener
     */
    private void openLoadMore(RequestLoadMoreListener requestLoadMoreListener) {
        this.mRequestLoadMoreListener = requestLoadMoreListener;
        mNextLoadEnable = true;
        mLoadMoreEnable = true;
        mLoading = false;
    }

    /**
     * 设置“加载更多”的监听事件的方法
     *
     * @param requestLoadMoreListener
     * @param recyclerView
     */
    public void setOnLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener, RecyclerView recyclerView) {
        openLoadMore(requestLoadMoreListener);
        if (getRecyclerView() == null) {
            setRecyclerView(recyclerView);
        }
    }

    public void disableLoadMoreIfNotFullPage() {
        checkNotNull();
        disableLoadMoreIfNotFullPage(getRecyclerView());
    }

    /**
     * 如果RecyclerView布局中的数据没有占满一页，则不显示加载更多这个视图
     *
     * @param recyclerView
     */
    public void disableLoadMoreIfNotFullPage(RecyclerView recyclerView) {
        setEnableLoadMore(false);
        if (recyclerView == null) return;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null) return;
        if (manager instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ((linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1) != getItemCount()) {
                        setEnableLoadMore(true);
                    }
                }
            }, 50);
        } else if (manager instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) manager;
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final int[] positions = new int[staggeredGridLayoutManager.getSpanCount()];
                    staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(positions);
                    int pos = getTheBiggestNumber(positions) + 1;
                    if (pos != getItemCount()) {
                        setEnableLoadMore(true);
                    }
                }
            }, 50);
        }
    }

    /**
     * 获取RecyclerView中瀑布布局的最长一列
     *
     * @param numbers
     * @return
     */
    private int getTheBiggestNumber(int[] numbers) {
        int tmp = -1;
        if (numbers == null || numbers.length == 0) {
            return tmp;
        }
        for (int num : numbers) {
            if (num > tmp) {
                tmp = num;
            }
        }
        return tmp;
    }

    /**
     * 给adapter设置手指离开界面时的监听事件
     */
    private boolean mUpFetchEnable;
    private boolean mUpFetching;
    private UpFetchListener mUpFetchListener;

    public boolean ismUpFetchEnable() {
        return mUpFetchEnable;
    }

    public void setmUpFetchEnable(boolean mUpFetchEnable) {
        this.mUpFetchEnable = mUpFetchEnable;
    }

    public boolean ismUpFetching() {
        return mUpFetching;
    }

    public void setmUpFetching(boolean mUpFetching) {
        this.mUpFetching = mUpFetching;
    }

    public UpFetchListener getmUpFetchListener() {
        return mUpFetchListener;
    }

    public void setmUpFetchListener(UpFetchListener mUpFetchListener) {
        this.mUpFetchListener = mUpFetchListener;
    }

    /**
     * 从位置1到mStartUpFetchPosition的位置手指离开界面事件有效，开始时，默认位置为1
     */
    private int mStartUpFetchPosition = 1;

    public void setmStartUpFetchPosition(int mStartUpFetchPosition) {
        this.mStartUpFetchPosition = mStartUpFetchPosition;
    }

    private void autoUpFetch(int positions) {
        if (!ismUpFetchEnable() || ismUpFetching()) {
            return;
        }
        if (positions <= mStartUpFetchPosition && mUpFetchListener != null) {
            mUpFetchListener.onUpFetch();
        }
    }

    public interface UpFetchListener {
        void onUpFetch();
    }

    /**
     * 设置最底部没有动画的item位置
     *
     * @param count
     */
    public void setNotDoAnimationCount(int count) {
        mLastPosition = count;
    }

    /**
     * 设置加载布局
     *
     * @param mLoadMoreView
     */
    public void setmLoadMoreView(LoadMoreView mLoadMoreView) {
        this.mLoadMoreView = mLoadMoreView;
    }

    /**
     * 设置“加载更多”视图是否显示
     *
     * @param enable
     */
    public void setEnableLoadMore(boolean enable) {
        int oldLoadMoreCount = getLoadMoreViewCount();
        mLoadMoreEnable = enable;
        int newLoadMoreCount = getLoadMoreViewCount();

        if (oldLoadMoreCount == 1) {
            if (newLoadMoreCount == 0) {
                notifyItemRemoved(getLoadMoreViewPosition());
            }
        } else {
            if (newLoadMoreCount == 1) {
                mLoadMoreView.setmLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
                notifyItemInserted(getLoadMoreViewPosition());
            }
        }
    }

    /**
     * 获取“加载更多”视图的位置
     *
     * @return
     */
    public int getLoadMoreViewPosition() {
        return getHeaderLayoutCount() + mData.size() + getFooterLayoutCount();
    }

    /**
     * 获取尾部的个数
     *
     * @return
     */
    public int getFooterLayoutCount() {
        if (mFooterLayout == null || mFooterLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 获取头部的个数
     *
     * @return
     */
    public int getHeaderLayoutCount() {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 获取“加载更多”视图的个数
     *
     * @return
     */
    public int getLoadMoreViewCount() {
        if (mRequestLoadMoreListener == null || !mLoadMoreEnable) {
            return 0;
        }
        if (!mNextLoadEnable && mLoadMoreView.ismLoadEndMoreGone()) {
            return 0;
        }
        if (mData.size() == 0) {
            return 0;
        }
        return 1;
    }

    public boolean ismLoading() {
        return mLoading;
    }

    /**
     * 刷新到最后，没有更多数据可以加载
     */
    public void loadMoreEnd() {
        loadMoreEnd(false);
    }

    private void loadMoreEnd(boolean gone) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mNextLoadEnable = false;
        mLoadMoreView.setmLoadMoreEndGone(gone);
        if (gone) {
            notifyItemRemoved(getLoadMoreViewPosition());
        } else {
            mLoadMoreView.setmLoadMoreStatus(LoadMoreView.STATUS_END);
            notifyItemChanged(getLoadMoreViewPosition());
        }
    }

    /**
     * 刷新完成
     */
    public void loadMoreComplete() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mNextLoadEnable = true;
        mLoadMoreView.setmLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * 刷新失败
     */
    public void loadMoreFail() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mLoadMoreView.setmLoadMoreStatus(LoadMoreView.STATUS_FAIL);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * 返回加载更多布局的状态
     *
     * @return
     */
    public boolean ismLoadMoreEnable() {
        return mLoadMoreEnable;
    }

    /**
     * 设置动画时间
     *
     * @param mDuration
     */
    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    /**
     * 初始化Adapter
     *
     * @param layoutResId
     * @param data
     */
    public BaseQuickAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
    }

    public BaseQuickAdapter(@Nullable List<T> data) {
        this(0, data);
    }

    public BaseQuickAdapter(@LayoutRes int layoutResId) {
        this(layoutResId, null);
    }

    /**
     * 重新设置新的数据源
     *
     * @param data
     */
    public void setNewData(@Nullable List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        if (mRequestLoadMoreListener != null) {
            mNextLoadEnable = true;
            mLoadMoreEnable = true;
            mLoading = false;
            mLoadMoreView.setmLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        }
        mLastPosition = -1;
        notifyDataSetChanged();
    }

    /**
     * 加入新的数据
     *
     * @param position
     * @param item
     */
    public void add(@IntRange(from = 0) int position, @NonNull T item) {
        addData(position, item);
    }

    public void addData(@IntRange(from = 0) int position, @NonNull T data) {
        mData.add(position, data);
        notifyItemInserted(position + getHeaderLayoutCount());
        compatibilityDataSizeChanged(1);
    }

    public void addData(@NonNull T data) {
        mData.add(data);
        notifyItemInserted(mData.size() + getHeaderLayoutCount());
        compatibilityDataSizeChanged(1);
    }

    /**
     * 移除对应位置的数据源和子视图
     *
     * @param position
     */
    public void remove(@IntRange(from = 0) int position) {
        mData.remove(position);
        int internalPosition = position + getHeaderLayoutCount();
        notifyItemRemoved(internalPosition);
        compatibilityDataSizeChanged(0);
        notifyItemRangeChanged(internalPosition, mData.size() - internalPosition);
    }

    /**
     * 插入数据源
     *
     * @param index
     * @param data
     */
    public void setData(@IntRange(from = 0) int index, @NonNull T data) {
        mData.set(index, data);
        notifyItemChanged(index + getHeaderLayoutCount());
    }

    private void compatibilityDataSizeChanged(int i) {

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
