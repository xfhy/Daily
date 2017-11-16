package com.xfhy.androidbasiclibs.uihelper.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import com.xfhy.androidbasiclibs.R;
import com.xfhy.androidbasiclibs.common.util.DevicesUtils;

/**
 * author feiyang
 * time create at 2017/11/15 19:51
 * description 加载中的dialog
 */
public class LoadingDialog extends AlertDialog {

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener
            cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);

        setCanceledOnTouchOutside(false);
    }

}
