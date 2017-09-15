package com.xfhy.androidbasiclibs.uihelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * author
 * create at 2017/9/15 13:34
 * description：
 */
public abstract class BaseQuickAdapter<T,K extends BaseViewHolder> extends RecyclerView.Adapter<K> {

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(K holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
