package com.xfhy.androidbasiclibs.adapter;

import android.view.ViewGroup;

import com.xfhy.androidbasiclibs.adapter.entity.SectionEntity;

import java.util.List;

/**
 * author feiyang
 * create at 2017/10/24 13:40
 * description：分组布局时需要使用该adapter
 */
public abstract class BaseSectionQuickAdapter<T extends SectionEntity, K extends BaseViewHolder>
        extends BaseQuickAdapter<T, K> {
    /**
     * 分组header布局id
     */
    protected int mSectionHeadResId;
    /**
     * 分组header类型
     */
    protected static final int SECTION_HEADER_VIEW = 0x00000444;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public BaseSectionQuickAdapter(int layoutResId, int sectionHeadResId, List<T> data) {
        super(layoutResId, data);
        this.mSectionHeadResId = sectionHeadResId;
    }

    @Override
    protected int getDefItemViewType(int position) {
        //item的类型 根据实体类里的一个属性  分组RecyclerView里,要么是分组header,要么是普通item
        return mData.get(position).isHeader ? SECTION_HEADER_VIEW : 0;
    }

    @Override
    protected K onCreateDefViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder  如果是分组header,那么view是mSectionHeadResId加载出来的
        if (viewType == SECTION_HEADER_VIEW)
            return createBaseViewHolder(getItemView(mSectionHeadResId, parent));
        //分组内的item,则用默认的方法创建ViewHolder
        return super.onCreateDefViewHolder(parent, viewType);
    }

    @Override
    protected boolean isFixedViewType(int type) {
        //分组header也是特殊布局,也需要进行跨格子(在GridLayoutManager中,比如SpanCount为2,那么分组header的跨度就是2)
        return super.isFixedViewType(type) || type == SECTION_HEADER_VIEW;
    }

    @Override
    public void onBindViewHolder(K holder, int position) {
        switch (holder.getItemViewType()) {
            case SECTION_HEADER_VIEW:
                //如果是分组header,那么需要设置为满Span  即占满
                setFullSpan(holder);
                //绑定数据  这是分组header
                convertHead(holder, getItem(position - getHeaderLayoutCount()));
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    /**
     * 分组header绑定数据
     *
     * @param helper ViewHolder
     * @param item   实体类
     */
    protected abstract void convertHead(K helper, T item);
}
