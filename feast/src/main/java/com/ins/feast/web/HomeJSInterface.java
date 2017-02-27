package com.ins.feast.web;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.ins.feast.ui.dialog.DialogPopupPhoto;
import com.sobey.common.helper.CropHelperSys;

/**
 * author 边凌
 * date 2017/2/16 10:56
 * desc ${与js交互的类}
 */

public class HomeJSInterface implements CropHelperSys.CropInterface {
    private Context context;
    private HomeWebView homeWebView;
    private DialogPopupPhoto imagePicker;
    private CropHelperSys cropHelperSys;

    private enum OnActivityResultAction {OpenImageSelector}

    private volatile OnActivityResultAction action;

    public HomeJSInterface(Context context, HomeWebView homeWebView) {
        this.context = context;
        this.homeWebView = homeWebView;
    }

    @JavascriptInterface
    public void openImageSelector() {
        action = OnActivityResultAction.OpenImageSelector;
        if (imagePicker == null) {
            imagePickerInit();
        }
        imagePicker.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (action) {
            case OpenImageSelector:
                cropHelperSys.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void cropResult(String path) {
        homeWebView.setImageSelectResult(path);
    }

    private void imagePickerInit() {
        imagePicker = new DialogPopupPhoto(context);
        cropHelperSys = new CropHelperSys(this);
        imagePicker.setOnCameraListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.hide();
                cropHelperSys.startCamera();
            }
        });
        imagePicker.setOnPhotoListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.hide();
                cropHelperSys.startPhoto();
            }
        });
    }
}
