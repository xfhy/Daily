package com.xfhy.androidbasiclibs.widget;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;

import java.io.Serializable;

/**
 * Model builder class to show custom state
 * 自定义的状态,通过该配置model可以控制布局的显示
 */
public class CustomStateOptions implements Serializable {

    @DrawableRes
    private int imageRes;
    private boolean isLoading;
    private String message;
    private String buttonText;
    private View.OnClickListener buttonClickListener;

    /**
     * 设置图片资源
     */
    public CustomStateOptions image(@DrawableRes int val) {
        imageRes = val;
        return this;
    }

    public CustomStateOptions loading() {
        isLoading = true;
        return this;
    }

    /**
     * 设置需要显示的文字信息
     */
    public CustomStateOptions message(String val) {
        message = val;
        return this;
    }

    /**
     * 设置按钮文字
     */
    public CustomStateOptions buttonText(String val) {
        buttonText = val;
        return this;
    }

    /**
     * 设置按钮点击点听器
     */
    public CustomStateOptions buttonClickListener(@NonNull View.OnClickListener val) {
        buttonClickListener = val;
        return this;
    }

    public int getImageRes() {
        return imageRes;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public String getMessage() {
        return message;
    }

    public String getButtonText() {
        return buttonText;
    }

    public View.OnClickListener getClickListener() {
        return buttonClickListener;
    }

}
