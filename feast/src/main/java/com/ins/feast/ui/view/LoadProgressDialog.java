package com.ins.feast.ui.view;

import android.app.Dialog;
import android.content.Context;

import com.ins.feast.R;

/**
 * author 边凌
 * date 2017/2/28 15:53
 * desc ${进度条弹窗}
 */

public class LoadProgressDialog extends Dialog {
    public LoadProgressDialog(Context context) {
        super(context);
        setContentView(R.layout.load_progress);
        setCanceledOnTouchOutside(false);
    }
}
