package com.sobey.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/2/16.
 */

public class GradientTextView extends TextView {

    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mPaint = getPaint();
        //获取字体颜色并计算渐变色
        int textColor = getCurrentTextColor();
        int endColor = textColor & 0x00ffffff; //透明度设为0:（颜色值高8位设置为0）
        LinearGradient mLinearGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0, new int[]{textColor, textColor, endColor}, new float[]{0, 0.7f, 1f}, Shader.TileMode.REPEAT);
        mPaint.setShader(mLinearGradient);
    }
}
