package com.ins.feast.ui.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.ins.feast.R;

/**
 * author 边凌
 * date 2017/2/22 10:09
 * desc ${TODO}
 */

public class WebViewBehavior extends CoordinatorLayout.Behavior{
    public WebViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency.getId()== R.id.appBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if(dependency.getVisibility()==View.VISIBLE){
            child.setY(dependency.getY()+dependency.getHeight());
        }else {
            child.setY(dependency.getY());
        }
        return true;
    }
}
