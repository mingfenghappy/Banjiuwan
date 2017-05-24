package com.ins.feast.common;

import android.graphics.Canvas;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.ins.feast.common.CardLayoutManager.CardConfig;
import com.ins.feast.entity.Card;

import java.util.List;


/**
 * 自定义卡片布局管理器拖拽手势回调处理器
 */

public class ItemTouchCardCallback extends ItemTouchHelper.SimpleCallback {

    protected RecyclerView mRv;
    protected List mDatas;
    protected RecyclerView.Adapter mAdapter;

    public ItemTouchCardCallback(RecyclerView rv, List datas) {
        this(0,
                ItemTouchHelper.DOWN | ItemTouchHelper.UP |
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                , rv, datas);
    }

    public ItemTouchCardCallback(int dragDirs, int swipeDirs, RecyclerView rv, List datas) {
        super(dragDirs, swipeDirs);
        mRv = rv;
        mAdapter = rv.getAdapter();
        mDatas = datas;

    }

    //水平方向是否可以被回收掉的阈值
    public float getThreshold(RecyclerView.ViewHolder viewHolder) {
        //考虑 探探垂直上下方向滑动，不删除卡片，这里参照源码写死0.5f
        return mRv.getWidth() * /*getSwipeThreshold(viewHolder)*/ 0.5f;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //Log.e("swipecard", "onSwiped() called with: viewHolder = [" + viewHolder + "], direction = [" + direction + "]");
        //rollBack(viewHolder);
        //★实现循环的要点
        Object remove = mDatas.remove(viewHolder.getLayoutPosition());
        mDatas.add(0, remove);
        Object next = mDatas.get(mDatas.size() - 1);
        mAdapter.notifyDataSetChanged();

        if (onSwipedListener != null)
            onSwipedListener.onSwiped(next, direction);
        Log.e("liao", "onSwiped Position:" + viewHolder.getLayoutPosition() + " index:" + ((Card) remove).getIndex());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        //Log.e("swipecard", "onChildDraw()  viewHolder = [" + viewHolder + "], dX = [" + dX + "], dY = [" + dY + "], actionState = [" + actionState + "], isCurrentlyActive = [" + isCurrentlyActive + "]");
        //if (isCurrentlyActive) {
        //先根据滑动的dxdy 算出现在动画的比例系数fraction
        double swipValue = Math.sqrt(dX * dX + dY * dY);
        double fraction = swipValue / getThreshold(viewHolder);
        //边界修正 最大为1
        if (fraction > 1) {
            fraction = 1;
        }
        //对每个ChildView进行缩放 位移
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = recyclerView.getChildAt(i);
            //第几层,举例子，count =7， 最后一个TopView（6）是第0层，
            int level = childCount - i - 1;

            //除了最后一个view 都放大宽度
            if (level < CardConfig.MAX_SHOW_COUNT - 1) {
                child.setScaleX((float) (1 + CardConfig.SCALE_GAP * level - fraction * CardConfig.SCALE_GAP));
            } else {
                child.setScaleX(1 + CardConfig.SCALE_GAP * (level - 1));
            }

            //除了第一个都缩小高度
            if (level > 0) {
                child.setScaleY((float) (1 - CardConfig.SCALE_GAP * level + fraction * CardConfig.SCALE_GAP));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    float alpha = 1f - (float) (level - fraction) / (CardConfig.MAX_SHOW_COUNT - 1);// + fraction * SCALE_GAP;
                    child.setAlpha(alpha);
                }
            }
        }
    }

    private OnSwipedListener onSwipedListener;

    public void setOnSwipedListener(OnSwipedListener onSwipedListener) {
        this.onSwipedListener = onSwipedListener;
    }

    public interface OnSwipedListener<T> {
        public void onSwiped(T t, int direction);
    }
}
