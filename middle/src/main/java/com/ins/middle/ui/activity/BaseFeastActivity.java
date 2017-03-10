package com.ins.middle.ui.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.TextView;

import com.ins.middle.R;
import com.sobey.common.base.BaseAppCompatActivity;
import com.sobey.common.utils.StrUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Administrator on 2016/7/1 0001.
 */
public class BaseFeastActivity extends BaseAppCompatActivity {

    protected final static String JS_BRIDGE_NAME = "JSBridge";
    protected Toolbar toolbar;
    private WebView webView;
    private boolean eventBusSupport = false;

    /**
     * 设置WebView生命周期支持
     */
    protected final void setWebViewLifeCycleSupport(WebView webView) {
        this.webView = webView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置EventBus注册，自动处理EventBus的反注册
     */
    public void setEventBusSupport() {
        if (eventBusSupport){
            return;
        }
        eventBusSupport = true;
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        if (eventBusSupport) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void setToolbar(boolean needback) {
        setToolbar(null, needback);
    }

    public void setToolbar() {
        setToolbar(null, true);
    }

    public void setToolbar(String title) {
        setToolbar(title, true);
    }

    /**
     * 把toolbar设置为""，把自定义居中文字设置为title，设置是否需要返回键
     */
    public void setToolbar(String title, boolean needback) {
        //设置toobar文字图标和返回事件
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.icon_back);
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.sb_text_blank));
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(needback);
        }
        //设置toobar居中文字
        TextView text_title = (TextView) findViewById(R.id.toolbar_title);
        if (text_title != null) {
            if (!StrUtils.isEmpty(title)) {
                text_title.setText(title);
            }
        }
    }

}
