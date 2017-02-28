package com.ins.feast.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ins.feast.R;

/**
 * author 边凌
 * date 2017/2/27 11:29
 * desc ${TODO}
 */

public class NoNetView extends TextView {

    public NoNetView(Context context) {
        super(context);
        init();
    }

    public NoNetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoNetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NoNetView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_no_net);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        setCompoundDrawables(null, drawable, null, null);
    }


}
