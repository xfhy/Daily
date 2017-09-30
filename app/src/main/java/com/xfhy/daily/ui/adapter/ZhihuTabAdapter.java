package com.xfhy.daily.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * author feiyang
 * create at 2017/9/30 17:18
 * description：
 */
public class ZhihuTabAdapter extends FragmentPagerAdapter {

    /**
     * 知乎的标题栏是直接定了的
     */
    private final static String[] TAB_TITLES = new String[]{"日报", "主题", "专栏", "热门"};
    /**
     * 用来盛放Fragment列表
     */
    private List<Fragment> fragmentList;

    public ZhihuTabAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
        if (fragmentList == null) {
            throw new IllegalArgumentException("知乎模块tab不能为空");
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (fragmentList.size() > position) {
            return fragmentList.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return TAB_TITLES[0];
            case 1:
                return TAB_TITLES[1];
            case 2:
                return TAB_TITLES[2];
            case 3:
                return TAB_TITLES[3];
            default:
                return TAB_TITLES[0];
        }
    }
}
