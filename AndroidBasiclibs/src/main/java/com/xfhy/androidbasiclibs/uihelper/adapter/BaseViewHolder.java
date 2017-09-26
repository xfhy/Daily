package com.xfhy.androidbasiclibs.uihelper.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * author xingyun
 * create at 2017/9/26 16:54
 * description：适用所有RecyclerView的ViewHolder
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    SparseArray<View> mItemViews;
    View mView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mItemViews = new SparseArray<>();
    }

    public View getView(int viewId) {
        View view = mItemViews.get(viewId);
        if (view == null) {
            view = mView.findViewById(viewId);
            mItemViews.put(viewId, view);
        }
        return view;
    }

    public BaseViewHolder setText(int viewId, int resId) {
        TextView textView = (TextView) getView(viewId);
        textView.setText(resId);
        return this;
    }

    public BaseViewHolder setText(int viewId, String text) {
        TextView textView = (TextView) getView(viewId);
        textView.setText(text);
        return this;
    }

    public BaseViewHolder setText(int viewId, SpannableStringBuilder text) {
        TextView textView = (TextView) getView(viewId);
        textView.setText(text);
        return this;
    }

    public BaseViewHolder setImageResource(int viewId, int resId) {
        ImageView imageView = (ImageView) getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    public BaseViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView = (ImageView) getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    public BaseViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView imageView = (ImageView) getView(viewId);
        imageView.setImageDrawable(drawable);
        return this;
    }

    public BaseViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public BaseViewHolder setBackgroundResource(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public BaseViewHolder setBackgroundDrawable(int viewId, Drawable drawable) {
        View view = getView(viewId);
        view.setBackground(drawable);
        return this;
    }

    public BaseViewHolder setTextColor(int viewId, int textColor) {
        TextView textView = (TextView) getView(viewId);
        textView.setTextColor(textColor);
        return this;
    }

    public BaseViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public BaseViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? view.VISIBLE : View.GONE);
        return this;
    }

    public BaseViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public BaseViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public BaseViewHolder setChecked(int viewId, boolean checked) {
        Checkable checkable = (Checkable) getView(viewId);
        checkable.setChecked(checked);
        return this;
    }

    /**
     * 关于事件监听
     */
    public BaseViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }
    public BaseViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }
}
