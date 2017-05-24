package com.ins.middle.base;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.ins.middle.common.AppData;
import com.sobey.common.utils.UrlUtil;

/**
 * author 边凌
 * date 2017/3/2 11:24
 * desc ${标题栏辅助类}
 */

public abstract class TitleHelper {
    private final static String TAG_NAME = "isOpen";
    private TextView titleView;

    public abstract void handleTitleStyleByTag(TitleType type);

    protected abstract TextView bindTitleCenter();

    public final void setTitleText(String title) {
        if (titleView == null) {
            titleView = bindTitleCenter();
        }

        if (!TextUtils.equals(title, AppData.Config.ERROR_PAGE_TITLE)) {
            titleView.setText(title);
        }
    }

    /**
     * 根据url切换标题栏样式
     *
     * @param url 当前网页的url
     */
    @CallSuper
    public void handleTitleWithUrl(String url) {
        try {
            TitleType type = generateTagByUrl(url);
            handleTitleStyleByTag(type);
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
    final
    @Nullable
    TitleType generateTagByUrl(String url) {
        String queryString = UrlUtil.getQueryString(url, TAG_NAME);
        if (TextUtils.isEmpty(queryString)) {
            return TitleType.error;
        }
        switch (queryString) {
            case "0":
                return TitleType.onlyCenter;
            case "1":
                return TitleType.centerAndBackIcon;
            case "2":
                return TitleType.home;
            case "3":
                return TitleType.noTitle;
             /*这个4是自己设的一个属性，因为这种布局在本地就可以判断是否需要,具体在Feast module 的CardActivity可以看到*/
            case "4":
                return TitleType.noTitleButHaveIcon;
        }
        return TitleType.error;
    }

    public enum TitleType {home, onlyCenter, centerAndBackIcon, noTitle, noTitleButHaveIcon, error}
}
