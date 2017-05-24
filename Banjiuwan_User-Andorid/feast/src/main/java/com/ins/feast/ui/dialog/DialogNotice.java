package com.ins.feast.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ins.feast.R;
import com.sobey.common.utils.StrUtils;


/**
 * @Function: 自定义对话框
 */
public class DialogNotice extends Dialog {

    private Context context;
    private TextView text_dialog_sure;

    private String msg;
    private int iconSrc;

    public static final int TYPE_ERROR = 0xf1233;
    public static final int TYPE_WARNING = 0xf1234;

    //拓展字段
    private Object object;

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public DialogNotice(Context context) {
        this(context, "对不起！您的点餐不在服务范围内", R.mipmap.icon_notice_error);
    }

    public DialogNotice(Context context, String msg, int iconSrc) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.msg = msg;
        this.iconSrc = iconSrc;
        setLoadingDialog();
    }

    private void setLoadingDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.dialog_notice, null);// 得到加载view

        text_dialog_sure = (TextView) v.findViewById(R.id.text_dialog_notice);

        setData();

        this.setCanceledOnTouchOutside(true);
        super.setContentView(v);
    }

    @Override
    public void show() {
        super.show();
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        /////////获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        ;
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        /////////设置高宽
        lp.width = (int) (screenWidth * 0.85); // 宽度
//        lp.height = (int) (lp.width*0.65); // 高度
        dialogWindow.setAttributes(lp);
    }

    public void setTypeMsg(int type, String msg) {
        switch (type) {
            case TYPE_ERROR:
                this.iconSrc = R.mipmap.icon_notice_error;
                this.msg = msg;
                setData();
                break;
            case TYPE_WARNING:
                this.iconSrc = R.mipmap.icon_notice_warning;
                this.msg = msg;
                setData();
                break;
        }
    }

    public void setIconMsg(int iconSrc, String msg) {
        this.iconSrc = iconSrc;
        this.msg = msg;
        setData();
    }

    public void setIcon(int iconSrc) {
        this.iconSrc = iconSrc;
        setData();
    }

    public void setMsg(String msg) {
        this.msg = msg;
        setData();
    }

    private void setData() {
        text_dialog_sure.setText(msg);
        text_dialog_sure.setCompoundDrawablesWithIntrinsicBounds(0, iconSrc, 0, 0);
    }
}
