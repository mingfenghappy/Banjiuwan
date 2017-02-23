package com.ins.feast.ui.activity;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ins.feast.R;
import com.ins.feast.utils.RxViewUtils;

/**
 * author 边凌
 * date 2017/2/22 11:07
 * desc ${TODO}
 */

class TitleViewHelper implements View.OnClickListener {
    private final static String KEY_HOME = "app/page/index";
    private final static String KEY_CENTER_LEFTICON = "app/page/car";
    private final static String KEY_CENTER = "app/page/find";
    private final static String KEY_TRANSLUCENT = "app/page/customer";

    private TextView title_location;
    private TextView title_center;
    private View iconRight;
    private HomeActivity homeActivity;
    private ImageView iconLeft;
    private View appBarLayout;

    TitleViewHelper(HomeActivity homeActivity) {
        title_center = (TextView) homeActivity.findViewById(R.id.text_toolbar_title);
        title_location = (TextView) homeActivity.findViewById(R.id.title_location);
        iconLeft = (ImageView) homeActivity.findViewById(R.id.icon_left);
        appBarLayout = homeActivity.findViewById(R.id.appBarLayout);
        iconRight = homeActivity.findViewById(R.id.icon_right);
        this.homeActivity = homeActivity;
        initListener();
    }

    /**
     * 设置监听器
     */
    private void initListener() {
        iconLeft.setOnClickListener(this);
        RxViewUtils.throttleFirst(title_location, this);
    }

    void processTitleWithUrl(String url, String title) {
        title_center.setText(title);
        String tag = url.substring(url.indexOf("Banjiuwan/") + "Banjiuwan/".length(), url.length());
        switchTitleByTag(tag);
    }

    private void switchTitleByTag(String tag) {
        switch (tag) {
            case KEY_HOME:
                showTitleBarHome();
                break;
            case KEY_CENTER:
                showTitleBarOnlyCenter();
                break;
            case KEY_TRANSLUCENT:
                showTitleBarTranslucent();
                break;
            case KEY_CENTER_LEFTICON:
                showTitleBarOnlyCenterAndBackIcon();
                break;
        }

//        setTranslucentFlags(TextUtils.equals(tag, KEY_TRANSLUCENT));
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
    private void showTitleBarOnlyCenter() {
        appBarLayout.setVisibility(View.VISIBLE);
        setIconVisibility(View.GONE);
    }

    private Window window;

    // FIXME: 2017/2/23 
    private void setTranslucentFlags(boolean hasTranslucentFlags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (window == null) {
                window = homeActivity.getWindow();
            }
            if (hasTranslucentFlags) {
                window.addFlags(flagTranslucentStatus);
            } else {
                window.clearFlags(flagTranslucentStatus);
            }
        }
    }

    /**
     *
     */
    private void showTitleBarTranslucent() {
        appBarLayout.setVisibility(View.GONE);
    }

    /**
     * 待返回键和中部title的标题栏布局
     */
    private void showTitleBarOnlyCenterAndBackIcon() {
        appBarLayout.setVisibility(View.VISIBLE);
        iconLeft.setImageResource(R.mipmap.ic_leftarrow_white);
        iconLeft.setVisibility(View.VISIBLE);
        iconRight.setVisibility(View.GONE);
        title_location.setVisibility(View.GONE);
    }

    /**
     * 仅用于首页的标题栏布局
     */
    private void showTitleBarHome() {
        if (title_location.getVisibility() == View.VISIBLE) {
            return;
        }
        appBarLayout.setVisibility(View.VISIBLE);
        iconLeft.setImageResource(R.mipmap.ic_mark);
        setIconVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_left:
                homeActivity.onBackPressed();
                break;
            case R.id.title_location:
                ChooseLocationActivity.start(homeActivity);
                break;
        }
    }

}
