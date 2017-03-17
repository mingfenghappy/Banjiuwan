package com.ins.middle.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.sobey.common.helper.CropHelperSys;
import com.sobey.common.helper.CropHelperSysV2;
import com.sobey.common.ui.dialog.DialogPopupPhoto;
import com.sobey.common.utils.PermissionsUtil;

import java.io.File;

/**
 * Created by liaoinstan on 2017/2/27.
 */

public class WebPickerHelper implements CropHelperSysV2.CropInterface {
//
//    public final static int FILECHOOSER_RESULTCODE = 0xf321;
//    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 0xf322;
//
//    private Activity activity;
//
//    public WebPickerHelper(Activity activity) {
//        this.activity = activity;
//    }
//
//    private ValueCallback<Uri> mUploadMessage;
//
//    public void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
//        mUploadMessage = uploadMsg;
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");
//        activity.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
//    }
//
//    private ValueCallback<Uri[]> mUploadMessageForAndroid5;
//
//    public void onenFileChooseImpleForAndroid(ValueCallback<Uri[]> filePathCallback) {
//        mUploadMessageForAndroid5 = filePathCallback;
//        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
//        contentSelectionIntent.setType("image/*");
//
//        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
//
//        activity.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (requestCode == FILECHOOSER_RESULTCODE) {
//            if (null == mUploadMessage)
//                return;
//            Uri result = (intent == null || resultCode != Activity.RESULT_OK) ? null : intent.getData();
//            mUploadMessage.onReceiveValue(result);
//            mUploadMessage = null;
//
//        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
//            if (null == mUploadMessageForAndroid5)
//                return;
//            Uri result = (intent == null || resultCode != Activity.RESULT_OK) ? null : intent.getData();
//            if (result != null) {
//                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
//            } else {
//                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
//            }
//            mUploadMessageForAndroid5 = null;
//        }
//    }


    private CropHelperSysV2 cropHelper;
    private Activity activity;
    private DialogPopupPhoto popup;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;

    public WebPickerHelper(final Activity activity) {
        this.activity = activity;
        this.cropHelper = new CropHelperSysV2(activity, this);
        popup = new DialogPopupPhoto(activity);
        popup.setOnCameraListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.hide();
                if (PermissionsUtil.requsetPhoto(activity, null)) {
                    cropHelper.startCamera();
                }
            }
        });
        popup.setOnPhotoListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.hide();
                cropHelper.startPhoto();
            }
        });
        popup.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                    mUploadMessage = null;
                }
                if (mUploadMessageForAndroid5 != null) {
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
                    mUploadMessageForAndroid5 = null;
                }
            }
        });
    }

    public void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        popup.show();
    }

    public void onenFileChooseImpleForAndroid(ValueCallback<Uri[]> filePathCallback) {
        mUploadMessageForAndroid5 = filePathCallback;
        popup.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        cropHelper.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void cropResult(String path) {
        Uri uri = Uri.fromFile(new File(path));
        if (mUploadMessage != null)
            mUploadMessage.onReceiveValue(uri);
        if (mUploadMessageForAndroid5 != null)
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{uri});
        mUploadMessage = null;
        mUploadMessageForAndroid5 = null;
    }

    @Override
    public void cancel() {
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(null);
            mUploadMessage = null;
        }
        if (mUploadMessageForAndroid5 != null) {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            mUploadMessageForAndroid5 = null;
        }
    }
}
/**
 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
 * if (webView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
 * //获取历史列表
 * WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
 * //判断当前历史列表是否最顶端,其实canGoBack已经判断过
 * if (mWebBackForwardList.getCurrentIndex() > 0) {
 * webView.goBack();
 * return true;
 * }
 * }
 * return super.onKeyDown(keyCode, event);
 * }
 */
//}
