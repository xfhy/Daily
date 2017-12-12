package com.xfhy.daily.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.xfhy.androidbasiclibs.util.GlideUtils;
import com.xfhy.androidbasiclibs.adapter.BaseSectionQuickAdapter;
import com.xfhy.androidbasiclibs.adapter.BaseViewHolder;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.LatestDailyListBean;

import java.util.List;

/**
 * @author feiyang
 * @time create at 2017/11/1 16:40
 * @description 知乎最新日报列表adapter
 */
public class ZHLatestDailyAdapter extends BaseSectionQuickAdapter<LatestDailyListBean
        .StoriesBean,
        BaseViewHolder> {
    private Context context;

    private HeaderChangeListener listener;

    public ZHLatestDailyAdapter(@NonNull Context context, @Nullable List<LatestDailyListBean
            .StoriesBean> data) {
        super(R.layout.item_zh_common_daily_list, R.layout.header_zh_latest_daily, data);
        this.context = context;
    }

    @Override
    protected void convertHead(BaseViewHolder helper, LatestDailyListBean.StoriesBean item) {
        helper.setText(R.id.tv_latest_header_title, item.header);
        if (listener != null) {
            listener.onHeaderChanged(item.header);
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, LatestDailyListBean.StoriesBean item) {
        if (item == null) {
            return;
        }
        holder.setText(R.id.tv_news_title_latest, item.getTitle());
        List<String> imageUrl = item.getImages();
        if (imageUrl != null && imageUrl.size() > 0) {
            GlideUtils.loadCustomImage(context, imageUrl.get(0), (ImageView) holder.getView(R.id
                    .iv_news_image_latest));
        }

    }

    /**
     * 监听header变化
     * @param listener HeaderChangeListener
     */
    public void setOnHeaderChangeListener(@NonNull HeaderChangeListener listener) {
        this.listener = listener;
    }

    public interface HeaderChangeListener {
        /**
         * 标题改变
         *
         * @param title 新标题
         */
        void onHeaderChanged(String title);
    }

}
