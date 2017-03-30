package com.ins.feast.ui.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ins.feast.R;
import com.ins.feast.common.JSFunctionUrl;
import com.ins.feast.ui.activity.ChooseLocationActivity;
import com.ins.feast.ui.activity.CommonWebActivity;
import com.ins.feast.ui.activity.HomeActivity;
import com.ins.feast.ui.activity.SearchDishesActivity;
import com.ins.feast.utils.RxViewUtils;
import com.ins.middle.base.TitleHelper;
import com.ins.middle.common.AppData;

/**
 * author 边凌
 * date 2017/2/22 11:07
 * desc ${标题栏样式辅助类，功能包括设置标题，标题栏样式切换以及标题栏按键点击事件}
 */

public class HomeTitleHelper extends TitleHelper implements View.OnClickListener {

    private TextView title_location;
    private TextView title_center;
    private View iconRight;
    private HomeActivity homeActivity;
    private ImageView iconLeft;
    private View appBarLayout;

    public HomeTitleHelper(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
        findView(homeActivity);
        initListener();
    }

    private void findView(HomeActivity homeActivity) {
        title_center = (TextView) homeActivity.findViewById(R.id.toolbar_title);
        title_location = (TextView) homeActivity.findViewById(R.id.title_location);
        iconLeft = (ImageView) homeActivity.findViewById(R.id.toolbar_leftIcon);
        appBarLayout = homeActivity.findViewById(R.id.appBarLayout);
        iconRight = homeActivity.findViewById(R.id.icon_right);
    }

    /**
     * 设置监听器
     */
    private void initListener() {
        iconLeft.setOnClickListener(this);
        RxViewUtils.throttleFirst(title_location, this);
        RxViewUtils.throttleFirst(iconRight, this);
    }

    /**
     * 根据tag处理标题栏样式
     *
     * @param type {@link #generateTagByUrl(String)}根据Url获取tag
     */
    @Override
    public void handleTitleStyleByTag(TitleType type) {
        switch (type) {
            case home:
                homeStyle();
                break;
            case onlyCenter:
                onlyCenterStyle();
                break;
            case noTitle:
                noTitleStyle();
                break;
            case centerAndBackIcon:
                centerAndBackIconStyle();
                break;
            default:
                break;
        }
    }

    @Override
    protected TextView bindTitleCenter() {
        return title_center;
    }

    /**
     * 统一处理左边图标，右边图标以及地理位置TextView的可见性
     */
    private void setIconVisibility(int visible) {
        iconLeft.setVisibility(visible);
        iconRight.setVisibility(visible);
        title_location.setVisibility(visible);
    }

    /**
     * 仅有中间标题的标题栏布局
     */
    private void onlyCenterStyle() {
        appBarLayout.setVisibility(View.VISIBLE);
        setIconVisibility(View.GONE);
    }

    /**
     * 标题栏透明的布局
     */
    private void noTitleStyle() {
        appBarLayout.setVisibility(View.GONE);
    }

    /**
     * 带返回键和中部title的标题栏布局
     */
    private void centerAndBackIconStyle() {
        appBarLayout.setVisibility(View.VISIBLE);
        iconLeft.setImageResource(R.mipmap.ic_leftarrow_white);
        iconLeft.setTag(true);
        iconLeft.setVisibility(View.VISIBLE);
        iconRight.setVisibility(View.GONE);
        title_location.setVisibility(View.GONE);
    }

    /**
     * 仅用于首页的标题栏布局
     */
    private void homeStyle() {
        appBarLayout.setVisibility(View.VISIBLE);
        iconLeft.setImageResource(R.mipmap.ic_mark);
        iconLeft.setTag(false);
        setIconVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_leftIcon:
                boolean canOnBackPressed = (boolean) iconLeft.getTag();
                if (canOnBackPressed) {
                    homeActivity.onBackPressed();
                }
                break;
            case R.id.title_location:
                ChooseLocationActivity.start(homeActivity, 1);
                break;
            case R.id.icon_right:
//                SearchDishesActivity.start(homeActivity);
                CommonWebActivity.start(homeActivity, AppData.Url.search);
                break;
        }
    }

}
