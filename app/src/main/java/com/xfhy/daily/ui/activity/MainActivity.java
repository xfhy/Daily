package com.xfhy.daily.ui.activity;

import com.xfhy.androidbasiclibs.basekit.activity.BaseActivity;
import com.xfhy.androidbasiclibs.common.util.DevicesUtils;
import com.xfhy.androidbasiclibs.common.util.LogUtils;
import com.xfhy.androidbasiclibs.common.util.ToastUtil;
import com.xfhy.daily.R;
import com.xfhy.daily.network.RetrofitHelper;
import com.xfhy.daily.network.entity.zhihu.HotDailyBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * author xfhy
 * create at 2017/9/12 21:11
 * description：主界面
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void initView() {
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewEvent() {

    }

    @Override
    protected void initData() {
        RetrofitHelper retrofitHelper = RetrofitHelper.getInstance();
        if (DevicesUtils.isNetworkConnected(this)) {
            retrofitHelper.getZhiHuApi().getHotDailyList()
                    .compose(this.<HotDailyBean>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<HotDailyBean>() {
                        @Override
                        public void accept(HotDailyBean s) throws Exception {
                            LogUtils.e(s.toString());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e(throwable.getLocalizedMessage());
                        }
                    });
        } else {
            ToastUtil.showMessage(this, "没有网络");
        }

    }
}
