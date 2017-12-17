package com.xfhy.daily.ui.fragment.zhihu;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xfhy.androidbasiclibs.basekit.fragment.BaseFragment;
import com.xfhy.androidbasiclibs.basekit.presenter.BasePresenter;
import com.xfhy.androidbasiclibs.basekit.view.BaseView;
import com.xfhy.androidbasiclibs.util.DevicesUtils;
import com.xfhy.androidbasiclibs.util.SnackbarUtil;
import com.xfhy.androidbasiclibs.widget.StatefulLayout;
import com.xfhy.daily.R;

import butterknife.BindView;

/**
 * @author xfhy
 *         create at 2017/11/26 16:50
 *         description：所有需要刷新和请求网络的fragment可以继承自该fragment,已实现基本的展示空布局,刷新布局,显示错误信息等
 *         MVP架构,布局中必须是有StatefulLayout,并且id为sfl_state_view
 */
public abstract class BaseStateMVPFragment<P extends BasePresenter> extends BaseFragment implements
        BaseView {

    protected P mPresenter;

    @BindView(R.id.sfl_state_view)
    protected StatefulLayout mStateView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.attachView(this);
            mPresenter.onCreate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    /**
     * 初始化presenter
     */
    public abstract void initPresenter();

    @Override
    public void onLoading() {
        mStateView.showLoading();
    }

    @Override
    public void closeLoading() {
        mStateView.showContent();
    }

    @Override
    public void showErrorMsg(String msg) {
        closeLoading();
        SnackbarUtil.showBarLongTime(mRootView, msg, SnackbarUtil.ALERT);
    }

    @Override
    public void showEmptyView() {
        closeRefresh();
        mStateView.showEmpty(R.string.stfEmptyMessage, R.string.stfButtonRetry);
    }

    /**
     * 停止刷新
     */
    public abstract void closeRefresh();

    @Override
    public void showOffline() {
        closeRefresh();
        mStateView.showOffline(R.string.stfOfflineMessage, R.string.stfButtonSetting, new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                //未联网  跳转到设置界面
                DevicesUtils.goSetting(mActivity);
            }
        });
    }


    @Override
    public void showContent() {
        closeRefresh();
        mStateView.showContent();
    }

}
