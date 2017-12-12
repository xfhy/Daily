package com.xfhy.androidbasiclibs.adapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * author xfhy
 * create at 2017年10月31日22:06:25
 * description：从左往右
 */
public class SlideInLeftAnimation implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        //view 的translationX 从 -顶层root view的宽度~0
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0)
        };
    }
}
