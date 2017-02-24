package com.ins.feast.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ins.feast.R;
import com.ins.feast.ui.dialog.DialogPopupPhoto;
import com.ins.feast.utils.GlideUtil;
import com.sobey.common.helper.CropHelperSys;
import com.sobey.common.utils.PermissionsUtil;

public class TestActivity extends BaseAppCompatActivity implements CropHelperSys.CropInterface {

    //图片选择辅助类 setNeedCrop(boolean):设置是否需要裁剪(默认false)  setNeedPress(boolean):设置是否需要进行压缩(默认true)
    private CropHelperSys cropHelper = new CropHelperSys(this);
    //选择弹出窗
    private DialogPopupPhoto popupDialog;

    private ImageView img_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setToolbar();

        initBase();

        img_header = (ImageView) findViewById(R.id.img_header);
        //首先设置一个默认头像
        GlideUtil.loadCircleImg(this, img_header, R.drawable.default_header);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupDialog != null) popupDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cropHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void initBase() {
        popupDialog = new DialogPopupPhoto(this);
        popupDialog.setOnCameraListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.hide();
                cropHelper.startCamera();
            }
        });
        popupDialog.setOnPhotoListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.hide();
                cropHelper.startPhoto();
            }
        });
    }

    //图片选择回调
    @Override
    public void cropResult(String path) {
        Log.e("liao",path);
        GlideUtil.loadCircleImg(this, img_header, R.drawable.default_header, path);
    }

    //button 点击
    public void onClick(View v){
        //API23以后一定进行权限检查，授予权限后才起调相机
        if (PermissionsUtil.requsetPhoto(this, findViewById(R.id.showingroup))) {
            popupDialog.show();
        }
    }
}