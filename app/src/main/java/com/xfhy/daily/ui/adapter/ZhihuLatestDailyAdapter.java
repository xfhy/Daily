package com.xfhy.daily.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.xfhy.androidbasiclibs.common.util.GlideUtils;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseQuickAdapter;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseViewHolder;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.LatestDailyListBean;

import java.util.List;

/**
 * @author feiyang
 * @time create at 2017/11/1 16:40
 * @description 知乎最新日报列表adapter
 */
public class ZhihuLatestDailyAdapter extends BaseQuickAdapter<LatestDailyListBean.StoriesBean,
        BaseViewHolder> {
    private Context context;

    public ZhihuLatestDailyAdapter(@NonNull Context context, @Nullable List<LatestDailyListBean
            .StoriesBean> data) {
        super(R.layout.item_zhihu_latest_daily, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, LatestDailyListBean.StoriesBean item) {
        if (item == null) {
            return;
        }
        holder.setText(R.id.tv_news_title_latest, item.getTitle());
        List<String> imageUrl = item.getImages();
        if (imageUrl != null && imageUrl.size() > 0) {
            GlideUtils.loadConsumImage(context, imageUrl.get(0), (ImageView) holder.getView(R.id
                    .iv_news_image_latest));
        }

    }
}
