package com.sobey.common.interfaces;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public interface OnPagerItemClickListener<T> {
    void onItemClick(T t, int position);
}
