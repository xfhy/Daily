package com.xfhy.daily.ui.fragment.zhihu;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xfhy.androidbasiclibs.basekit.fragment.BaseFragment;
import com.xfhy.daily.R;
import com.xfhy.daily.ui.adapter.ZhihuTabAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author feiyang
 * create at 2017/9/30 15:34
 * description：知乎板块的主fragment
 */
public class ZhihuFragment extends BaseFragment {

    @BindView(R.id.tl_zhihu)
    TabLayout mTabLayout;
    @BindView(R.id.vp_module_zhihu)
    ViewPager mViewPager;

    public static ZhihuFragment newInstance() {

        Bundle args = new Bundle();

        ZhihuFragment fragment = new ZhihuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViewEvent() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_zhuhu_main;
    }

    @Override
    protected void initView() {
        ZhihuTabAdapter zhihuTabAdapter = new ZhihuTabAdapter(mActivity.getSupportFragmentManager
                (), getTestData());
        mViewPager.setAdapter(zhihuTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * 测试
     */
    private List<Fragment> getTestData() {
        List<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ZhihuLatestDailyFragment zhihuLatestDailyFragment = ZhihuLatestDailyFragment
                    .newInstance();
            fragmentList.add(zhihuLatestDailyFragment);
        }
        return fragmentList;
    }


    @Override
    protected void lazyLoad() {

    }
}
