package com.xfhy.daily.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.xfhy.androidbasiclibs.util.DensityUtil;
import com.xfhy.androidbasiclibs.util.GlideUtils;
import com.xfhy.androidbasiclibs.adapter.BaseQuickAdapter;
import com.xfhy.androidbasiclibs.adapter.BaseViewHolder;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.ColumnDailyBean;

import java.util.List;

/**
 * @author feiyang
 *         time create at 2017/11/22 14:56
 *         description 知乎专栏列表adapter
 */
public class ZHSectionAdapter extends BaseQuickAdapter<ColumnDailyBean.DataBean, BaseViewHolder> {

    private Context mContext;

    public ZHSectionAdapter(int layoutResId, @Nullable List<ColumnDailyBean.DataBean> data,
                            Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        //第一项需要离顶部一段10dp的距离
        if (position == 0) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView
                    .getLayoutParams();
            layoutParams.setMargins(layoutParams.getMarginStart(),
                    DensityUtil.dip2px(mContext, 10f),
                    layoutParams.getMarginEnd(),
                    DensityUtil.dip2px(mContext, 10f));
            holder.itemView.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, ColumnDailyBean.DataBean item) {
        GlideUtils.loadCustomImage(mContext, item.getThumbnail(), (ImageView) holder.getView(R.id
                .iv_sections_thumbnail));
        holder.setText(R.id.tv_sections_name, item.getName());
        holder.setText(R.id.tv_sections_description, item.getDescription());
    }
}
