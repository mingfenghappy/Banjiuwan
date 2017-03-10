package com.ins.middle.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ins.middle.R;


/**
 * 仿猫眼电影加载进度框
 */
public class DialogLoading extends Dialog {
    private Context context;
    private ImageView img_out;
    private AnimationDrawable animationRefresh;
    private ImageView img_in;

    private int inSrc = R.drawable.loading_in;
    private int[] outSrcs = new int[]{R.drawable.loading_out1, R.drawable.loading_out2,R.drawable.loading_out3,R.drawable.loading_out4};
    //边框旋转一帧用时
    private int TIME_OUT = 100;

    public DialogLoading(Context context) {
        super(context, R.style.LoadingDialog);
        this.context = context;
        setLoadingDialog();
    }

    private void setLoadingDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.dialog_loading_layout, null);// 得到加载view
        ViewGroup layout = (ViewGroup) v.findViewById(R.id.dialog_view);// 加载布局

        /**
         * <item name="android:windowCloseOnTouchOutside">false</item>
         * 可以在style文件中进行配置，最低app level 11（当前15）
         */
        this.setCanceledOnTouchOutside(false);
        this.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

        super.setContentView(v);

        setAnim();
    }

    private void setAnim() {
        img_in = (ImageView) findViewById(R.id.img_loading_in);
        img_in.setImageResource(inSrc);

        /////////////
        img_out = (ImageView) findViewById(R.id.img_loading_out);
        animationRefresh = new AnimationDrawable();
        for (int src : this.outSrcs) {
            animationRefresh.addFrame(ContextCompat.getDrawable(context, src), TIME_OUT);
            animationRefresh.setOneShot(false);
        }
        img_out.setImageDrawable(animationRefresh);
        animationRefresh.start();
    }

    @Override
    public void show() {
        super.show();
        onStart();
    }

    @Override
    public void hide() {
        super.hide();
        onStop();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        onStop();
    }

    public void onStart() {
        animationRefresh.start();
    }

    public void onStop() {
        animationRefresh.stop();
    }
}
