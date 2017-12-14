package com.xfhy.daily.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xfhy.androidbasiclibs.util.DensityUtil;
import com.xfhy.androidbasiclibs.util.GlideUtils;
import com.xfhy.androidbasiclibs.adapter.BaseQuickAdapter;
import com.xfhy.androidbasiclibs.adapter.BaseViewHolder;
import com.xfhy.daily.R;
import com.xfhy.daily.model.bean.ColumnDailyDetailsBean;

import java.util.List;

/**
 * @author xfhy
 *         create at 2017/11/26 14:52
 *         description：知乎专栏详情列表adapter
 */
public class ZHSectionDetailsAdapter extends BaseQuickAdapter<ColumnDailyDetailsBean.StoriesBean,
        BaseViewHolder> {
    private Context mContext;

    public ZHSectionDetailsAdapter(int layoutResId, @Nullable List<ColumnDailyDetailsBean
            .StoriesBean> data, Context mContext) {
        super(layoutResId, data);
        this.mContext = mContext;
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
    protected void convert(BaseViewHolder holder, ColumnDailyDetailsBean.StoriesBean item) {
        List<String> itemImages = item.getImages();
        if (itemImages != null && itemImages.size() > 0) {
            GlideUtils.loadCustomImage(mContext, itemImages.get(0), (ImageView) holder.getView(R
                    .id.iv_news_image_latest));
        } else {
            Glide.with(mContext).load(R.mipmap.ic_launcher).into((ImageView) holder.getView(R
                    .id.iv_news_image_latest));
        }
        holder.setText(R.id.tv_news_title_latest, item.getTitle());
    }
}
