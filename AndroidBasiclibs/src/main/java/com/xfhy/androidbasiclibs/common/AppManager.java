package com.xfhy.androidbasiclibs.common;

import com.xfhy.androidbasiclibs.basekit.activity.BaseActivity;

import java.util.Stack;

/**
 * author feiyang
 * create at 2017/9/15 13:43
 * description：管理整个app的Activity
 */
public class AppManager {

    /**
     * 维护Activity的集合
     */
    private static Stack<BaseActivity> mActivities = new Stack<>();
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 获取AppManager
     *
     * @return 获取AppManager
     */
    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加一个activity到管理集合里
     *
     * @param activity 需要进行添加的Activity
     */
    public void addActivity(BaseActivity activity) {
        if (mActivities == null || mActivities.isEmpty()) {
            return;
        }
        mActivities.add(activity);
    }

    /**
     * 获取当前Activity (最后一个添加进来的)
     *
     * @return 返回当前Activity
     */
    public BaseActivity getCurrentActivity() {
        if (mActivities == null || mActivities.isEmpty()) {
            return null;
        }
        return mActivities.get(mActivities.size() - 1);
    }

    /**
     * 结束当前Activity
     */
    public void finishCurrentActivity() {
        if (mActivities == null || mActivities.isEmpty()) {
            return;
        }
        BaseActivity activity = mActivities.get(mActivities.size() - 1);
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity 需要结束的Activity
     */
    public void finishActivity(BaseActivity activity) {
        if (mActivities == null || mActivities.isEmpty()) {
            return;
        }
        BaseActivity baseActivity = mActivities.get(mActivities.size() - 1);
        if (baseActivity != null) {
            baseActivity.finish();
            baseActivity = null;
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param cls 需要结束的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (mActivities == null || mActivities.isEmpty()) {
            return;
        }
        for (BaseActivity activityTemp : mActivities) {
            if (activityTemp.getClass().equals(cls)) {
                finishActivity(activityTemp);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (mActivities == null || mActivities.isEmpty()) {
            return;
        }
        for (BaseActivity activity : mActivities) {
            finishActivity(activity);
        }
        mActivities.clear();
    }

}
