package com.xfhy.androidbasiclibs.adapter.animation;

import android.animation.Animator;
import android.view.View;

/**
 * author xfhy
 * create at 2017年10月31日22:06:25
 * description：所有动画的父类
 */
public interface BaseAnimation {
    /**
     * 返回一个Animator数组,方便扩展,可以在view上加多个动画
     * @param view
     * @return
     */
    Animator[] getAnimators(View view);
}
