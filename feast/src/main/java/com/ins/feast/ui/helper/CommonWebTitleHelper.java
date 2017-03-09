package com.ins.feast.ui.helper;

import android.view.View;
import android.widget.TextView;

import com.ins.feast.R;
import com.ins.feast.ui.activity.CommonWebActivity;
import com.ins.feast.utils.RxViewUtils;
import com.ins.middle.base.TitleHelper;
import com.ins.middle.entity.WebEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * author 边凌
 * date 2017/3/2 11:36
 * desc ${TODO}
 */

public class CommonWebTitleHelper extends TitleHelper implements View.OnClickListener {
    private CommonWebActivity commonWebActivity;
    /*有标题栏情况下的返回键*/
    private View leftIcon_back_1;
    /*无标题栏情况下的返回键*/
    private View leftIcon_back_2;
    /*购物车图标，在无标题栏情况下呈现*/
    private View rightIcon_car;

    private View rootView;
    private TextView titleCenter;

    public CommonWebTitleHelper(CommonWebActivity commonWebActivity) {
        this.commonWebActivity = commonWebActivity;
        findView();
        setListener();
    }

    private void setListener() {
        leftIcon_back_1.setOnClickListener(this);
        leftIcon_back_2.setOnClickListener(this);
        RxViewUtils.throttleFirst(rightIcon_car, this);
    }

    private void findView() {
        leftIcon_back_1 = commonWebActivity.findViewById(R.id.toolbar_leftIcon);
        leftIcon_back_2 = commonWebActivity.findViewById(R.id.leftIcon_back);
        rightIcon_car = commonWebActivity.findViewById(R.id.rightIcon_car);
        rootView = commonWebActivity.findViewById(R.id.appBarLayout);
        titleCenter = (TextView) commonWebActivity.findViewById(R.id.toolbar_title);
    }

    @Override
    public void handleTitleStyleByTag(TitleType type) {
        switch (type) {
            case centerAndBackIcon:
                style_CenterAndBackIcon();
                break;
            case onlyCenter:
                style_OnlyCenter();
                break;
            case noTitleButHaveIcon:
                style_NoTitleButHaveIcon();
                break;
        }
    }

    private void style_NoTitleButHaveIcon() {
        rootView.setVisibility(View.GONE);
        leftIcon_back_2.setVisibility(View.VISIBLE);
        rightIcon_car.setVisibility(View.VISIBLE);
    }

    private void style_OnlyCenter() {
        rootView.setVisibility(View.VISIBLE);
        leftIcon_back_1.setVisibility(View.GONE);
        leftIcon_back_2.setVisibility(View.GONE);
        rightIcon_car.setVisibility(View.GONE);
    }

    private void style_CenterAndBackIcon() {
        rootView.setVisibility(View.VISIBLE);
        leftIcon_back_1.setVisibility(View.VISIBLE);
        leftIcon_back_2.setVisibility(View.GONE);
        rightIcon_car.setVisibility(View.GONE);
    }

    @Override
    protected TextView bindTitleCenter() {
        return titleCenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightIcon_car:
                EventBus.getDefault().post(WebEvent.jumpToCarTab);
            case R.id.toolbar_leftIcon:
            case R.id.leftIcon_back:
                commonWebActivity.onBackPressed();
                break;
        }
    }
}
