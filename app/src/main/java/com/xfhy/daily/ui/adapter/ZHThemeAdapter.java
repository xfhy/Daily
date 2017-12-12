package com.xfhy.daily.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xfhy.androidbasiclibs.util.DevicesUtils;
import com.xfhy.androidbasiclibs.util.GlideUtils;
import com.xfhy.androidbasiclibs.adapter.BaseQuickAdapter;
import com.xfhy.androidbasiclibs.adapter.BaseViewHolder;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.TopicDailyListBean;

import java.util.List;

/**
 * @author xfhy
 *         create at 2017/11/19 9:51
 *         description：知乎主题列表adapter
 */
public class ZHThemeAdapter extends BaseQuickAdapter<TopicDailyListBean.OthersBean,
        BaseViewHolder> {

    private Context context;
    /**
     * 屏幕的宽度/2   把item的宽度设置为屏幕宽度的一半
     */
    private int mWidthPixels;

    public ZHThemeAdapter(int layoutResId, @Nullable List<TopicDailyListBean.OthersBean> data,
                          Context context) {
        super(layoutResId, data);
        this.context = context;
        DisplayMetrics devicesSize = DevicesUtils.getDevicesSize(context);
        mWidthPixels = devicesSize.widthPixels / 2;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = super.onCreateViewHolder(parent, viewType);
        //设置item的宽高相等 设置为屏幕宽度的一半
        View view = baseViewHolder.getView(R.id.fl_theme_root_view);
        if (view instanceof FrameLayout) {
            FrameLayout rootView = (FrameLayout) view;
            ViewGroup.LayoutParams rootViewLayoutParams = rootView.getLayoutParams();
            rootViewLayoutParams.height = rootViewLayoutParams.width = mWidthPixels;
            rootView.setLayoutParams(rootViewLayoutParams);
        }

        return baseViewHolder;
    }

    @Override
    protected void convert(BaseViewHolder holder, TopicDailyListBean.OthersBean item) {
        GlideUtils.loadCustomImage(context, item.getThumbnail(), (ImageView) holder.getView(R.id
                .iv_theme_item));
        holder.setText(R.id.tv_theme_item, item.getName());
    }
}
