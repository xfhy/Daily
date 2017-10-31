package com.xfhy.androidbasiclibs.uihelper.adapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * author xfhy
 * create at 2017年10月31日22:06:25
 * description：从上往下
 */
public class SlideInTopAnimation implements BaseAnimation {

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationY", -view.getMeasuredHeight(), 0)};
    }
}
