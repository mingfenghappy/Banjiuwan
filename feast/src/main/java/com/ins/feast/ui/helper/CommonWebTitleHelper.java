package com.ins.feast.ui.helper;

import android.view.View;
import android.widget.TextView;

import com.ins.feast.R;
import com.ins.feast.ui.activity.CommonWebActivity;

/**
 * author 边凌
 * date 2017/3/2 11:36
 * desc ${TODO}
 */

public class CommonWebTitleHelper extends TitleHelper implements View.OnClickListener {
    private CommonWebActivity commonWebActivity;
    private View leftIcon;
    private View leftIcon_gray;
    private View rootView;
    private TextView titleCenter;

    public CommonWebTitleHelper(CommonWebActivity commonWebActivity) {
        this.commonWebActivity = commonWebActivity;
        findView();
        setListener();
    }

    private void setListener() {
        leftIcon.setOnClickListener(this);
        leftIcon_gray.setOnClickListener(this);
    }

    private void findView() {
        leftIcon = commonWebActivity.findViewById(R.id.toolbar_leftIcon);
        leftIcon_gray = commonWebActivity.findViewById(R.id.toolbar_leftIcon_gray);
        rootView = commonWebActivity.findViewById(R.id.appBarLayout);
        titleCenter = (TextView) commonWebActivity.findViewById(R.id.toolbar_title);
    }

    @Override
    public void handleTitleStyleByTag(TitleType type) {
        switch (type) {
            case centerAndBackIcon:
                rootView.setVisibility(View.VISIBLE);
                leftIcon.setVisibility(View.VISIBLE);
                leftIcon_gray.setVisibility(View.GONE);
                break;
            case onlyCenter:
                rootView.setVisibility(View.VISIBLE);
                leftIcon.setVisibility(View.GONE);
                leftIcon_gray.setVisibility(View.GONE);
                break;
            case noTitle:
                rootView.setVisibility(View.GONE);
                leftIcon_gray.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected TextView bindTitleCenter() {
        return titleCenter;
    }

    @Override
    public void onClick(View v) {
        commonWebActivity.onBackPressed();
    }
}
