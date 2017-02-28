package com.ins.feast.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.sobey.common.helper.CropHelperSys;

/**
 * Created by liaoinstan on 2017/2/27.
 */

public class WebPickerHelper {

    public final static int FILECHOOSER_RESULTCODE = 0xf321;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 0xf322;

    private Activity activity;

    public WebPickerHelper(Activity activity) {
        this.activity = activity;
    }

    private ValueCallback<Uri> mUploadMessage;

    public void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
    }

    private ValueCallback<Uri[]> mUploadMessageForAndroid5;

    public void openFileChooserImplForAndroid(ValueCallback<Uri[]> filePathCallback) {
        mUploadMessageForAndroid5 = filePathCallback;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

        activity.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = (intent == null || resultCode != Activity.RESULT_OK) ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != Activity.RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
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
