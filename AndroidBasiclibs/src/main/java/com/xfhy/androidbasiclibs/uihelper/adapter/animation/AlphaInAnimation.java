package com.xfhy.androidbasiclibs.uihelper.adapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * author xfhy
 * create at 2017年10月31日22:06:25
 * description：渐显
 */
public class AlphaInAnimation implements BaseAnimation {
    /**
     * 默认透明度从0开始
     */
    private static final float DEFAULT_ALPHA_FROM = 0f;
    private final float mFrom;

    public AlphaInAnimation() {
        this(DEFAULT_ALPHA_FROM);
    }

    public AlphaInAnimation(float from) {
        mFrom = from;
    }

    @Override
    public Animator[] getAnimators(View view) {
        //返回一个Animator数组    将动画附加到view上,透明度从mFrom-1f
        return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f)};
    }
}
