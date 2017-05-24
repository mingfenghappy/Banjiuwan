package com.ins.chef.web;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.ins.chef.ui.activity.LoginActivity;
import com.ins.middle.base.BaseJSInterface;
import com.ins.middle.common.AppData;
import com.ins.middle.entity.User;
import com.sobey.common.utils.ActivityUtil;
import com.sobey.common.utils.ApplicationHelp;

/**
 * author 边凌
 * date 2017/2/28 11:07
 * desc ${TODO}
 */

public class ChefJSInterface extends BaseJSInterface {

    @JavascriptInterface
    public String getToken() {
        return AppData.App.getToken();
    }

    @JavascriptInterface
    public void removeToken() {
        AppData.App.removeToken();
    }
}
