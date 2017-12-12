package com.xfhy.androidbasiclibs.adapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * author xfhy
 * create at 2017年10月31日22:06:25
 * description：从下往上
 */
public class SlideInBottomAnimation implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        //这里设置的是view的纵坐标,从view.getMeasuredHeight()-0,相当于从下往上移动自身的高度
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight(), 0)
        };
    }
}
