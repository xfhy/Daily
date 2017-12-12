package com.xfhy.daily.ui.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.xfhy.androidbasiclibs.util.DateUtils;
import com.xfhy.androidbasiclibs.util.GlideUtils;
import com.xfhy.androidbasiclibs.util.LogUtils;
import com.xfhy.androidbasiclibs.adapter.BaseSectionQuickAdapter;
import com.xfhy.androidbasiclibs.adapter.BaseViewHolder;
import com.xfhy.androidbasiclibs.widget.CircleImageView;
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

    /**
     * 回复内容的最大行数(未展开时)
     */
    private final static int MAX_LINES = 2;

    private final static int NOT_EXPANDED = 0;
    private final static int EXPANDED = 1;


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
    protected void convert(final BaseViewHolder holder, DailyCommentBean.CommentsBean item) {
        if (item == null) {
            return;
        }
        //头像
        GlideUtils.loadCustomImage(context, item.getAvatar(), (CircleImageView) holder.getView(R.id
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

        final DailyCommentBean.CommentsBean.ReplyToBean replyTo = item.getReplyTo();
        if (replyTo != null) {

            //有些content是null,是被作者删除了的
            if (replyTo.getContent() == null) {
                return;
            }

            holder.setVisible(R.id.tv_comment_reply, true);

            //改变字体颜色   将作者突出显示
            //先构造SpannableString
            SpannableString spanString = new SpannableString("@" + item.getReplyTo().getAuthor() +
                    ":" + item.getReplyTo().getContent());
            //再构造一个改变字体颜色的Span
            ForegroundColorSpan authorSpan = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                authorSpan = new ForegroundColorSpan(context.getColor(R.color.snackbar_blue));
            } else {
                authorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color
                        .snackbar_blue));
            }
            //将这个Span应用于指定范围的字体
            spanString.setSpan(authorSpan, 0, item.getReplyTo().getAuthor().length()+2, Spannable
                    .SPAN_INCLUSIVE_INCLUSIVE);

            //回复内容
            final TextView replyView = (TextView) holder.getView(R.id.tv_comment_reply);
            replyView.setText(spanString);


            //必须这样写才能计算出TextView的行数,不然得出的结果是0
            replyView.post(new Runnable() {
                @Override
                public void run() {
                    final int replyLineCount = replyView.getLineCount();//获取当前回复内容所占的行数
                    if (replyLineCount > 0) {
                        LogUtils.e("replyLineCount:" + replyLineCount);
                        holder.setVisible(R.id.tv_comment_expand, true);
                        holder.setText(R.id.tv_comment_expand, "展开");
                    }

                    holder.setOnClickListener(R.id.tv_comment_expand, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                /*
                                *   有
                                *       1,少   不用管
                                *       2,多
                                *           a,已展开
                                *           b,未展开
                                * */
                            if (replyTo.getExpandState() == NOT_EXPANDED) {
                                replyView.setMaxLines(Integer.MAX_VALUE);
                                holder.setText(R.id.tv_comment_expand, "收缩");
                                replyTo.setExpandState(EXPANDED);
                            } else if (replyTo.getExpandState() == EXPANDED) {
                                replyView.setMaxLines(MAX_LINES);
                                holder.setText(R.id.tv_comment_expand, "展开");
                                replyTo.setExpandState(NOT_EXPANDED);
                            }
                        }
                    });
                }
            });

        }

    }
}
