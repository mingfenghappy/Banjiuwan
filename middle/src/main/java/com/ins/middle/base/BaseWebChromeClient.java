package com.ins.middle.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;


/**
 * author 边凌
 * date 2017/2/27 17:58
 * desc ${处理了Html 打开本机文件和相机的{@link android.webkit.WebChromeClient}基类}
 * <p>
 * 注意，该类的实例必须在Activity的{@link Activity#onCreate(Bundle)}之后创建
 */

public class BaseWebChromeClient extends WebPickerChromeClient {
    public BaseWebChromeClient(Activity activity) {
        super(new WebPickerHelper(activity));
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
    }

    /**
     * 若需要对h5中的file标签处理，则在{@link Activity#onActivityResult(int, int, Intent)}中必须调用该方法
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        webPickerHelper.onActivityResult(requestCode, resultCode, intent);
    }
}
