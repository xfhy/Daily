package com.xfhy.androidbasiclibs.common.util;

import android.content.Context;

import com.xfhy.androidbasiclibs.R;
import com.xfhy.androidbasiclibs.uihelper.widget.LoadingDialog;

/**
 * author feiyang
 * time create at 2017/11/15 21:12
 * description 加载中布局Util
 */
public class LoadDialogUtil {

    private static LoadingDialog mDialog;

    public static boolean isLoading() {
        if (mDialog != null) {
            return mDialog.isShowing();
        }
        return false;
    }

    public static void show(Context context) {
        mDialog = new LoadingDialog(context, R.style.loadingDialog);
        mDialog.show();
    }

    public static void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

}
