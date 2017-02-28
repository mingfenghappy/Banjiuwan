package com.ins.feast.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.JavascriptInterface;

/**
 * author 边凌
 * date 2017/2/16 10:56
 * desc ${与js交互的类}
 */

public class HomeJSInterface extends BaseJSInterface {
    private HomeWebView homeWebView;
    private Activity context;
    private final static int REQUEST_PHOTOS = 1;
    private final static int REQUEST_CAMARAS = 2;

    public HomeJSInterface(Activity context, HomeWebView homeWebView) {
        this.context = context;
        this.homeWebView = homeWebView;
    }

    @JavascriptInterface
    public void test() {

    }

    /**
     * 打开相机
     */
    @JavascriptInterface
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        context.startActivityForResult(intent, REQUEST_CAMARAS);
    }

    /**
     * 打开相册
     */
    @JavascriptInterface
    public void openPhotos() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(intent, REQUEST_PHOTOS);
    }

    public void setImageSelectedResult(int requestCode, int resultCode, Intent intent){
        if(resultCode==Activity.RESULT_OK){
            if(requestCode==REQUEST_PHOTOS){
//                Uri selectedImage = intent.getData();
//                String[] filePathColumns = {MediaStore.Images.Media.DATA};
//                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePathColumns[0]);
//                String imagePath = c.getString(columnIndex);
//                showImage(imagePath);
//                c.close();
//
            }else if(requestCode==REQUEST_CAMARAS){

            }
        }
    }
}
