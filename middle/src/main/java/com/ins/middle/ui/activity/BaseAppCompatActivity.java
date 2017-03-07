package com.ins.middle.ui.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ins.middle.R;
import com.sobey.common.common.MyActivityCollector;
import com.sobey.common.utils.StrUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by Administrator on 2016/7/1 0001.
 */
public class BaseAppCompatActivity extends AppCompatActivity {

    protected final static String JS_BRIDGE_NAME = "JSBridge";
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //禁止横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //设置状态栏深色文字
        //StatusBarTextUtil.StatusBarLightMode(this);
        //添加到activity集合
        MyActivityCollector.addActivity(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyActivityCollector.removeActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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


    //双击退出
    private boolean needDoubleClickExit = false;
    private long exitTime;

    @Override
    public void onBackPressed() {
        //双击退出
        if (needDoubleClickExit) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void setNeedDoubleClickExit(boolean needDoubleClickExit) {
        this.needDoubleClickExit = needDoubleClickExit;
    }
}
