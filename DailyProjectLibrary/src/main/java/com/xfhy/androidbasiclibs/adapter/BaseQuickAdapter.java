package com.xfhy.androidbasiclibs.adapter;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xfhy.androidbasiclibs.adapter.animation.AlphaInAnimation;
import com.xfhy.androidbasiclibs.adapter.animation.BaseAnimation;
import com.xfhy.androidbasiclibs.adapter.animation.ScaleInAnimation;
import com.xfhy.androidbasiclibs.adapter.animation.SlideInBottomAnimation;
import com.xfhy.androidbasiclibs.adapter.animation.SlideInLeftAnimation;
import com.xfhy.androidbasiclibs.adapter.animation.SlideInRightAnimation;
import com.xfhy.androidbasiclibs.adapter.animation.SlideInTopAnimation;
import com.xfhy.androidbasiclibs.adapter.loadmore.LoadMoreView;
import com.xfhy.androidbasiclibs.adapter.loadmore.SimpleLoadMoreView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * author feiyang
 * create at 2017/10/16 15:45
 * description：简单的封装BaseAdapter
 * 泛型:  T:bean类  K:ViewHolder(如果不是自定义则直接用BaseViewHolder)
 */
public abstract class BaseQuickAdapter<T, K extends BaseViewHolder> extends RecyclerView
        .Adapter<K> {

    //load more
    /**
     * 可进行下一页加载
     */
    private boolean mNextLoadEnable = false;
    /**
     * 可进行加载更多
     */
    private boolean mLoadMoreEnable = false;
    /**
     * 当前加载状态 是否正在加载
     */
    private boolean mLoading = false;
    /**
     * 加载更多布局  默认是实现了一个简单布局,当然也可以自己实现
     */
    private LoadMoreView mLoadMoreView = new SimpleLoadMoreView();
    /**
     * 加载更多回调
     */
    private RequestLoadMoreListener mRequestLoadMoreListener;
    private boolean mEnableLoadMoreEndClick = false;
    /**
     * 当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
     */
    private int mPreLoadNumber = 1;

    protected Context mContext;
    /**
     * 数据集合
     */
    protected List<T> mData;
    /**
     * RecyclerView中普通item的布局id
     */
    protected int mLayoutResId;
    protected LayoutInflater mLayoutInflater;

    //item类型
    public static final int HEADER_VIEW = 0x00000111;
    public static final int LOADING_VIEW = 0x00000222;
    public static final int FOOTER_VIEW = 0x00000333;
    public static final int EMPTY_VIEW = 0x00000555;

    /**
     * 子项item点击事件
     */
    private OnItemClickListener onItemClickListener;
    /**
     * 子项item长按事件
     */
    private OnItemLongClickListener onItemLongClickListener;

    //header footer
    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;
    /**
     * if asFlow is true, footer/header will arrange like normal item view.
     * only works when use {@link GridLayoutManager},and it will ignore span size.
     * 如果asFlow是true的话,那么footer和header就像正常item一样,不会跨区域
     * 默认是false,header和footer都占满屏幕
     */
    private boolean headerViewAsFlow, footerViewAsFlow;
    private SpanSizeLookup mSpanSizeLookup;

    //empty
    /**
     * 用来存放EmptyView的容器
     */
    private FrameLayout mEmptyLayout;
    /**
     * 是否使用EmptyView
     */
    private boolean mIsUseEmpty = true;
    /**
     * 头布局和空布局共存?
     */
    private boolean mHeadAndEmptyEnable;
    /**
     * 尾布局和空布局共存
     */
    private boolean mFootAndEmptyEnable;

    //Animation
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int ALPHAIN = 0x00000001;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SCALEIN = 0x00000002;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_BOTTOM = 0x00000003;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_LEFT = 0x00000004;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_RIGHT = 0x00000005;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_TOP = 0x00000006;

    @IntDef({ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT, SLIDEIN_TOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }

    /**
     * 方向
     */
    @IntDef({LinearLayout.HORIZONTAL, LinearLayout.VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    /**
     * 动画只执行1次?
     */
    private boolean mFirstOnlyEnable = true;
    /**
     * 开启了动画?
     */
    private boolean mOpenAnimationEnable = false;
    private Interpolator mInterpolator = new LinearInterpolator();
    /**
     * 动画播放时长
     */
    private int mDuration = 300;
    /**
     * 上一个在播放动画的item的位置
     */
    private int mLastPosition = -1;

    /**
     * 自定义的动画
     */
    private BaseAnimation mCustomAnimation;
    /**
     * 当前选择使用哪种动画,这里默认是渐显
     */
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();

    private RecyclerView mRecyclerView;

    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    private void checkNotNull() {
        if (getRecyclerView() == null) {
            throw new RuntimeException("please bind recyclerView first!");
        }
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
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

    /*--------------------------header-footer----------------------------------*/

    /**
     * if addHeaderView will be return 1, if not will be return 0
     * 添加了header则返回1   没有header则返回0
     */
    public int getHeaderLayoutCount() {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * if addFooterView will be return 1, if not will be return 0
     */
    public int getFooterLayoutCount() {
        if (mFooterLayout == null || mFooterLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * Return root layout of header
     */

    public LinearLayout getHeaderLayout() {
        return mHeaderLayout;
    }

    /**
     * Return root layout of footer
     */
    public LinearLayout getFooterLayout() {
        return mFooterLayout;
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        K baseViewHolder = null;
        this.mContext = parent.getContext();
        this.mLayoutInflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case LOADING_VIEW:
                baseViewHolder = getLoadingView(parent);
                break;
            case HEADER_VIEW:
                baseViewHolder = createBaseViewHolder(mHeaderLayout);
                break;
            case EMPTY_VIEW:
                baseViewHolder = createBaseViewHolder(mEmptyLayout);
                break;
            case FOOTER_VIEW:
                baseViewHolder = createBaseViewHolder(mFooterLayout);
                break;
            default:
                baseViewHolder = onCreateDefViewHolder(parent, viewType);
                bindViewClickListener(baseViewHolder);
        }
        return baseViewHolder;
    }

    private K getLoadingView(ViewGroup parent) {
        //加载 加载布局
        View view = getItemView(mLoadMoreView.getLayoutId(), parent);
        //生成baseviewholder
        K holder = createBaseViewHolder(view);
        //设置加载布局的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {
                    //之前是加载失败状态时   前去刷新
                    notifyLoadMoreToLoading();
                }
                if (mEnableLoadMoreEndClick && mLoadMoreView.getLoadMoreStatus() == LoadMoreView
                        .STATUS_END) {
                    //加载更多布局可以被点击  并且  之前状态是结束状态
                    notifyLoadMoreToLoading();
                }
            }
        });
        return holder;
    }

    /**
     * The notification starts the callback and loads more
     * 通知启动回调并加载更多
     */
    public void notifyLoadMoreToLoading() {
        //如果当前正在加载中,则不用管
        if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_LOADING) {
            return;
        }
        //将加载更多布局的状态设置为默认状态  这样当下面刷新adapter时会回调onBindViewHolder()从而触发
        //autoLoadMore()方法去判断是否需要加载更多,这时候刚好又是默认状态是可以更新的,于是就去回调onLoadMoreRequested()方法
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * 创建默认的ViewHolder  即中间的数据项的ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected K onCreateDefViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mLayoutResId;
        /*if (mMultiTypeDelegate != null) {  //不同的类型的布局
            layoutId = mMultiTypeDelegate.getLayoutId(viewType);
        }*/
        return createBaseViewHolder(parent, mLayoutResId);
    }

    protected K createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return createBaseViewHolder(getItemView(layoutResId, parent));
    }

    /**
     * @param layoutResId ID for an XML layout resource to load
     * @param parent      Optional view to be the parent of the generated hierarchy or else
     *                    simply an object that
     *                    provides a set of LayoutParams values for root of the returned
     *                    hierarchy
     * @return view will be return
     */
    protected View getItemView(@LayoutRes int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, parent, false);
    }

    /**
     * if you want to use subclass of BaseViewHolder in the adapter,
     * you must override the method to create new ViewHolder.
     * 如果要在适配器中使用BaseViewHolder的子类，
     *       *您必须覆盖该方法才能创建新的ViewHolder。
     *
     * @param view view
     * @return new ViewHolder
     */
    @SuppressWarnings("unchecked")
    protected K createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            //判断z是否是BaseViewHolder的子类或接口  不是则返回null
            z = getInstancedGenericKClass(temp);
            //返回超类
            temp = temp.getSuperclass();
        }
        K k;
        // 泛型擦除会导致z为null
        if (z == null) {
            //为null则说明z不是BaseViewHolder的子类或接口 则创建一个BaseViewHolder
            k = (K) new BaseViewHolder(view);
        } else {
            //尝试创建z的实例   利用反射
            k = createGenericKInstance(z, view);
        }
        return k != null ? k : (K) new BaseViewHolder(view);
    }

    /**
     * get generic parameter K
     * 判断z是否是BaseViewHolder的子类或接口
     *
     * @param z
     * @return
     */
    private Class getInstancedGenericKClass(Class z) {
        //getGenericSuperclass()获得带有泛型的父类
        //Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                //判断tempClass是否是BaseViewHolder类型相同或具有相同的接口
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (BaseViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && BaseViewHolder.class.isAssignableFrom(
                            (Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }

    /**
     * try to create Generic K instance
     * 尝试创建Generic K实例
     *
     * @param z
     * @param view
     * @return
     */
    @SuppressWarnings("unchecked")
    private K createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            //成员类&&非静态类
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                //获取z的构造函数
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                //禁止java语言访问检查
                constructor.setAccessible(true);
                //通过构造方法构造z对象
                return (K) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 绑定item的点击事件
     */
    private void bindViewClickListener(final BaseViewHolder baseViewHolder) {
        if (baseViewHolder == null) {
            return;
        }
        final View view = baseViewHolder.itemView;
        if (view == null) {
            return;
        }
        if (getOnItemClickListener() != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOnItemClickListener().onItemClick(BaseQuickAdapter.this, v, baseViewHolder
                            .getLayoutPosition() - getHeaderLayoutCount());
                }
            });
        }
        if (getOnItemLongClickListener() != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return getOnItemLongClickListener().onItemLongClick(BaseQuickAdapter.this, v,
                            baseViewHolder.getLayoutPosition() - getHeaderLayoutCount());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(K holder, int position) {
        //Do not move position, need to change before LoadMoreView binding
        //判断是否需要进行上拉加载更多
        autoLoadMore(position);

        int viewType = holder.getItemViewType();
        switch (viewType) {
            case 0:
                convert(holder, getItem(position - getHeaderLayoutCount()));
                break;
            case LOADING_VIEW:
                mLoadMoreView.convert(holder);
                break;
            case HEADER_VIEW:
                break;
            case EMPTY_VIEW:
                break;
            case FOOTER_VIEW:
                break;
            default:
                convert(holder, getItem(position - getHeaderLayoutCount()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        int count;
        //如果界面上显示了空布局
        if (getEmptyViewCount() == 1) {
            count = 1;
            //如果header可见  则只需要+1就行了,因为header的布局是LinearLayout
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                count++;
            }
            if (mFootAndEmptyEnable && getFooterLayoutCount() != 0) {
                count++;
            }
        } else {  //未显示空布局
            //         1 or 0                  数据项             1 or 0                 加载更多 1 or 0
            count = getHeaderLayoutCount() + mData.size() + getFooterLayoutCount() +
                    getLoadMoreViewCount();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        /*
        这里有空布局的逻辑一起混起的,干脆一起分析,反正不是很难.

        - 当有空布局的时候,position的值只可能为0,1,2;
        再根据是否显示了header,即可判断出当前position的type应该是什么.
        - 再看没有显示空布局的情况
            - header类型  索引:`< 1`
            - 中间的数据项类型  需要减去header的数量(1 or 0) ;这里面牵涉到了item多种类型的逻辑,
            如果是多种类型的则交由mMultiTypeDelegate去处理,如果不是,则默认返回super.getItemViewType(position);
            - footer类型  减去header的数量(1 or 0),再减去中间数据项的数量
            - 加载中类型 剩下的
         */
        if (getEmptyViewCount() == 1) {
            boolean header = mHeadAndEmptyEnable && getHeaderLayoutCount() != 0;
            switch (position) {
                case 0:
                    if (header) {
                        return HEADER_VIEW;
                    } else {
                        return EMPTY_VIEW;
                    }
                case 1:
                    if (header) {
                        return EMPTY_VIEW;
                    } else {
                        return FOOTER_VIEW;
                    }
                case 2:
                    return FOOTER_VIEW;
                default:
                    return EMPTY_VIEW;
            }
        }
        //1 or 0
        int numHeaders = getHeaderLayoutCount();
        if (position < numHeaders) {
            return HEADER_VIEW;
        } else {
            int adjPosition = position - numHeaders;
            int adapterCount = mData.size();

            //中间的数据项
            if (adjPosition < adapterCount) {
                return getDefItemViewType(adjPosition);
            } else {
                //剩下 footer   加载中布局
                adjPosition = adjPosition - adapterCount;
                int numFooters = getFooterLayoutCount();
                if (adjPosition < numFooters) {
                    return FOOTER_VIEW;
                } else {
                    return LOADING_VIEW;
                }
            }
        }
    }

    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * Append header to the rear of the mHeaderLayout.
     * 默认添加header到headerLayout的底部(索引最大的那个位置)
     *
     * @param header
     */
    public int addHeaderView(@NonNull View header) {
        return addHeaderView(header, -1);
    }

    /**
     * Add header view to mHeaderLayout and set header view position in mHeaderLayout.
     * When index = -1 or index >= child count in mHeaderLayout,
     * the effect of this method is the same as that of {@link #addHeaderView(View)}.
     *
     * @param header
     * @param index  the position in mHeaderLayout of this header.
     *               When index = -1 or index >= child count in mHeaderLayout,
     *               the effect of this method is the same as that of {@link #addHeaderView(View)}.
     */
    public int addHeaderView(@NonNull View header, @IntRange(from = -1) int index) {
        return addHeaderView(header, index, LinearLayout.VERTICAL);
    }

    /**
     * @param header
     * @param index
     * @param orientation
     */
    public int addHeaderView(@NonNull View header, @IntRange(from = -1) int index,
                             @OrientationMode int orientation) {

        // 如果为空 则创建头布局
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(header.getContext());
            // 方向  LayoutParams设置
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
                mHeaderLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                        WRAP_CONTENT));
            } else {
                mHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
                mHeaderLayout.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT,
                        MATCH_PARENT));
            }
        }


        final int childCount = mHeaderLayout.getChildCount();
        // 如果index不合法,则添加到索引最大的位置
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mHeaderLayout.addView(header, index);   //就是添加到LinearLayout中嘛,哈哈,被我猜中啦

        /*
        如果头布局(LinearLayout)中子View(header的item)的数量等于1
         说明这是第一次添加headerLayout进RecyclerView
         需要进行通知刷新操作  告知RecyclerView第一个索引处更新啦
         */
        if (mHeaderLayout.getChildCount() == 1) {
            int position = getHeaderViewPosition();
            if (position != -1) {
                notifyItemInserted(position); //告知RecyclerView第一个索引处更新啦
                // 这时RecyclerView中的第一项已经是headerLayout(LinearLayout)了
            }
        }
        return index;
    }

    public int setHeaderView(@NonNull View header) {
        return setHeaderView(header, 0, LinearLayout.VERTICAL);
    }

    public int setHeaderView(@NonNull View header, @IntRange(from = -1) int index) {
        return setHeaderView(header, index, LinearLayout.VERTICAL);
    }

    public int setHeaderView(@NonNull View header, @IntRange(from = -1) int index,
                             @OrientationMode int orientation) {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() <= index) {
            return addHeaderView(header, index, orientation);
        } else {
            mHeaderLayout.removeViewAt(index);
            mHeaderLayout.addView(header, index);
            return index;
        }
    }

    /**
     * Append footer to the rear of the mFooterLayout.
     *
     * @param footer
     */
    public int addFooterView(@NonNull View footer) {
        return addFooterView(footer, -1, LinearLayout.VERTICAL);
    }

    public int addFooterView(@NonNull View footer, @IntRange(from = -1) int index) {
        return addFooterView(footer, index, LinearLayout.VERTICAL);
    }

    /**
     * Add footer view to mFooterLayout and set footer view position in mFooterLayout.
     * When index = -1 or index >= child count in mFooterLayout,
     * the effect of this method is the same as that of {@link #addFooterView(View)}.
     *
     * @param footer
     * @param index  the position in mFooterLayout of this footer.
     *               When index = -1 or index >= child count in mFooterLayout,
     *               the effect of this method is the same as that of {@link #addFooterView(View)}.
     */
    public int addFooterView(@NonNull View footer, @IntRange(from = -1) int index,
                             @OrientationMode int orientation) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(footer.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                        WRAP_CONTENT));
            } else {
                mFooterLayout.setOrientation(LinearLayout.HORIZONTAL);
                mFooterLayout.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT,
                        MATCH_PARENT));
            }
        }
        final int childCount = mFooterLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mFooterLayout.addView(footer, index);
        if (mFooterLayout.getChildCount() == 1) {
            int position = getFooterViewPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
        return index;
    }

    public int setFooterView(@NonNull View header) {
        return setFooterView(header, 0, LinearLayout.VERTICAL);
    }

    public int setFooterView(@NonNull View header, @IntRange(from = -1) int index) {
        return setFooterView(header, index, LinearLayout.VERTICAL);
    }

    public int setFooterView(@NonNull View header, @IntRange(from = -1) int index,
                             @OrientationMode int orientation) {
        if (mFooterLayout == null || mFooterLayout.getChildCount() <= index) {
            return addFooterView(header, index, orientation);
        } else {
            mFooterLayout.removeViewAt(index);
            mFooterLayout.addView(header, index);
            return index;
        }
    }

    /**
     * remove header view from mHeaderLayout.
     * When the child count of mHeaderLayout is 0, mHeaderLayout will be set to null.
     *
     * @param header
     */
    public void removeHeaderView(@NonNull View header) {
        if (getHeaderLayoutCount() == 0) return;

        mHeaderLayout.removeView(header);
        //如果mHeaderLayout已经没有子View,则直接将mHeaderLayout从RecyclerView中移除
        if (mHeaderLayout.getChildCount() == 0) {
            int position = getHeaderViewPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }
    }

    /**
     * remove footer view from mFooterLayout,
     * When the child count of mFooterLayout is 0, mFooterLayout will be set to null.
     *
     * @param footer
     */
    public void removeFooterView(@NonNull View footer) {
        if (getFooterLayoutCount() == 0) return;

        mFooterLayout.removeView(footer);
        //如果mFooterLayout已经没有子View,则直接将mHeaderLayout从RecyclerView中移除
        if (mFooterLayout.getChildCount() == 0) {
            int position = getFooterViewPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }
    }

    /**
     * remove all header view from mHeaderLayout and set null to mHeaderLayout
     * 移除所有header view
     */
    public void removeAllHeaderView() {
        if (getHeaderLayoutCount() == 0) return;

        mHeaderLayout.removeAllViews();
        int position = getHeaderViewPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }
    }

    /**
     * remove all footer view from mFooterLayout and set null to mFooterLayout
     * 移除所有footer view
     */
    public void removeAllFooterView() {
        if (getFooterLayoutCount() == 0) return;

        mFooterLayout.removeAllViews();
        int position = getFooterViewPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }
    }

    /**
     * 设置header不跨区域  就像正常item
     * true:不跨区域(不占满一行)   false:跨区域(占满一行)
     */
    public void setHeaderViewAsFlow(boolean headerViewAsFlow) {
        this.headerViewAsFlow = headerViewAsFlow;
    }

    public boolean isHeaderViewAsFlow() {
        return headerViewAsFlow;
    }

    /**
     * 设置footer不跨区域  就像正常item
     * true:不跨区域   false:跨区域
     */
    public void setFooterViewAsFlow(boolean footerViewAsFlow) {
        this.footerViewAsFlow = footerViewAsFlow;
    }

    public boolean isFooterViewAsFlow() {
        return footerViewAsFlow;
    }

    /**
     * 判断当前type是否是特殊的type
     */
    protected boolean isFixedViewType(int type) {
        return type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type ==
                LOADING_VIEW;
    }

    /**
     * get the specific view by position,e.g. getViewByPosition(2, R.id.textView)
     * <p>
     * bind recyclerView {@link #bindToRecyclerView(RecyclerView)} before use!
     *
     * @see #bindToRecyclerView(RecyclerView)
     */
    @Nullable
    public View getViewByPosition(int position, @IdRes int viewId) {
        checkNotNull();
        return getViewByPosition(getRecyclerView(), position, viewId);
    }

    @Nullable
    public View getViewByPosition(RecyclerView recyclerView, int position, @IdRes int viewId) {
        if (recyclerView == null) {
            return null;
        }
        BaseViewHolder viewHolder = (BaseViewHolder) recyclerView.findViewHolderForLayoutPosition
                (position);
        if (viewHolder == null) {
            return null;
        }
        return viewHolder.getView(viewId);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * RecyclerView在开始观察该适配器时调用。
     * 请记住，多个RecyclerView可能会观察到相同的适配器。
     * <p>
     * Adapter与RecyclerView关联起来
     * 这里面主要是做表格布局管理器的头布局和脚布局自占一行的适配
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            //设置adapter中每个Item所占用的跨度数
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);

                    //如果当前type为header并且asFlow为true:那么header设置为不跨区域,就和正常item一样.只占1格
                    if (type == HEADER_VIEW && isHeaderViewAsFlow()) {
                        return 1;
                    }
                    if (type == FOOTER_VIEW && isFooterViewAsFlow()) {
                        return 1;
                    }

                    //如果用户没有自定义SpanSizeLookup  SpanSizeLookup是用来查询每个item占用的跨度数的实例
                    if (mSpanSizeLookup == null) {
                        /*
                        1.如果是特殊的type,那么item的跨度设置为当前gridManager的SpanCount
                        即如果我设置的是mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
                        那么当前item占2格的跨度
                        2.如果item不是特殊的item,那么就是占1个格子,不跨
                        */
                        return isFixedViewType(type) ? gridManager.getSpanCount() : 1;
                    } else {
                        /*
                        1.如果是特殊的type,那么item的跨度设置为当前gridManager的SpanCount
                        即如果我设置的是mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
                        那么当前item占2格的跨度
                        2.如果item不是特殊的item,那么交给外部调用者来处理每个item应该占多少个格
                        */
                        return isFixedViewType(type) ? gridManager.getSpanCount() :
                                mSpanSizeLookup.getSpanSize(gridManager,
                                        position - getHeaderLayoutCount());
                    }
                }


            });
        }
    }

    /**
     * 用于外部调用者设置每个item的跨度,除了header,footer,emptyView,loadMoreView
     */
    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

    /**
     * @param spanSizeLookup instance to be used to query number of spans occupied by each item
     *                       用于查询每个item占用的跨度数的实例
     */
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    /*-------------------data操作-----------------------------*/

    /**
     * Get the data of list
     *
     * @return 列表数据
     */
    @NonNull
    public List<T> getData() {
        return mData;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Nullable
    public T getItem(@IntRange(from = 0) int position) {
        return position < mData.size() ? mData.get(position) : null;
    }

    /**
     * 绑定数据
     *
     * @param holder BaseViewHolder
     * @param item   item数据
     */
    protected abstract void convert(BaseViewHolder holder, T item);

    /**
     * 删除item
     *
     * @param position 删除item的位置
     */
    public void removeItem(@IntRange(from = 0) int position) {
        if (mData == null) {
            return;
        }
        if (position >= mData.size()) {
            return;
        }

        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 新增item
     *
     * @param position 新增item位置
     * @param item     item数据
     */
    public void addItem(@IntRange(from = 0) int position, @NonNull T item) {
        if (mData == null) {
            return;
        }
        if (position > mData.size()) {
            return;
        }
        mData.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * add new data in to certain location
     * 添加一个集合的数据
     *
     * @param position the insert position
     * @param newData  the new data collection
     */
    public void addData(@IntRange(from = 0) int position, @NonNull Collection<? extends T>
            newData) {
        mData.addAll(position, newData);
        notifyItemRangeInserted(position + getHeaderLayoutCount(), newData.size());
        compatibilityDataSizeChanged(newData.size());
    }

    /**
     * add new data to the end of mData
     * 添加数据到末尾
     *
     * @param newData the new data collection
     */
    public void addData(@NonNull Collection<? extends T> newData) {
        mData.addAll(newData);
        notifyItemRangeInserted(mData.size() - newData.size() + getHeaderLayoutCount(), newData
                .size());
        compatibilityDataSizeChanged(newData.size());
    }

    /**
     * use data to replace all item in mData. this method is different {@link #setNewData(List)},
     * it doesn't change the mData reference
     *
     * @param data data collection
     */
    public void replaceData(@NonNull Collection<? extends T> data) {
        // 不是同一个引用才清空列表
        if (data != mData) {
            mData.clear();
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clearData(){
        if (mData != null) {
            mData.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     * 判断loadMoreView和emptyView是否已经变化
     *
     * @param size Need compatible data size
     */
    private void compatibilityDataSizeChanged(int size) {
        final int dataSize = mData == null ? 0 : mData.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }

    /**
     * setting up a new instance to data;
     * 设置新数据
     *
     * @param data
     */
    public void setNewData(@Nullable List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;

        //如果当前设置了加载更多监听器
        if (mRequestLoadMoreListener != null) {
            // 状态切换为可加载下一页
            mNextLoadEnable = true;
            mLoadMoreEnable = true;
            mLoading = false;
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        }
        mLastPosition = -1;
        notifyDataSetChanged();
    }

    /*---------------------动画-----------------------*/

    /**
     * up fetch end
     * 设置count个不执行动画
     */
    public void setNotDoAnimationCount(@IntRange(from = 0) int count) {
        mLastPosition = count;
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     * simple to solve item will layout using all
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(K holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type ==
                LOADING_VIEW) {
            //设置为跨区域  比如是StaggeredGridLayoutManager时,header或者footer等应该如何展示
            setFullSpan(holder);
        } else {
            //添加动画到holder的itemView上,并执行动画
            addAnimation(holder);
        }
    }

    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder True if this item should traverse all spans.
     */
    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager
                    .LayoutParams) holder
                    .itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    /**
     * add animation when you want to show time
     * 添加动画到item上并执行动画
     *
     * @param holder
     */
    private void addAnimation(RecyclerView.ViewHolder holder) {
        // 判断是否开启了动画
        if (mOpenAnimationEnable) {
            //  !isFirstOnly            这一次item的位置>上一次加载动画item的位置
            if (!mFirstOnlyEnable || holder.getLayoutPosition() > mLastPosition) {
                BaseAnimation animation = null;
                //判断是否是自定义了动画
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation;
                } else {
                    //没有自定义,则使用默认的动画
                    animation = mSelectAnimation;
                }
                // 遍历定义到holder.itemView上的动画  BaseAnimation使用的getAnimators()是获取动画数组,方便扩展
                // 如果用户需要自定义动画的话,则可以在一个item上同时加入多个动画,然后下面让这些动画依次执行
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    //开启动画
                    startAnim(anim, holder.getLayoutPosition());
                }
                //记录这一次执行动画的item
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    /**
     * set anim to start when loading
     * 开启动画并设置插值器
     *
     * @param anim
     * @param index
     */
    protected void startAnim(Animator anim, int index) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }

    /**
     * Set the view animation type.
     * 设置动画类型
     *
     * @param animationType One of {@link #ALPHAIN}, {@link #SCALEIN}, {@link #SLIDEIN_BOTTOM},
     *                      {@link #SLIDEIN_LEFT}, {@link #SLIDEIN_RIGHT}.
     */
    public void openLoadAnimation(@AnimationType int animationType) {
        //标志着需要加载动画
        this.mOpenAnimationEnable = true;
        //用户没有自定义动画  是使用的默认动画
        mCustomAnimation = null;
        //根据用户传入的类型初始化应该使用哪种动画
        switch (animationType) {
            case ALPHAIN:
                mSelectAnimation = new AlphaInAnimation();
                break;
            case SCALEIN:
                mSelectAnimation = new ScaleInAnimation();
                break;
            case SLIDEIN_BOTTOM:
                mSelectAnimation = new SlideInBottomAnimation();
                break;
            case SLIDEIN_LEFT:
                mSelectAnimation = new SlideInLeftAnimation();
                break;
            case SLIDEIN_RIGHT:
                mSelectAnimation = new SlideInRightAnimation();
                break;
            case SLIDEIN_TOP:
                mSelectAnimation = new SlideInTopAnimation();
                break;
            default:
                break;
        }
    }

    /**
     * Sets the duration of the animation.
     *
     * @param duration The length of the animation, in milliseconds.
     */
    public void setDuration(@IntRange(from = 0) int duration) {
        mDuration = duration;
    }

    /**
     * Set Custom ObjectAnimator
     * 自定义动画
     *
     * @param animation ObjectAnimator
     */
    public void openLoadAnimation(@NonNull BaseAnimation animation) {
        //标志着需要加载动画
        this.mOpenAnimationEnable = true;
        //初始化自定义动画
        this.mCustomAnimation = animation;
    }

    /**
     * To open the animation when loading
     * 开启动画,这种情况下会默认开启:渐显动画
     */
    public void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
    }

    /**
     * {@link #addAnimation(RecyclerView.ViewHolder)}
     * 设置动画是否只加载一次
     *
     * @param firstOnly true just show anim when first loading false show anim when load the data
     *                  every time  true:第一次显示时才加载动画   false:每次都加载动画
     */
    public void isFirstOnly(boolean firstOnly) {
        this.mFirstOnlyEnable = firstOnly;
    }

    /*---------------------------------上拉加载更多--------------------------------------*/

    /**
     * 设置当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested()方法
     *
     * @param preLoadNumber
     */
    public void setPreLoadNumber(@IntRange(from = 1) int preLoadNumber) {
        mPreLoadNumber = preLoadNumber;
    }

    /**
     * @return Whether the Adapter is actively showing load
     * progress.
     */
    public boolean isLoading() {
        return mLoading;
    }

    /**
     * Returns the enabled status for load more.
     *
     * @return True if load more is enabled, false otherwise.
     */
    public boolean isLoadMoreEnable() {
        return mLoadMoreEnable;
    }

    /**
     * 根据position位置判断当前是否需要进行加载更多
     *
     * @param position 当前onBindViewHolder()的Position
     */
    private void autoLoadMore(int position) {
        // 判断是否可以进行加载更多的逻辑
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        //只有在当前列表的倒数mPreLoadNumber个item开始绑定数据时才进行加载更多的逻辑
        if (position < getItemCount() - mPreLoadNumber) {
            return;
        }
        //判断当前加载状态,如果不是默认状态(可能正处于 正在加载中 的状态),则不进行加载
        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_DEFAULT) {
            return;
        }
        //设置当前状态:加载中
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
        if (!mLoading) {
            mLoading = true;
            if (getRecyclerView() != null) {
                getRecyclerView().post(new Runnable() {
                    @Override
                    public void run() {
                        //回调  让调用者去处理加载更多的逻辑
                        mRequestLoadMoreListener.onLoadMoreRequested();
                    }
                });
            } else {
                mRequestLoadMoreListener.onLoadMoreRequested();
            }
        }
    }

    /**
     * Load more view count
     * 判断是否可以进行加载更多的逻辑
     *
     * @return 0 or 1
     */
    public int getLoadMoreViewCount() {
        //参数合法性    加载更多状态
        if (mRequestLoadMoreListener == null || !mLoadMoreEnable) {
            return 0;
        }
        //可加载下一页               有无更多数据
        if (!mNextLoadEnable && mLoadMoreView.isLoadEndMoreGone()) {
            return 0;
        }
        //当前数据项个数
        if (mData.size() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * same as recyclerView.setAdapter(), and save the instance of recyclerView
     */
    public void bindToRecyclerView(@NonNull RecyclerView recyclerView) {
        if (getRecyclerView() != null) {
            throw new RuntimeException("Don't bind twice");
        }
        setRecyclerView(recyclerView);
        getRecyclerView().setAdapter(this);
    }

    /**
     * bind recyclerView {@link #bindToRecyclerView(RecyclerView)} before use!
     *
     * @see #disableLoadMoreIfNotFullPage(RecyclerView)
     */
    public void disableLoadMoreIfNotFullPage() {
        //检查当前RecyclerView是否为null
        checkNotNull();
        disableLoadMoreIfNotFullPage(getRecyclerView());
    }

    /**
     * check if full page after {@link #setNewData(List)}, if full, it will enable load more again.
     * <p>
     * 不是配置项！！
     * <p>
     * 这个方法是用来检查是否满一屏的，所以只推荐在 {@link #setNewData(List)} 之后使用
     * 原理:先关闭 load more，检查完了再决定是否开启
     * 数据项个数未满一屏幕,则不开启load more
     * 数据项个数 > 一屏幕,则继续开启load more
     * <p>
     * 不是配置项！！
     *
     * @param recyclerView your recyclerView
     * @see #setNewData(List)
     */
    public void disableLoadMoreIfNotFullPage(@NonNull RecyclerView recyclerView) {
        // 设置加载状态为false
        setEnableLoadMore(false);
        if (recyclerView == null) return;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null) return;
        if (manager instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //数据项个数 > 一屏幕,则继续开启load more
                    if ((linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1) !=
                            getItemCount()) {
                        setEnableLoadMore(true);
                    }
                }
            }, 50);
        } else if (manager instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager staggeredGridLayoutManager =
                    (StaggeredGridLayoutManager) manager;
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //返回StaggeredGridLayoutManager布局的跨度数
                    final int[] positions = new int[staggeredGridLayoutManager.getSpanCount()];
                    //返回每一个跨度(列)的最后一个可见的item的位置  赋值到该数组里面
                    staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(positions);
                    //找出数组中最大的数(即StaggeredGridLayoutManager布局的当前可见的最下面那个item)
                    int pos = getTheBiggestNumber(positions) + 1;
                    // 数据项个数 > 一屏幕,则继续开启load more
                    if (pos != getItemCount()) {
                        setEnableLoadMore(true);
                    }
                }
            }, 50);
        }
    }

    /**
     * Set the enabled state of load more.
     * 设置上拉加载更多是否可用
     *
     * @param enable True if load more is enabled, false otherwise.
     */
    public void setEnableLoadMore(boolean enable) {
        //之前的状态需要和现在的状态做对比
        int oldLoadMoreCount = getLoadMoreViewCount();
        mLoadMoreEnable = enable;
        int newLoadMoreCount = getLoadMoreViewCount();

        if (oldLoadMoreCount == 1) {
            if (newLoadMoreCount == 0) {
                //之前有 现在没有 需要移除
                notifyItemRemoved(getLoadMoreViewPosition());
            }
        } else {
            if (newLoadMoreCount == 1) {
                //将加载布局插入
                mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
                notifyItemInserted(getLoadMoreViewPosition());
            }
        }
    }

    /**
     * 返回数组中的最大值
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
     * Refresh complete
     * 刷新完成时调用
     */
    public void loadMoreComplete() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        //将当前加载状态改为false  表示未在加载
        mLoading = false;
        //可进行下一页加载
        mNextLoadEnable = true;
        // 恢复加载更多布局的状态
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        // 告知加载更多布局被更新了,需要刷新一下
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * Gets to load more locations
     * 获取加载更多的布局的索引
     *
     * @return
     */
    public int getLoadMoreViewPosition() {
        return getHeaderLayoutCount() + mData.size() + getFooterLayoutCount();
    }

    /**
     * Refresh failed
     * 加载失败
     */
    public void loadMoreFail() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        //当前加载状态  切换为未在加载中
        mLoading = false;
        //加载布局设置为加载失败
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
        //通知加载布局更新了,需要刷新
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * Refresh end, no more data
     * 加载更多,并且没有更多数据了    调用此方法即表示无更多数据了
     * 这里设置加载更多布局(默认布局是显示:客官,没有更多数据了~)依然可见
     */
    public void loadMoreEnd() {
        loadMoreEnd(false);
    }

    /**
     * Refresh end, no more data
     * 加载更多,并且没有更多数据了   调用此方法即表示无更多数据了
     * gone:设置加载更多布局是否可见   true:不可见   false:可见
     *
     * @param gone if true gone the load more view
     */
    public void loadMoreEnd(boolean gone) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        ////当前加载状态  切换为未在加载中
        mLoading = false;
        //不能再加载下一页了  因为已经没有更多数据了
        mNextLoadEnable = false;
        //设置加载更多布局是否可见
        mLoadMoreView.setLoadMoreEndGone(gone);
        if (gone) {
            //如果布局不可见,则更新
            notifyItemRemoved(getLoadMoreViewPosition());
        } else {
            //如果布局可见,则先更新布局(切换为STATUS_END状态那种布局)
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
            //并更新adapter
            notifyItemChanged(getLoadMoreViewPosition());
        }
    }

    /**
     * 设置监听RecyclerView上拉加载更多  并设置监听器
     *
     * @param requestLoadMoreListener
     * @param recyclerView
     */
    public void setOnLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener,
                                      RecyclerView recyclerView) {
        openLoadMore(requestLoadMoreListener);
        if (getRecyclerView() == null) {
            setRecyclerView(recyclerView);
        }
    }

    /**
     * 开启上拉加载更多
     *
     * @param requestLoadMoreListener
     */
    private void openLoadMore(RequestLoadMoreListener requestLoadMoreListener) {
        this.mRequestLoadMoreListener = requestLoadMoreListener;
        mNextLoadEnable = true;
        mLoadMoreEnable = true;
        mLoading = false;
    }

    /*-------------------emptyView-----------------------*/
    public void setEmptyView(@LayoutRes int layoutResId, @NonNull ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup,
                false);
        setEmptyView(view);
    }

    /**
     * bind recyclerView {@link #bindToRecyclerView(RecyclerView)} before use!
     *
     * @see #bindToRecyclerView(RecyclerView)
     */
    public void setEmptyView(@LayoutRes int layoutResId) {
        checkNotNull();
        setEmptyView(layoutResId, getRecyclerView());
    }

    public void setEmptyView(@NonNull View emptyView) {
        boolean insert = false;
        if (mEmptyLayout == null) {
            mEmptyLayout = new FrameLayout(emptyView.getContext());

            // 默认空布局是match_parent
            final RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams
                    (RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.MATCH_PARENT);
            final ViewGroup.LayoutParams lp = emptyView.getLayoutParams();
            if (lp != null) {
                layoutParams.width = lp.width;
                layoutParams.height = lp.height;
            }
            mEmptyLayout.setLayoutParams(layoutParams);
            insert = true;
        }

        //将需要显示的View添加到mEmptyLayout(FrameLayout)中
        mEmptyLayout.removeAllViews();
        mEmptyLayout.addView(emptyView);
        mIsUseEmpty = true;
        if (insert) { // 如果是第一次初始化空布局,那么需要通知adapter刷新
            if (getEmptyViewCount() == 1) {
                int position = 0;

                //判断有无header,如果有的话,那么header无需刷新 只需要刷新空布局即可
                if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                    position++;
                }
                notifyItemInserted(position);
            }
        }
    }

    /**
     * 设置当数据为空时是否显示headerView
     * 调用此方法前需要调用{@link RecyclerView#setAdapter(RecyclerView.Adapter)}
     *
     * @param isHeadAndEmpty false:当数据为空时不显示headerView,只显示EmptyView  true:
     *                       当数据为空时显示emptyView和headerView
     */
    public void setHeaderAndEmpty(boolean isHeadAndEmpty) {
        setHeaderFooterEmpty(isHeadAndEmpty, false);
    }

    /**
     * 设置数据为空时是否显示headerView和footerView
     * 调用此方法前需要调用 {@link RecyclerView#setAdapter(RecyclerView.Adapter)}
     *
     * @param isHeadAndEmpty true:显示headerView false:不显示headerView
     * @param isFootAndEmpty true:显示footerVIew false:不显示footerVIew
     */
    public void setHeaderFooterEmpty(boolean isHeadAndEmpty, boolean isFootAndEmpty) {
        mHeadAndEmptyEnable = isHeadAndEmpty;
        mFootAndEmptyEnable = isFootAndEmpty;
    }

    /**
     * Set whether to use empty view
     * 设置是否需要使用EmptyView
     *
     * @param isUseEmpty true:使用   false:不使用
     */
    public void isUseEmpty(boolean isUseEmpty) {
        mIsUseEmpty = isUseEmpty;
    }

    /**
     * When the current adapter is empty, the BaseQuickAdapter can display a special view
     * called the empty view. The empty view is used to provide feedback to the user
     * that no data is available in this AdapterView.
     *
     * @return The view to show if the adapter is empty.
     */
    public View getEmptyView() {
        return mEmptyLayout;
    }

    /**
     * 返回HeaderView在RecyclerView中的位置
     *
     * @return 0 or -1
     */
    private int getHeaderViewPosition() {
        //Return to header view notify position
        if (getEmptyViewCount() == 1) {
            //有空布局 并且 头布局可见
            if (mHeadAndEmptyEnable) {
                return 0;
            }
        } else {
            //没有空布局   返回0
            return 0;
        }
        return -1;
    }

    private int getFooterViewPosition() {
        //Return to footer view notify position
        if (getEmptyViewCount() == 1) {
            int position = 1;
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                //空布局可见 并且 头布局可见
                position++;
            }
            if (mFootAndEmptyEnable) {
                //尾布局可见
                return position;
            }
        } else {
            //头布局有无:0 or 1              正常项的大小
            return getHeaderLayoutCount() + mData.size();
        }
        return -1;
    }

    /**
     * if show empty view will be return 1 or not will be return 0
     *
     * @return
     */
    public int getEmptyViewCount() {
        if (mEmptyLayout == null || mEmptyLayout.getChildCount() == 0) {
            return 0;
        }
        if (!mIsUseEmpty) {
            return 0;
        }
        if (mData.size() != 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 设置item点击事件
     *
     * @param onItemClickListener OnItemClickListener
     */
    public void setOnItemClickListener(@NonNull OnItemClickListener
                                               onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置item长按事件
     *
     * @param onItemLongClickListener OnItemLongClickListener
     */
    public void setOnItemLongClickListener(@NonNull OnItemLongClickListener
                                                   onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    /**
     * item点击监听器
     */
    public interface OnItemClickListener {
        /**
         * item点击事件回调
         *
         * @param view     触发事件View
         * @param position 触发事件的view所在RecyclerView中的位置
         */
        void onItemClick(BaseQuickAdapter adapter, View view, int position);
    }

    /**
     * item长按监听器
     */
    public interface OnItemLongClickListener {
        /**
         * item长按事件回调
         *
         * @param view     触发事件View
         * @param position 触发事件的view所在RecyclerView中的位置
         * @return 是否消费
         */
        boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position);
    }

    /**
     * 加载更多监听器
     */
    public interface RequestLoadMoreListener {
        /**
         * 当需要加载更多时会被调用
         */
        void onLoadMoreRequested();
    }

}
