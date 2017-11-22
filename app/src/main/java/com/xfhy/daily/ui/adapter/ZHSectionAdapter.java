package com.xfhy.daily.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.xfhy.androidbasiclibs.common.util.GlideUtils;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseQuickAdapter;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseViewHolder;
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
    protected void convert(BaseViewHolder holder, ColumnDailyBean.DataBean item) {
        GlideUtils.loadConsumImage(mContext, item.getThumbnail(), (ImageView) holder.getView(R.id
                .iv_sections_thumbnail));
        holder.setText(R.id.tv_sections_name, item.getName());
        holder.setText(R.id.tv_sections_description, item.getDescription());
    }
}
