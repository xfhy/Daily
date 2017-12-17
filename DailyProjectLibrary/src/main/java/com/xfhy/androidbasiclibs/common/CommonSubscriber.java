package com.xfhy.androidbasiclibs.common;

import android.text.TextUtils;

import com.xfhy.androidbasiclibs.BaseApplication;
import com.xfhy.androidbasiclibs.R;
import com.xfhy.androidbasiclibs.basekit.view.BaseView;
import com.xfhy.androidbasiclibs.util.LogUtils;
import com.xfhy.androidbasiclibs.util.StringUtils;

import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;

/**
 * @author xfhy
 *         create at 2017/12/17 11:02
 *         description： 统一的订阅
 */
public abstract class CommonSubscriber<T> extends ResourceSubscriber<T> {

    private BaseView mView;
    /**
     * 错误消息
     */
    private String mErrorMsg;

    public CommonSubscriber(BaseView view) {
        mView = view;
    }

    public CommonSubscriber(BaseView view, String errorMsg) {
        mView = view;
        mErrorMsg = errorMsg;
    }

    //有错误时才会执行onError
    @Override
    public void onError(Throwable t) {
        if (mView == null) {
            return;
        }
        if (!TextUtils.isEmpty(mErrorMsg)) {
            mView.showErrorMsg(mErrorMsg);
        } else if (t instanceof HttpException) {
            //数据加载失败
            mView.showErrorMsg(StringUtils.getStringByResId(BaseApplication.getApplication(), R
                    .string.data_loading_failed));
        } else {
            //未知错误
            mView.showErrorMsg(StringUtils.getStringByResId(BaseApplication.getApplication(), R
                    .string.unknown_mistake));
            LogUtils.e(t.toString());
        }
    }

    @Override
    public void onComplete() {

    }
}
