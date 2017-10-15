package com.xfhy.androidbasiclibs.uihelper.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xfhy.androidbasiclibs.R;

/**
 * author feiyang
 * create at 2017/10/12 15:23
 * description：空布局
 */
public class EmptyView extends LinearLayout implements View.OnClickListener {

    /**
     * 监听器
     */
    private OnRetryListener listener;

    public EmptyView(Context context) {
        super(context);
        initView();
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_view, this);
        Button btnRetry = findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_retry) {
            if (listener != null) {
                listener.onClick();
            }
        }
    }

    /**
     * 设置重试按钮监听器
     *
     * @param listener 监听器OnRetryListener
     */
    public void setOnRetryListener(@NonNull OnRetryListener listener) {
        this.listener = listener;
    }

    /**
     * 重试回调接口
     */
    public interface OnRetryListener {
        /**
         * 当重试按钮被点击时会回调此方法
         */
        void onClick();
    }

}
