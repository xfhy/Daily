package com.xfhy.androidbasiclibs.common.util;


import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfhy.androidbasiclibs.R;

/**
 * author feiyang
 * create at 2017/10/12 16:57
 * description：SnakeBar的简单封装,方便使用
 * <p>
 * <p>
 * <p>
 * 源码中:
 * Snackbar snackbar = new Snackbar(findSuitableParent(view));
 * Snackbar的第一个参数:view
 * <p>
 * findSuitableParent(view)方法的主要作用:
 * 1.当传入的View不为空时，如果我们在布局中发现了CoordinatorLayout布局，那么返回的View就是CoordinatorLayout；
 * 2.如果没有CoordinatorLayout布局，我们就先找到一个id为android.R.id.content的FrameLayout（这个布局是最底层的根布局），
 * 将View设置为该FrameLayout；
 * 3.其他情况下就使用View的Parent布局一直到这个View不为空。
 * <p>
 * 这里的颜色配置不能再R.color中配置  Snackbar里面是只能是@ColorInt来设置背景颜色
 */
public class SnackbarUtil {

    /**
     * 信息类型
     */
    public static final int INFO = 1;
    /**
     * 确认信息类型
     */
    public static final int CONFIRM = 2;
    /**
     * 警告类型
     */
    public static final int WARNING = 3;
    /**
     * 错误类型
     */
    public static final int ALERT = 4;

    /**
     * 信息类型的背景颜色
     */
    public final static int BLUE = 0xff2195f3;
    /**
     * 确认信息类型背景颜色
     */
    public final static int GREEN = 0xff4caf50;
    /**
     * 警告类型背景颜色
     */
    public final static int ORANGE = 0xffffc107;
    /**
     * 错误类型背景颜色
     */
    public final static int RED = 0xfff44336;

    /**
     * 消息类型   替代Java中的枚举类型
     */
    @IntDef({INFO, CONFIRM, WARNING, ALERT})
    private @interface MessageType {
    }

    /**
     * 显示Snackbar,时长:短时间(1570ms)，可自定义颜色
     *
     * @param view            The view to find a parent from.   view不能为空,
     *                        否则会抛出IllegalArgumentException("No suitable parent found from the
     *                        given view.Please provide a valid view.");
     * @param message         需要显示的消息
     * @param messageColor    消息文本颜色
     * @param backgroundColor 背景颜色
     */
    public static void showBarShortTime(@NonNull View view, @NonNull String message, @ColorInt int
            messageColor, @ColorInt int backgroundColor) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        setSnackbarColor(snackbar, messageColor, backgroundColor);

        snackbar.show();
    }

    /**
     * 显示Snackbar,时长:长时间(2750ms)，可自定义颜色
     *
     * @param view            The view to find a parent from.
     * @param message         需要显示的消息
     * @param messageColor    消息文本颜色
     * @param backgroundColor 背景颜色
     */
    public static void showBarLongTime(@NonNull View view, @NonNull String message, @ColorInt int
            messageColor, @ColorInt int backgroundColor) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        setSnackbarColor(snackbar, messageColor, backgroundColor);
        snackbar.show();
    }

    /**
     * 自定义时常显示Snackbar，自定义颜色
     *
     * @param view            The view to find a parent from.
     * @param message         需要显示的消息
     * @param duration        显示时长   单位:ms
     * @param messageColor    消息文本颜色
     * @param backgroundColor 背景颜色
     */
    public static void showCustomCATSnackbar(@NonNull View view, @NonNull String message,
                                             @IntRange(from = 1) int duration,
                                             @ColorInt int messageColor,
                                             @ColorInt int backgroundColor) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setDuration(duration);
        setSnackbarColor(snackbar, messageColor, backgroundColor);
        snackbar.show();
    }

    /**
     * 显示Snackbar,时长:短时间(1570ms)，可选预设类型
     * {@link android.support.design.widget.SnackbarManager.SHORT_DURATION_MS}
     *
     * @param view    The view to find a parent from.
     * @param message 需要显示的消息
     * @param type    需要显示的消息类型 SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    public static void showBarShortTime(@NonNull View view, @NonNull String message, @MessageType
            int type) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        switchType(snackbar, type);
        snackbar.show();
    }

    /**
     * 显示Snackbar,时长:长时间(2750ms)，可选预设类型
     * {@link android.support.design.widget.SnackbarManager.LONG_DURATION_MS}
     *
     * @param view    The view to find a parent from.
     * @param message 需要显示的消息
     * @param type    需要显示的消息类型 SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    public static void showBarLongTime(@NonNull View view, @NonNull String message, @MessageType
            int type) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        switchType(snackbar, type);
        snackbar.show();
    }

    /**
     * 自定义时长 显示Snackbar，可选预设类型
     *
     * @param view     The view to find a parent from.
     * @param message  需要显示的消息
     * @param duration 显示时长   单位:ms
     * @param type     需要显示的消息类型 SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    public static void showCustomTimeSnackbar(@NonNull View view, @NonNull String message,
                                              @IntRange(from = 1) int duration, @MessageType int
                                                      type) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setDuration(duration);
        switchType(snackbar, type);
        snackbar.show();
    }

    /**
     * 设置Snackbar背景颜色
     *
     * @param snackbar        Snackbar
     * @param backgroundColor 背景颜色
     */
    private static void setSnackbarBgColor(Snackbar snackbar, @ColorInt int backgroundColor) {
        View view = snackbar.getView();
        view.setBackgroundColor(backgroundColor);
    }

    /**
     * 设置Snackbar文字和背景颜色
     *
     * @param snackbar        Snackbar
     * @param messageColor    文字颜色
     * @param backgroundColor 背景颜色
     */
    private static void setSnackbarColor(Snackbar snackbar, @ColorInt int messageColor,
                                         @ColorInt int backgroundColor) {
        View view = snackbar.getView();  //获取Snackbar自己的布局
        //设置Snackbar自己的布局的背景颜色
        view.setBackgroundColor(backgroundColor);
        //设置Snackbar自己的布局中的TextView的颜色
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);
    }

    /**
     * 切换预设消息类型
     *
     * @param snackbar Snackbar
     * @param type     消息类型 SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    private static void switchType(Snackbar snackbar, @MessageType int type) {
        switch (type) {
            case INFO:
                setSnackbarBgColor(snackbar, BLUE);
                break;
            case CONFIRM:
                setSnackbarBgColor(snackbar, GREEN);
                break;
            case WARNING:
                setSnackbarBgColor(snackbar, ORANGE);
                break;
            case ALERT:
                setSnackbarColor(snackbar, Color.YELLOW, RED);
                break;
        }
    }

    /**
     * 向Snackbar中添加view
     *
     * @param snackbar Snackbar
     * @param layoutId 需要添加的布局的id
     * @param index    新加布局在Snackbar中的位置
     */
    public static void SnackbarAddView(Snackbar snackbar, @LayoutRes int layoutId, int index) {
        View snackbarView = snackbar.getView();
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarView;

        View addView = LayoutInflater.from(snackbarView.getContext()).inflate(layoutId, null);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        p.gravity = Gravity.CENTER_VERTICAL;
        snackbarLayout.addView(addView, index, p);
    }

}
