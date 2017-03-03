package com.ins.feast.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义卡片布局管理器，从底部开始向上排列
 */

public class CardLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "swipecard";

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //Log.e(TAG, "onLayoutChildren() called with: recycler = [" + recycler + "], state = [" + state + "]");
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();
        if (itemCount < 1) {
            return;
        }
        //top-3View的position
        int bottomPosition;
        //边界处理
        if (itemCount < CardConfig.MAX_SHOW_COUNT) {
            bottomPosition = 0;
        } else {
            bottomPosition = itemCount - CardConfig.MAX_SHOW_COUNT;
        }

        //从可见的最底层View开始layout，依次层叠上去
        for (int position = bottomPosition; position < itemCount; position++) {
            View view = recycler.getViewForPosition(position);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            //获取每个子view的高宽（包括了分割线的宽）
            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
            //我们在布局时，将childView居中处理，这里也可以改为只水平居中
            layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 2 + getDecoratedMeasuredHeight(view));
            /**
             * TopView的Scale 为1，translationY 0
             * 每一级Scale相差0.05f，translationY相差7dp左右
             *
             * 观察人人影视的UI，拖动时，topView被拖动，Scale不变，一直为1.
             * top-1View 的Scale慢慢变化至1，translation也慢慢恢复0
             * top-2View的Scale慢慢变化至 top-1View的Scale，translation 也慢慢变化只top-1View的translation
             * top-3View的Scale要变化，translation岿然不动
             */

            //第几层,举例子，count =7， 最后一个TopView（6）是第0层，
            int level = itemCount - position - 1;

            //除了最后一个view 都放大宽度
            if (level < CardConfig.MAX_SHOW_COUNT - 1) {
                view.setScaleX(1 + CardConfig.SCALE_GAP * level);
            } else {
                //最后一个view 放大到上一级的宽度
                view.setScaleX(1 + CardConfig.SCALE_GAP * (level - 1));
            }

            //除了第一个都缩小高度，并且叠加透明的
            if (level > 0) {
                view.setScaleY(1 - CardConfig.SCALE_GAP * level);
                float alpha = 1f - (float) level / (CardConfig.MAX_SHOW_COUNT - 1);
                view.setAlpha(alpha);
                //Log.e("liao", "level:" + level + " alpha:" + alpha);
            }

        }
    }

    public static class CardConfig {
        //屏幕上最多同时显示几个Item
        public static int MAX_SHOW_COUNT = 4;

        //每一级Scale相差0.05f，translationY相差7dp左右
        public static float SCALE_GAP = 0.08f;
    }

}
