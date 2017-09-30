package com.xfhy.daily.ui.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.xfhy.androidbasiclibs.basekit.activity.BaseActivity;
import com.xfhy.daily.R;

import butterknife.BindView;

/**
 * author xfhy
 * create at 2017/9/12 21:11
 * description：主界面
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_main)
    CoordinatorLayout activityMain;

    @Override
    protected void initView() {
        setToolBar(mToolbar, getResources().getString(R.string.drawer_menu_zhihu));
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
        /*
        请求网络的demo   以后需要写在presenter里面的

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
        }*/

    }

    /**
     * 设置toolbar的标题
     *
     * @param toolbar Toolbar
     * @param title   标题
     */
    protected void setToolBar(Toolbar toolbar, String title) {
        //setSupportActionBar之前设置标题
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            //显示左上角的按钮
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
    }

}
