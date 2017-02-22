package com.ins.feast.ui.activity;

import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ins.feast.R;
import com.ins.feast.utils.RxViewUtils;
import com.sobey.common.utils.L;

import java.util.Stack;

/**
 * author 边凌
 * date 2017/2/22 11:07
 * desc ${TODO}
 */

class TitleViewHelper implements View.OnClickListener {
    private final static String KEY_HOME = "app/page/index";
    private final static String KEY_CAR = "app/page/car";
    private final static String KEY_FIND = "app/page/find";
    private final static String KEY_CUSTOMER = "app/page/customer";
    private final static String KEY_MY = "app/page/my";
    //菜单
    private final static String KEY_COOKBOOK = "app/page/cookbook";
    //坝坝宴
    private final static String KEY_BAMYAN = "app/page/bamYan";
    //套餐
    private final static String KEY_SETMEAL = "app/page/setMeal";
    //修改个人资料
    private final static String KEY_PERSONAL_DATA = "app/page/personData";
    private TextView title_location;
    private TextView title_center;
    private View iconRight;
    private HomeActivity homeActivity;
    private ImageView iconLeft;
    private View appBarLayout;

    private Stack<TitleInfo> urlStack = new Stack<>();

    public void setOnClickListener(@IdRes int id, View.OnClickListener listener) {
        View viewById = homeActivity.findViewById(id);
        if (viewById != null) {
            viewById.setOnClickListener(listener);
        }
    }

    TitleViewHelper(HomeActivity homeActivity) {
        title_center = (TextView) homeActivity.findViewById(R.id.text_toolbar_title);
        title_location = (TextView) homeActivity.findViewById(R.id.title_location);
        iconLeft = (ImageView) homeActivity.findViewById(R.id.icon_left);
        appBarLayout = homeActivity.findViewById(R.id.appBarLayout);
        iconRight = homeActivity.findViewById(R.id.icon_right);
        this.homeActivity = homeActivity;
        urlStack.push(new TitleInfo(KEY_HOME,"办酒碗"));
        initListener();
    }

    /**
     * 设置监听器
     */
    private void initListener() {
        iconLeft.setOnClickListener(this);
        RxViewUtils.throttleFirst(title_location, this);
    }

    private String processTitleWithUrl(String url) {
        L.d("processTitleWithUrl");
        String key = null;
        if (url.contains(KEY_HOME)) {
            showTitleBarHome();
            key = KEY_HOME;
        } else if (url.contains(KEY_FIND)) {
            showTitleBarOnlyCenter();
            key = KEY_FIND;
        } else if (url.contains(KEY_CUSTOMER)) {
            showTitleBarCustomer();
            key = KEY_CUSTOMER;
        } else if (url.contains(KEY_CAR)) {
            showTitleBarOnlyCenterAndBackIcon();
            key = KEY_CAR;
        } else if (url.contains(KEY_MY)) {
            showTitleBarOnlyCenter();
            key = KEY_MY;
        } else if (url.contains(KEY_COOKBOOK)) {
            showTitleBarOnlyCenterAndBackIcon();
            key = KEY_COOKBOOK;
        } else if (url.contains(KEY_BAMYAN)) {
            showTitleBarOnlyCenterAndBackIcon();
            key = KEY_BAMYAN;
        } else if (url.contains(KEY_SETMEAL)) {
            showTitleBarOnlyCenterAndBackIcon();
            key = KEY_SETMEAL;
        } else if (url.contains(KEY_PERSONAL_DATA)) {
            showTitleBarOnlyCenterAndBackIcon();
            key = KEY_PERSONAL_DATA;
        }
        L.d("key:"+key);
        return key;
    }

    void processTitleWithUrlWhenGoNewPage(String url) {
        String key = processTitleWithUrl(url);
        if (!TextUtils.isEmpty(key)) {
            urlStack.push(new TitleInfo(key,title_center.getText().toString()));
            L.d("goNewPage:"+key+"\nurlStack:"+urlStack);
        }
    }

    void processTitleWithUrlWhenGoBack() {
        TitleInfo pop = urlStack.pop();
        title_center.setText(urlStack.peek().title);
        L.d("whenGoBack:"+pop);
        processTitleWithUrl(urlStack.peek().urlKey);
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
     * 仅有中间标题和左侧返回键的样式
     */
    private void showTitleBarOnlyCenter() {
        appBarLayout.setVisibility(View.VISIBLE);
        setIconVisibility(View.GONE);
    }

    //客服
    private void showTitleBarCustomer() {
        appBarLayout.setVisibility(View.GONE);
    }

    //购物车
    private void showTitleBarOnlyCenterAndBackIcon() {
        appBarLayout.setVisibility(View.VISIBLE);
        iconLeft.setImageResource(R.mipmap.ic_leftarrow_white);
        iconLeft.setVisibility(View.VISIBLE);
        iconRight.setVisibility(View.GONE);
        title_location.setVisibility(View.GONE);
    }

    //首页
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

    private class TitleInfo{
        String urlKey;
        String title;

        TitleInfo(String urlKey, String title) {
            this.urlKey = urlKey;
            this.title = title;
        }

        @Override
        public String toString() {
            return "TitleInfo{" +
                    "urlKey='" + urlKey + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}
