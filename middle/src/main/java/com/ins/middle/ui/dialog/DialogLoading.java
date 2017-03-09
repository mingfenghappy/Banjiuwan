package com.ins.middle.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ins.middle.R;


/**
 * 仿猫眼电影加载进度框
 */
public class DialogLoading extends Dialog {
    private Context context;
    private ImageView header_img;
    private AnimationDrawable animationRefresh;
    private ProgressBar footer_progress;
    private RotateAnimation mRotateUpAnim;

    //外面旋转的边框src资源
    private int rotateSrc = R.drawable.maoyan_out;
    //内部动画的src资源
    private int[] refreshAnimSrcs = new int[]{R.drawable.maoyan_in1, R.drawable.maoyan_in2};
    //边框旋转一圈用时
    private int TIME_OUT = 800;
    //内部帧动画每一帧用时
    private int TIME_IN = 150;

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
        footer_progress.startAnimation(mRotateUpAnim);
    }

    public void onStop() {
        animationRefresh.stop();
        footer_progress.clearAnimation();
    }

    private void setAnim() {
        footer_progress = (ProgressBar) findViewById(R.id.rotation_footer_progress);

        mRotateUpAnim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setInterpolator(new LinearInterpolator());
        mRotateUpAnim.setRepeatCount(Animation.INFINITE);
        mRotateUpAnim.setDuration(TIME_OUT);
        mRotateUpAnim.setFillAfter(true);

        footer_progress.setIndeterminateDrawable(ContextCompat.getDrawable(context, rotateSrc));
        footer_progress.startAnimation(mRotateUpAnim);

        /////////////
        header_img = (ImageView) findViewById(R.id.meituan_header_img);
        animationRefresh = new AnimationDrawable();
        for (int src : this.refreshAnimSrcs) {
            animationRefresh.addFrame(ContextCompat.getDrawable(context, src), TIME_IN);
            animationRefresh.setOneShot(false);
        }
        header_img.setImageDrawable(animationRefresh);
        animationRefresh.start();
    }
}
