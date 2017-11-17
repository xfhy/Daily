package com.xfhy.daily.ui.adapter;

import android.content.Context;

import com.xfhy.androidbasiclibs.common.util.DateUtils;
import com.xfhy.androidbasiclibs.common.util.GlideUtils;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseSectionQuickAdapter;
import com.xfhy.androidbasiclibs.uihelper.adapter.BaseViewHolder;
import com.xfhy.androidbasiclibs.uihelper.widget.CircleImageView;
import com.xfhy.daily.R;
import com.xfhy.daily.network.entity.zhihu.DailyCommentBean;

import java.util.Date;
import java.util.List;

/**
 * author feiyang
 * time create at 2017/11/17 19:12
 * description 知乎评论页RecyclerView adapter
 */
public class ZHCommentAdapter extends BaseSectionQuickAdapter<DailyCommentBean.CommentsBean,
        BaseViewHolder> {

    private Context context;

    public ZHCommentAdapter(Context context,
                            List<DailyCommentBean.CommentsBean> data) {
        super(R.layout.item_zh_comment, R.layout.item_zh_comment_header, data);
        this.context = context;
    }

    public ZHCommentAdapter(int layoutResId, int sectionHeadResId, List<DailyCommentBean
            .CommentsBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, DailyCommentBean.CommentsBean item) {
        helper.setText(R.id.tv_comment_header, item.header);
    }

    @Override
    protected void convert(BaseViewHolder holder, DailyCommentBean.CommentsBean item) {
        if (item == null) {
            return;
        }
        //头像
        GlideUtils.loadConsumImage(context, item.getAvatar(), (CircleImageView) holder.getView(R.id
                .civ_comment_avatar));
        //昵称
        holder.setText(R.id.tv_author, item.getAuthor());
        //点赞
        holder.setText(R.id.tv_comment_like, String.valueOf(item.getLikes()));
        //内容
        holder.setText(R.id.tv_content, item.getContent());
        //时间
        holder.setText(R.id.tv_comment_time, DateUtils.getDateFormatText(new Date(item.getTime())
                , "MM-dd HH:mm"));
    }
}
