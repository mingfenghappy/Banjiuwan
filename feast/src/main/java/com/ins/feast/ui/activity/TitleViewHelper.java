package com.ins.feast.ui.activity;

import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ins.feast.R;
import com.ins.feast.common.AppData;
import com.ins.feast.utils.RxViewUtils;
import com.sobey.common.utils.L;
import com.sobey.common.utils.UrlUtil;

/**
 * author 边凌
 * date 2017/2/22 11:07
 * desc ${标题栏样式辅助类，功能包括设置标题，标题栏样式切换以及标题栏按键点击事件}
 */

public class TitleViewHelper implements View.OnClickListener {
    private final static String TAG_NAME = "isOpen";
    //仅有中间标题的标题栏布局
    private final static String TAG_CENTER = "0";
    //带返回键和中部title的标题栏布局
    private final static String TAG_CENTER_LEFTICON = "1";
    //仅用于首页的标题栏布局
    private final static String TAG_HOME = "2";
    //没有标题栏的布局
    private final static String TAG_NO_TITLE = "app/page/customer";

    private TextView title_location;
    private TextView title_center;
    private View iconRight;
    private HomeActivity homeActivity;
    private ImageView iconLeft;
    private View appBarLayout;

    public TitleViewHelper(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
        findView(homeActivity);
        initListener();
    }

    private void findView(HomeActivity homeActivity) {
        title_center = (TextView) homeActivity.findViewById(R.id.text_toolbar_title);
        title_location = (TextView) homeActivity.findViewById(R.id.title_location);
        iconLeft = (ImageView) homeActivity.findViewById(R.id.icon_left);
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
     * 根据url切换标题栏样式
     *
     * @param url   当前网页的url
     * @param title 标题栏标题
     */
    public void handleTitleWithUrl(String url, String title) {
        if (!TextUtils.equals(title, AppData.Config.ERROR_PAGE_TITLE)) {
            title_center.setText(title);
        }
        try {
            String tag = generateTag(url);
            handleTitleStyleByTag(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据当前网页的url解析出tag
     *
     * @param url url
     * @return tag
     */
    private
    @Nullable
    String generateTag(String url) {
        return UrlUtil.getQueryString(url, TAG_NAME);
    }

    /**
     * 根据tag处理标题栏样式
     *
     * @param tag {@link #generateTag(String)}根据Url获取tag
     */
    private void handleTitleStyleByTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        switch (tag) {
            case TAG_HOME:
                homeStyle();
                break;
            case TAG_CENTER:
                onlyCenterStyle();
                break;
            case TAG_NO_TITLE:
                noTitleStyle();
                break;
            case TAG_CENTER_LEFTICON:
                centerAndBackIconStyle();
                break;
        }
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
        iconLeft.setVisibility(View.VISIBLE);
        iconRight.setVisibility(View.GONE);
        title_location.setVisibility(View.GONE);
    }

    /**
     * 仅用于首页的标题栏布局
     */
    private void homeStyle() {
        if (title_location.getVisibility() == View.VISIBLE) {
            return;
        }
        appBarLayout.setVisibility(View.VISIBLE);
        iconLeft.setImageResource(R.mipmap.ic_mark);
        setIconVisibility(View.VISIBLE);
    }

    private Window window;

    private boolean lastHasFlags = false;

    // FIXME: 2017/2/23
    private void setTranslucentFlags(boolean hasTranslucentFlags) {
        L.d("setTranslucent:" + hasTranslucentFlags);
        if (lastHasFlags == hasTranslucentFlags) {
            return;
        }
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
        lastHasFlags = hasTranslucentFlags;
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
            case R.id.icon_right:
                SearchDishesActivity.start(homeActivity);
                break;
        }
    }

}
