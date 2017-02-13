package com.ins.feast.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.ins.baidumapsdk.Locationer;
import com.ins.feast.R;
import com.ins.feast.common.AppData;
import com.shelwee.update.UpdateHelper;
import com.sobey.common.utils.PermissionsUtil;

public class HomeActivity extends BaseAppCompatActivity implements Locationer.LocationCallback {

    private Locationer locationer;
    private TextView text_hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initBase();
        initView();
    }

    private void initBase() {
        //初始化定位
        locationer = new Locationer(this);
        locationer.setCallback(this);
        //双击返回键退出
        setNeedDoubleClickExit(true);
        //检查并申请权限
        PermissionsUtil.checkAndRequestPermissions(this);
        //检查更新
        new UpdateHelper.Builder(this).checkUrl(AppData.Url.version_passenger).isHintNewVersion(false).build().check();
    }

    private void initView() {
        text_hello = (TextView) findViewById(R.id.text_hello);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationer.stopLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationer.startlocation();
    }

    @Override
    public void onLocation(LatLng latLng, String city, String district, boolean isFirst) {
        if (isFirst) text_hello.append("\n当前城市："+city+"\n区域："+district+"\n坐标："+latLng.toString());
    }
}
