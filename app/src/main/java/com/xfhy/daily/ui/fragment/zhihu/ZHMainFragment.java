package com.xfhy.daily.ui.fragment.zhihu;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.xfhy.androidbasiclibs.basekit.fragment.BaseFragment;
import com.xfhy.daily.R;
import com.xfhy.daily.ui.adapter.ZHTabAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author feiyang
 * create at 2017/9/30 15:34
 * description：知乎板块的主fragment
 */
public class ZHMainFragment extends BaseFragment {

    @BindView(R.id.tl_zhihu)
    TabLayout mTabLayout;
    @BindView(R.id.vp_module_zhihu)
    ViewPager mViewPager;
    private static final int PAGE_COUNT = 4;

    public static ZHMainFragment newInstance() {

        Bundle args = new Bundle();

        ZHMainFragment fragment = new ZHMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViewEvent() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_zh_main;
    }

    @Override
    protected void initView() {
        ZHTabAdapter zhihuTabAdapter = new ZHTabAdapter(getChildFragmentManager
                (), getTabData());
        mViewPager.setAdapter(zhihuTabAdapter);
        // 设置默认的缓存个数
        mViewPager.setOffscreenPageLimit(PAGE_COUNT);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * 获取tab数据
     */
    private List<Fragment> getTabData() {
        List<Fragment> fragmentList = new ArrayList<>();
        ZHLatestDailyFragment zhLatestDailyFragment = ZHLatestDailyFragment.newInstance();
        ZHThemeFragment zhThemeFragment = ZHThemeFragment.newInstance();
        ZHSectionFragment zhSectionFragment = ZHSectionFragment.newInstance();
        ZHHotFragment zhHotFragment = ZHHotFragment.newInstance();
        fragmentList.add(zhLatestDailyFragment);
        fragmentList.add(zhThemeFragment);
        fragmentList.add(zhSectionFragment);
        fragmentList.add(zhHotFragment);
        return fragmentList;
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
    }
}
