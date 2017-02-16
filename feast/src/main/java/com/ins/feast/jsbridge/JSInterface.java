package com.ins.feast.jsbridge;

import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * author 边凌
 * date 2017/2/16 10:56
 * desc ${与js交互的类}
 */

public class JSInterface {
    private Context context;

    public JSInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void test() {

    }
}
