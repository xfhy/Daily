package com.xfhy.androidbasiclibs.uihelper.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorLong;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * author xingyun
 * create at 2017/9/26 16:54
 * description：适用所有RecyclerView的ViewHolder
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    //子布局中的控件
    private SparseArray<View> mItemViews;
    //子布局
    private View mView;

    //初始化ViewHolder
    public BaseViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mItemViews = new SparseArray<>();
    }

    /**
     * 获取子控件
     * <p>
     * 子控件的id
     *
     * @param viewId 返回子控件
     * @return
     */
    public View getView(@IdRes int viewId) {
        View view = mItemViews.get(viewId);
        if (view == null) {
            view = mView.findViewById(viewId);
            mItemViews.put(viewId, view);
        }
        return view;
    }

    /**
     * 通过strings.xml文件给TextView设置文本
     * <p>
     * 子控件的id
     *
     * @param viewId 子控件在strings.xml中的文本
     * @param resId  返回子控件
     * @return BaseViewHolder
     */
    public BaseViewHolder setText(@IdRes int viewId, @StringRes int resId) {
        TextView textView = (TextView) getView(viewId);
        textView.setText(resId);
        return this;
    }

    /**
     * 通过String给TextView设置文本
     * <p>
     * 子控件的id
     *
     * @param viewId 子控件中的文本
     * @param text   返回子控件
     * @return BaseViewHolder
     */
    public BaseViewHolder setText(@IdRes int viewId, String text) {
        TextView textView = (TextView) getView(viewId);
        if (text != null) {
            textView.setText(text);
        } else {
            textView.setText("");
        }
        return this;
    }

    /**
     * 通过SpannableStringBuilder给TextView设置文本
     *
     * @param viewId View的id
     * @param text   文本
     * @return BaseViewHolder
     */
    public BaseViewHolder setText(@IdRes int viewId, SpannableStringBuilder text) {
        TextView textView = (TextView) getView(viewId);
        if (text != null) {
            textView.setText(text);
        } else {
            textView.setText("");
        }
        return this;
    }

    /**
     * 通过drawable文件夹中的资源设置图片
     *
     * @param viewId view的id
     * @param resId  文本
     * @return BaseViewHolder
     */
    public BaseViewHolder setImageResource(@IdRes int viewId, @DrawableRes int resId) {
        ImageView imageView = (ImageView) getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    /**
     * 通过Bitmap设置图片
     *
     * @param viewId view Id
     * @param bitmap Bitmap
     * @return BaseViewHolder
     */
    public BaseViewHolder setImageBitmap(@IdRes int viewId, @NonNull Bitmap bitmap) {
        ImageView imageView = (ImageView) getView(viewId);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        return this;
    }

    /**
     * 通过Drawable设置图片
     *
     * @param viewId View的id
     * @param drawable Drawable
     * @return BaseViewHolder
     */
    public BaseViewHolder setImageDrawable(@IdRes int viewId, @NonNull Drawable drawable) {
        ImageView imageView = (ImageView) getView(viewId);
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        }
        return this;
    }

    /**
     * 通过一串数字设置背景色
     *
     * @param viewId View的id
     * @param color 颜色值 16进制
     * @return BaseViewHolder
     */
    public BaseViewHolder setBackgroundColor(@IdRes int viewId, @ColorLong int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * 通过drawable文件夹设置背景图
     *
     * @param viewId View的id
     * @param backgroundRes Resource
     * @return BaseViewHolder
     */
    public BaseViewHolder setBackgroundResource(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }


    /**
     * 通过Drawable设置背景图
     *
     * @param viewId View的id
     * @param drawable Drawable
     * @return BaseViewHolder
     */
    public BaseViewHolder setBackgroundDrawable(@IdRes int viewId, Drawable drawable) {
        View view = getView(viewId);
        if (drawable != null) {
            view.setBackground(drawable);
        }
        return this;
    }

    /**
     * 通过一串数字设置文字颜色
     *
     * @param viewId View的id
     * @param textColor 颜色值 16进制
     * @return BaseViewHolder
     */
    public BaseViewHolder setTextColor(@IdRes int viewId, @ColorLong int textColor) {
        TextView textView = (TextView) getView(viewId);
        textView.setTextColor(textColor);
        return this;
    }

    /**
     * 通过float设置透明度
     *
     * @param viewId View的id
     * @param value 透明度  范围:[0.0,1.0]
     * @return BaseViewHolder
     */
    public BaseViewHolder setAlpha(@IdRes int viewId, @FloatRange(from = 0.0, to = 1.0) float
            value) {
        getView(viewId).setAlpha(value);
        return this;
    }

    /**
     * 通过boolean类型设置是否显示
     *
     * @param viewId View的id
     * @param visible 是否可见 true:可见;  false:不可见,Gone
     * @return BaseViewHolder
     */
    public BaseViewHolder setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? view.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 缓存子控件上界面的数据
     *
     * @param viewId View的id
     * @param tag 需要缓存的数据
     * @return BaseViewHolder
     */
    public BaseViewHolder setTag(@IdRes int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * 设置某一位置子控件的数据
     *
     * @param viewId View的id
     * @param key 数据标识
     * @param tag 数据
     * @return BaseViewHolder
     */
    public BaseViewHolder setTag(@IdRes int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * 设置子控件是否选中
     *
     * @param viewId  View的id
     * @param checked true:选中   false:未选中
     * @return BaseViewHolder
     */
    public BaseViewHolder setChecked(@IdRes int viewId, boolean checked) {
        Checkable checkable = (Checkable) getView(viewId);
        checkable.setChecked(checked);
        return this;
    }

    /**
     * 设置子控件的点击事件
     *
     * @param viewId View的id
     * @param listener OnClickListener监听器
     * @return BaseViewHolder
     */
    public BaseViewHolder setOnClickListener(@IdRes int viewId, @NonNull View.OnClickListener
            listener) {
        View view = getView(viewId);
        if (listener != null) {
            view.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 设置子控件的触摸事件
     *
     * @param viewId View的id
     * @param listener OnTouchListener
     * @return BaseViewHolder
     */
    public BaseViewHolder setOnTouchListener(@IdRes int viewId, @NonNull View.OnTouchListener
            listener) {
        View view = getView(viewId);
        if (listener != null) {
            view.setOnTouchListener(listener);
        }
        return this;
    }

    /**
     * 设置子控件的长按事件
     *
     * @param viewId View的id
     * @param listener OnLongClickListener
     * @return BaseViewHolder
     */
    public BaseViewHolder setOnLongClickListener(@IdRes int viewId, @NonNull View
            .OnLongClickListener
            listener) {
        View view = getView(viewId);
        if (listener != null) {
            view.setOnLongClickListener(listener);
        }
        return this;
    }
}
