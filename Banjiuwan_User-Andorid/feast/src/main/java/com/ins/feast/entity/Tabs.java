package com.ins.feast.entity;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.widget.RadioButton;

import com.ins.feast.R;
import com.ins.middle.base.TitleHelper;
import com.ins.middle.common.AppData;

/**
 * author 边凌
 * date 2017/3/9 14:26
 * desc ${主页Tab的辅助类}
 */

public class Tabs {
    private String title;
    private
    @IdRes
    int buttonId;
    private String urlTag;
    private TitleHelper.TitleType titleType;
    private String url;

    private static Tabs[] values;

    static {
        values = new Tabs[5];
        values[0] = new Tabs("办酒碗", R.id.rb_home, "index", TitleHelper.TitleType.home, AppData.Url.app_home);
        values[1] = new Tabs("购物车", R.id.rb_cart, "car", TitleHelper.TitleType.onlyCenter, AppData.Url.app_cart);
        values[2] = new Tabs("发现", R.id.rb_find, "find", TitleHelper.TitleType.onlyCenter, AppData.Url.app_find);
        values[3] = new Tabs("", R.id.rb_customerService, "customer", TitleHelper.TitleType.noTitle, AppData.Url.app_customer_service);
        values[4] = new Tabs("我的", R.id.rb_mine, "my", TitleHelper.TitleType.onlyCenter, AppData.Url.app_mine);
    }

    private Tabs(String title, @IdRes int buttonId, String urlTag, TitleHelper.TitleType titleType, String url) {
        this.title = title;
        this.buttonId = buttonId;
        this.urlTag = urlTag;
        this.titleType = titleType;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public
    @IdRes
    int getButtonId() {
        return buttonId;
    }

    public String getUrlTag() {
        return urlTag;
    }

    public TitleHelper.TitleType getTitleType() {
        return titleType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获得默认的5个tab
     */
    public static Tabs[] values() {
        return values;
    }

    public static void init() {
        values[0].setUrl(AppData.Url.app_home);// = new Tabs("办酒碗", R.id.rb_home, "index", TitleHelper.TitleType.home, );
        values[1].setUrl(AppData.Url.app_cart);// = new Tabs("购物车", R.id.rb_cart, "car", TitleHelper.TitleType.onlyCenter, AppData.Url.app_cart);
        values[2].setUrl(AppData.Url.app_find);// = new Tabs("发现", R.id.rb_find, "find", TitleHelper.TitleType.onlyCenter, AppData.Url.app_find);
        values[3].setUrl(AppData.Url.app_customer_service);// = new Tabs("", R.id.rb_customerService, "customer", TitleHelper.TitleType.noTitle, AppData.Url.app_customer_service);
        values[4].setUrl(AppData.Url.app_mine);// = new Tabs("我的", R.id.rb_mine, "my", TitleHelper.TitleType.onlyCenter, AppData.Url.app_mine);
    }

    //通用工具方法：通过buttonId 查找 该button对应的url
    public static String getUrlById(int buttonId) {
        for (Tabs tab : Tabs.values()) {
            if (tab.getButtonId() == buttonId) {
                return tab.getUrl();
            }
        }
        return "";
    }

    //通用工具方法：通过buttonId 查找 该button对应的url
    public static RadioButton getRadioButtonByUrl(Activity activity, String url) {
        for (Tabs tab : Tabs.values()) {
            if (url.equals(tab.getUrl())) {
                return (RadioButton) activity.findViewById(tab.getButtonId());
            }
        }
        return null;
    }
}
