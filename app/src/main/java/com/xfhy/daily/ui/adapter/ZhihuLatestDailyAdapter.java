package com.xfhy.daily.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.xfhy.androidbasiclibs.common.util.GlideUtils;
import com.xfhy.androidbasiclibs.common.util.LogUtils;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseMultiItemQuickAdapter;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseQuickAdapter;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseSectionQuickAdapter;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseViewHolder;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.LatestDailyListBean;

import java.util.List;

/**
 * @author feiyang
 * @time create at 2017/11/1 16:40
 * @description 知乎最新日报列表adapter
 */
public class ZhihuLatestDailyAdapter extends BaseSectionQuickAdapter<LatestDailyListBean
        .StoriesBean,
        BaseViewHolder> {
    private Context context;

    private HeaderChangeListener listener;

    public ZhihuLatestDailyAdapter(@NonNull Context context, @Nullable List<LatestDailyListBean
            .StoriesBean> data) {
        super(R.layout.item_zhihu_latest_daily, R.layout.header_zhihu_latest_daily, data);
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
            GlideUtils.loadConsumImage(context, imageUrl.get(0), (ImageView) holder.getView(R.id
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
