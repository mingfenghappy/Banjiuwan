package com.sobey.common.helper;

import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class SwipeHelper {
    public static void setSwipeListener(final SwipeRefreshLayout swipe, RecyclerView recyclerView , final OnSwiperFreshListener listener){
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动，既是否向右滑动或向下滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    boolean b = !ViewCompat.canScrollVertically(recyclerView, 1);
                    if (b) {
                        if (listener!=null) listener.onLoadmore();
                    }
                }
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (listener!=null) listener.onRefresh();
                    }
                },2000);
            }
        });
    }

    public interface OnSwiperFreshListener{
        /**
         * 下拉刷新，回调接口
         */
        void onRefresh();
        /**
         * 上拉加载，回调接口
         */
        void onLoadmore();
    }
}
