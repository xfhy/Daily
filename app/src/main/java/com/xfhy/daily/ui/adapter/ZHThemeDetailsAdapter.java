package com.xfhy.daily.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.xfhy.androidbasiclibs.common.util.GlideUtils;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseQuickAdapter;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseViewHolder;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.ThemeDailyDetailsBean;

import java.util.List;

/**
 * @author xfhy
 *         create at 2017/11/19 18:09
 *         description：知乎主题详情页adapter
 */
public class ZHThemeDetailsAdapter extends BaseQuickAdapter<ThemeDailyDetailsBean.StoriesBean,
        BaseViewHolder> {

    private Context mContext;

    public ZHThemeDetailsAdapter(int layoutResId, @Nullable List<ThemeDailyDetailsBean
            .StoriesBean> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, ThemeDailyDetailsBean.StoriesBean item) {

        //日报图片  可能没有
        List<String> images = item.getImages();
        if (images == null || images.size() == 0) {
            holder.setVisible(R.id.iv_daily_image, false);
        } else {
            holder.setVisible(R.id.iv_daily_image, true);
            GlideUtils.loadCustomImage(mContext, images.get(0), (ImageView) holder.getView(R.id
                    .iv_daily_image));
        }

        //日报标题
        holder.setText(R.id.tv_daily_title, item.getTitle());
    }
}
