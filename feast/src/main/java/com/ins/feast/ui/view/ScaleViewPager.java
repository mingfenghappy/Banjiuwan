package com.ins.feast.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/10.
 */
public class ScaleViewPager extends ViewPager {

    //高宽比
    public static final float LV = 1.65f / 1f;

    public ScaleViewPager(Context context) {
        super(context);
    }

    public ScaleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (width * LV), mode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
