package com.sobey.common.view.singlepopview;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.sobey.common.R;
import com.sobey.common.view.mulpopview.MyMulPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/3 0003.
 */
public class MySinglePopupWindow extends PopupWindow {

    private Context context;
    private ListView listView;
    private MySingleAdapter adapter;
    private boolean needanim = true;

    private View shadowView;

    List<String> results = new ArrayList<String>();
//    int images[] = new int[]{R.drawable.ic_category_0, R.drawable.ic_category_10, R.drawable.ic_category_30, R.drawable.ic_category_20};


    public void setResults(List<String> results) {
        adapter.getResults().clear();
        adapter.getResults().addAll(results);
        adapter.notifyDataSetChanged();
    }

    public MySinglePopupWindow(final Context context) {
        this.context = context;

        //加载布局
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(inflater.inflate(R.layout.pop_single, null));

        //获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int w = outMetrics.widthPixels;
        int h = outMetrics.heightPixels;

        // 设置SelectPicPopupWindow的View
        this.setContentView(getContentView());
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 3);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimationPreview);

        initBase();
    }

    private void initBase() {
        listView = (ListView) getContentView().findViewById(R.id.list_pop_single);

        adapter = new MySingleAdapter(context, results);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (onPopSingleClickListenner != null)
                    onPopSingleClickListenner.OnPopClick(adapter.getResults().get(position));
            }
        });
    }

    /**
     * 显示popupWindow
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            int x = (parent.getWidth() - this.getWidth()) / 2;
            this.showAsDropDown(parent, x, 0);
            if (needanim) turnBackgroundDark();
        } else {
            this.dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (needanim) resetBackground();
    }

    //////////对外接口

    public void setNeedanim(boolean needanim) {
        this.needanim = needanim;
    }

    public void setShadowView(View shadowView) {
        this.shadowView = shadowView;
    }

    private OnPopSingleClickListenner onPopSingleClickListenner;

    public void setOnPopSingleClickListenner(OnPopSingleClickListenner onPopSingleClickListenner) {
        this.onPopSingleClickListenner = onPopSingleClickListenner;
    }

    public interface OnPopSingleClickListenner {
        void OnPopClick(String value);
    }

    //////////////////////////
    ////////背景变暗
    //////////////////////////
    private void turnBackgroundDark() {
        if (shadowView != null) {
            turnDarkByView();
        } else {
            turnDarkByAlpha();
        }
    }

    private void resetBackground() {
        if (shadowView != null) {
            turnLightByView();
        } else {
            turnLightByAlpha();
        }
    }


    ////////////////////////
    //////背景变暗动画
    ////////////////////////


    private void turnDarkByAlpha() {
        final Activity activity = (Activity) context;
        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0.7f);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = (Float) animation.getAnimatedValue();
                activity.getWindow().setAttributes(params);
            }
        });
    }

    private void turnLightByAlpha() {
        final Activity activity = (Activity) context;
        ValueAnimator animator = ValueAnimator.ofFloat(0.7f, 1f);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = (Float) animation.getAnimatedValue();
                activity.getWindow().setAttributes(params);
            }
        });
    }

    private void turnDarkByView() {
        if (shadowView != null) {
            shadowView.setVisibility(View.VISIBLE);
            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.setDuration(300).start();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float alpha = (Float) animation.getAnimatedValue();
                    shadowView.setAlpha(alpha);
                }
            });
        } else {
            final Activity activity = (Activity) context;
            ValueAnimator animator = ValueAnimator.ofFloat(1f, 0.7f);
            animator.setDuration(300).start();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                    params.alpha = (Float) animation.getAnimatedValue();
                    activity.getWindow().setAttributes(params);
                }
            });
        }
    }

    private void turnLightByView() {
        if (shadowView != null) {
            ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f);
            animator.setDuration(300).start();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float alpha = (Float) animation.getAnimatedValue();
                    shadowView.setAlpha(alpha);
                }
            });
        } else {
            final Activity activity = (Activity) context;
            ValueAnimator animator = ValueAnimator.ofFloat(0.7f, 1f);
            animator.setDuration(300).start();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                    params.alpha = (Float) animation.getAnimatedValue();
                    activity.getWindow().setAttributes(params);
                }
            });
        }
    }
}
