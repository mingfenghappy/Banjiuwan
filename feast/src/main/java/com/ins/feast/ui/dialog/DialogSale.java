package com.ins.feast.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ins.feast.R;
import com.ins.feast.entity.SaleDialogEntity;
import com.ins.feast.utils.AppHelper;
import com.ins.feast.utils.GlideUtil;


/**
 * @Function: 自定义对话框
 */
public class DialogSale extends Dialog{

    private Context context;
    private ImageView img_sale_pic;

    private SaleDialogEntity saleEntity;

    //拓展字段
    private Object object;

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public DialogSale(Context context) {
        this(context, null);
    }

    public DialogSale(Context context, SaleDialogEntity saleEntity) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.saleEntity = saleEntity;
        setLoadingDialog();
    }

    private void setLoadingDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.dialog_sale, null);// 得到加载view

        img_sale_pic = (ImageView) v.findViewById(R.id.img_sale_pic);

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
        int screenHeight = dm.heightPixels;
        /////////设置高宽
        lp.width = (int) (screenWidth * 0.80); // 宽度
        lp.height = (int) (lp.width * 1.65); // 高度
        dialogWindow.setAttributes(lp);
    }

    private void setData() {
        if (saleEntity != null) {
            GlideUtil.loadImg(context, img_sale_pic, R.drawable.default_bk_img, AppHelper.getRealImgPath(saleEntity.getPicUrl()));
        }
    }

    public void setSaleEntity(SaleDialogEntity saleEntity) {
        this.saleEntity = saleEntity;
        setData();
    }

    public SaleDialogEntity getSaleEntity() {
        return saleEntity;
    }

    public void setOnDialogClickListener(View.OnClickListener onClickListener) {
        img_sale_pic.setOnClickListener(onClickListener);
    }
}
