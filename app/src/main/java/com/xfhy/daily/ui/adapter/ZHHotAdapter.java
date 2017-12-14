package com.xfhy.daily.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.xfhy.androidbasiclibs.util.DensityUtil;
import com.xfhy.androidbasiclibs.util.GlideUtils;
import com.xfhy.androidbasiclibs.adapter.BaseQuickAdapter;
import com.xfhy.androidbasiclibs.adapter.BaseViewHolder;
import com.xfhy.daily.R;
import com.xfhy.daily.model.bean.HotDailyBean;

import java.util.List;

/**
 * @author xfhy
 *         create at 2017/11/26 16:26
 *         description：知乎热门文章adapter
 */
public class ZHHotAdapter extends BaseQuickAdapter<HotDailyBean.RecentBean, BaseViewHolder> {

    public ZHHotAdapter(int layoutResId, @Nullable List<HotDailyBean.RecentBean> data) {
        super(layoutResId, data);
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
    protected void convert(BaseViewHolder holder, HotDailyBean.RecentBean item) {
        GlideUtils.loadCustomImage(holder.itemView, item.getThumbnail(), (ImageView) holder
                .getView(R.id.iv_news_image_latest));
        holder.setText(R.id.tv_news_title_latest, item.getTitle());
    }
}
