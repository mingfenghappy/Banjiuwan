package com.sobey.common.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.sobey.common.common.MyActivityCollector;

/**
 * author 边凌
 * date 2017/2/28 15:38
 * desc ${AppCompatActivity基类}
 */

public class BaseAppCompatActivity extends AppCompatActivity{
    //双击退出
    private boolean needDoubleClickExit = false;
    private long exitTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        MyActivityCollector.addActivity(this);
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
        }else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("unused")
    public void setNeedDoubleClickExit(boolean needDoubleClickExit) {
        this.needDoubleClickExit = needDoubleClickExit;
    }
}
