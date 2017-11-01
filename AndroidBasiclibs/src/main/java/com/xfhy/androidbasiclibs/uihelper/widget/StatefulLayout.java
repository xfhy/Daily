package com.xfhy.androidbasiclibs.uihelper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xfhy.androidbasiclibs.R;

/**
 * Android layout to show most common state templates like loading, empty, error etc. To do that
 * all you need to is
 * wrap the target area(view) with StatefulLayout. For more information about usage look
 * <p>
 * setAnimationEnabled(boolean animationEnabled)
 * setInAnimation(@AnimRes int inAnimation)
 * setOutAnimation(@AnimRes int outAnimation)
 * showContent()
 * showLoading(String message)
 * showEmpty(String message)
 * showError(String message, OnClickListener clickListener)
 * showOffline(String message, OnClickListener clickListener)
 * showLocationOff(String message, OnClickListener clickListener)
 * showCustom(CustomStateOptions options)
 * <p>
 * 自定义属性
 * <attr name="stfAnimationEnabled" format="boolean" />
 * <attr name="stfInAnimation" format="reference" />
 * <attr name="stfOutAnimation" format="reference" />
 */
public class StatefulLayout
        extends LinearLayout {

    private static final String MSG_ONE_CHILD = "StatefulLayout must have one child!";
    private static final boolean DEFAULT_ANIM_ENABLED = true;
    private static final int DEFAULT_IN_ANIM = android.R.anim.fade_in;
    private static final int DEFAULT_OUT_ANIM = android.R.anim.fade_out;

    /**
     * 指示是否将动画置于状态更改中
     */
    private boolean animationEnabled;
    /**
     * 进入时动画
     */
    private Animation inAnimation;
    /**
     * 离开界面时动画
     */
    private Animation outAnimation;
    /**
     * 当动画持续时间短于状态请求时，同步转换动画
     *       改变
     */
    private int animCounter;

    /**
     * 包裹的内容View
     */
    private View content;
    /**
     * 多样式布局的根布局
     */
    private LinearLayout stContainer;
    private ProgressBar stProgress;
    private ImageView stImage;
    private TextView stMessage;
    private Button stButton;

    public StatefulLayout(Context context) {
        this(context, null);
    }

    public StatefulLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        //读取自定义属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable
                .stfStatefulLayout, 0, 0);
        animationEnabled = array.getBoolean(R.styleable.stfStatefulLayout_stfAnimationEnabled,
                DEFAULT_ANIM_ENABLED);
        inAnimation = loadAnim(array.getResourceId(R.styleable.stfStatefulLayout_stfInAnimation,
                DEFAULT_IN_ANIM));
        outAnimation = loadAnim(array.getResourceId(R.styleable.stfStatefulLayout_stfOutAnimation,
                DEFAULT_OUT_ANIM));
        array.recycle();
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public void setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
    }

    public Animation getInAnimation() {
        return inAnimation;
    }

    public void setInAnimation(Animation animation) {
        inAnimation = animation;
    }

    public void setInAnimation(@AnimRes int anim) {
        inAnimation = loadAnim(anim);
    }

    public Animation getOutAnimation() {
        return outAnimation;
    }

    public void setOutAnimation(Animation animation) {
        outAnimation = animation;
    }

    public void setOutAnimation(@AnimRes int anim) {
        outAnimation = loadAnim(anim);
    }

    //从xml加载view已经完成会回调此方法
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //将自己写的样式布局加载到当前View中,并fbc

        if (getChildCount() > 1) {
            throw new IllegalStateException(MSG_ONE_CHILD);
        }
        if (isInEditMode()) {  //判断当前视图是否可编辑
            return; // hide state views in designer
        }
        setOrientation(VERTICAL);
        content = getChildAt(0); // assume first child as content  Content是第一个
        LayoutInflater.from(getContext()).inflate(R.layout.stf_template, this, true);
        stContainer = (LinearLayout) findViewById(R.id.stContainer);
        stProgress = (ProgressBar) findViewById(R.id.stProgress);
        stImage = (ImageView) findViewById(R.id.stImage);
        stMessage = (TextView) findViewById(R.id.stMessage);
        stButton = (Button) findViewById(R.id.stButton);
    }

    // content //

    public void showContent() {
        // 判断是否启用了动画
        if (isAnimationEnabled()) {
            stContainer.clearAnimation();  //取消此view的所有动画
            content.clearAnimation();
            final int animCounterCopy = ++animCounter;
            if (stContainer.getVisibility() == VISIBLE) {
                outAnimation.setAnimationListener(new CustomAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (animCounter != animCounterCopy) {
                            return;
                        }
                        // 多样式布局隐藏,内容区可见
                        stContainer.setVisibility(GONE);
                        content.setVisibility(VISIBLE);
                        content.startAnimation(inAnimation);
                    }
                });
                stContainer.startAnimation(outAnimation);
            }
        } else {
            // 多样式布局隐藏,内容区可见
            stContainer.setVisibility(GONE);
            content.setVisibility(VISIBLE);
        }
    }

    // loading //

    public void showLoading() {
        showLoading(R.string.stfLoadingMessage);
    }

    public void showLoading(@StringRes int resId) {
        showLoading(getStringRes(resId));
    }

    public void showLoading(String message) {
        showCustom(new CustomStateOptions()
                .message(message)
                .loading());
    }

    // empty //

    public void showEmpty() {
        showEmpty(R.string.stfEmptyMessage);
    }

    public void showEmpty(@StringRes int resId) {
        showEmpty(getStringRes(resId));
    }

    public void showEmpty(String message) {
        showCustom(new CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_empty));
    }

    public void showEmpty(String message, String stfButtonText) {
        showCustom(new CustomStateOptions()
                .message(message)
                .buttonText(stfButtonText)
                .image(R.drawable.stf_ic_empty));
    }

    public void showEmpty(@StringRes int textResId, @StringRes int btnTextResId) {
        showCustom(new CustomStateOptions()
                .message(getStringRes(textResId))
                .buttonText(getStringRes(btnTextResId))
                .image(R.drawable.stf_ic_empty));
    }

    // error //

    public void showError(@NonNull OnClickListener clickListener) {
        showError(R.string.stfErrorMessage, clickListener);
    }

    public void showError(@StringRes int resId, @NonNull OnClickListener clickListener) {
        showError(getStringRes(resId), clickListener);
    }

    public void showError(String message, @NonNull OnClickListener clickListener) {
        showCustom(new CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_error)
                .buttonText(getStringRes(R.string.stfButtonRetry))
                .buttonClickListener(clickListener));
    }

    public void showError(String message, String
            stfButtonText, @NonNull OnClickListener clickListener) {
        showCustom(new CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_error)
                .buttonText(stfButtonText)
                .buttonClickListener(clickListener));
    }

    public void showError(@StringRes int textResId, @StringRes int
            btnTextResId, @NonNull OnClickListener clickListener) {
        showCustom(new CustomStateOptions()
                .message(getStringRes(textResId))
                .image(R.drawable.stf_ic_error)
                .buttonText(getStringRes(btnTextResId))
                .buttonClickListener(clickListener));
    }

    // offline

    public void showOffline(@NonNull OnClickListener clickListener) {
        showOffline(R.string.stfOfflineMessage, clickListener);
    }

    public void showOffline(@StringRes int resId, @NonNull OnClickListener clickListener) {
        showOffline(getStringRes(resId), clickListener);
    }

    public void showOffline(String message, @NonNull OnClickListener clickListener) {
        showCustom(new CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_offline)
                .buttonText(getStringRes(R.string.stfButtonRetry))
                .buttonClickListener(clickListener));
    }

    public void showOffline(String message, String
            btnText, @NonNull OnClickListener clickListener) {
        showCustom(new CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_offline)
                .buttonText(btnText)
                .buttonClickListener(clickListener));
    }

    public void showOffline(@StringRes int textResId, @StringRes
            int btnTextResId, @NonNull OnClickListener clickListener) {
        showCustom(new CustomStateOptions()
                .message(getStringRes(textResId))
                .image(R.drawable.stf_ic_offline)
                .buttonText(getStringRes(btnTextResId))
                .buttonClickListener(clickListener));
    }

    // custom //

    /**
     * Shows custom state for given options. If you do not set buttonClickListener, the button
     * will not be shown
     *
     * @param options customization options
     */
    public void showCustom(final CustomStateOptions options) {
        //启用动画了的
        if (isAnimationEnabled()) {
            stContainer.clearAnimation();
            content.clearAnimation();
            final int animCounterCopy = ++animCounter;

            //如果多样式布局不可见
            if (stContainer.getVisibility() == GONE) {
                outAnimation.setAnimationListener(new CustomAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (animCounterCopy != animCounter) {
                            return;
                        }
                        content.setVisibility(GONE);
                        stContainer.setVisibility(VISIBLE);
                        stContainer.startAnimation(inAnimation);
                    }
                });
                content.startAnimation(outAnimation);
                state(options);
            } else {
                outAnimation.setAnimationListener(new CustomAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (animCounterCopy != animCounter) {
                            return;
                        }
                        state(options);
                        stContainer.startAnimation(inAnimation);
                    }
                });
                stContainer.startAnimation(outAnimation);
            }
        } else {
            content.setVisibility(GONE);
            stContainer.setVisibility(VISIBLE);
            state(options);
        }
    }

    // helper methods //

    /**
     * 根据状态model配置当前的界面应该显示什么
     */
    private void state(CustomStateOptions options) {
        if (!TextUtils.isEmpty(options.getMessage())) {
            stMessage.setVisibility(VISIBLE);
            stMessage.setText(options.getMessage());
        } else {
            stMessage.setVisibility(GONE);
        }

        if (options.isLoading()) {
            stProgress.setVisibility(VISIBLE);
            stImage.setVisibility(GONE);
            stButton.setVisibility(GONE);
        } else {
            stProgress.setVisibility(GONE);
            if (options.getImageRes() != 0) {
                stImage.setVisibility(VISIBLE);
                stImage.setImageResource(options.getImageRes());
            } else {
                stImage.setVisibility(GONE);
            }

            if (options.getClickListener() != null) {
                stButton.setVisibility(VISIBLE);
                stButton.setOnClickListener(options.getClickListener());
                if (!TextUtils.isEmpty(options.getButtonText())) {
                    stButton.setText(options.getButtonText());
                }
            } else {
                stButton.setVisibility(GONE);
            }
        }
    }

    private String getStringRes(@StringRes int resId) {
        return getContext().getString(resId);
    }

    private Animation loadAnim(@AnimRes int resId) {
        return AnimationUtils.loadAnimation(getContext(), resId);
    }

}
