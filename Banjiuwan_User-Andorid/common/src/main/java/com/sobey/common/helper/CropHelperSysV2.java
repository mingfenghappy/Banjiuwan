package com.sobey.common.helper;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.sobey.common.utils.FileUtil;
import com.sobey.common.utils.StrUtils;
import com.sobey.common.utils.UriUtil;
import com.sobey.common.utils.others.BitmapUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/1/19.
 */
public class CropHelperSysV2 {
    //    private File file;
    private String path;
    private static final int PHOTO_CAPTURE = 0xf311;// 拍照
    private static final int PHOTO_RESULT = 0xf312;// 结果
    private static final int PHOTO_CROP = 0xf313;// 裁剪
    private boolean needCrop = false;   //默认不需要裁剪
    private boolean needPress = true;   //默认需要压缩
    private CropInterface cropInterface;
    private Object activityOrfragment;
    private Context context;

    public CropHelperSysV2(Object activityOrfragment, CropInterface cropInterface) {
        this.cropInterface = cropInterface;
        this.activityOrfragment = activityOrfragment;

        if (activityOrfragment instanceof Activity) {
            context = ((Activity) activityOrfragment);
        } else if (activityOrfragment instanceof Fragment) {
            context = ((Fragment) activityOrfragment).getActivity();
        } else if (activityOrfragment instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) activityOrfragment).getActivity();
        }
    }

    public void setNeedCrop(boolean needCrop) {
        this.needCrop = needCrop;
    }

    public void setNeedPress(boolean needPress) {
        this.needPress = needPress;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    if (needCrop) {
                        startPhotoCrop(Uri.fromFile(new File(path)));
                    } else {
                        if (needPress) {
                            path = compress(path, path);
                        }
                        cropInterface.cropResult(path);
                    }
                } else {
                    File file = new File(path);
                    if (file != null && file.exists()) {
                        file.delete();
                    }
                    cropInterface.cancel();
                }
                break;
            case PHOTO_RESULT:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        //获取真实的图片路径，下面2种方法都可以
                        String pathFromUri = FileUtil.PathUtil.getPath(context, uri);
//                        String pathFromUri = FileUtil.getRealFilePath(context, uri);
                        if (needPress && !StrUtils.isEmpty(pathFromUri)) {
                            pathFromUri = compress(pathFromUri, FileUtil.getPhotoFullPath());
                        }
                        cropInterface.cropResult(pathFromUri);
                    }
                } else {
                    //没选择，不删除
                    cropInterface.cancel();
                }
                break;
            case PHOTO_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    cropInterface.cropResult(path);
                } else {
                    File file = new File(path);
                    if (file != null && file.exists()) {
                        file.delete();
                    }
                    cropInterface.cancel();
                }
                break;
        }
    }

    private String compress(String FromPath, String toPath) {
        Bitmap bitmap = null;
        try {
            int degree = BitmapUtil.getBitmapDegree(FromPath);          //获取旋转角度
            bitmap = BitmapUtil.revitionImageSize(FromPath);            //压缩并获取压缩后的位图
            bitmap = BitmapUtil.rotateBitmap(degree, bitmap);           //根据旋转角度进行旋转
            toPath = BitmapUtil.saveBitmap(bitmap, toPath);             //保存图片到指定路径
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toPath;
    }


    /**
     * 调用摄像头拍照
     */
    public void startCamera() {
        path = FileUtil.getPhotoFullPath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, UriUtil.getUriFromFile(context, path));
//        if (android.os.Build.VERSION.SDK_INT < 24) {
//            //Android 7.0以下，直接获取启调
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
//        } else {
//            //适配到android 7.0
//            ContentValues contentValues = new ContentValues(1);
//            contentValues.put(MediaStore.Images.Media.DATA, new File(path).getAbsolutePath());
//            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        }
        if (activityOrfragment instanceof Activity) {
            ((Activity) activityOrfragment).startActivityForResult(intent, PHOTO_CAPTURE);
        } else if (activityOrfragment instanceof Fragment) {
            ((Fragment) activityOrfragment).startActivityForResult(intent, PHOTO_CAPTURE);
        } else if (activityOrfragment instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) activityOrfragment).startActivityForResult(intent, PHOTO_CAPTURE);
        }
    }

    /**
     * 调用相册并裁剪
     */
    public void startPhoto() {
        Intent intent;
        int mark;
        if (needCrop) {
            path = FileUtil.getPhotoFullPath();
            intent = getPhotoCropIntent();
            mark = PHOTO_CROP;
        } else {
            intent = getPhotoIntent();
            mark = PHOTO_RESULT;
        }

        if (activityOrfragment instanceof Activity) {
            ((Activity) activityOrfragment).startActivityForResult(intent, mark);
        } else if (activityOrfragment instanceof Fragment) {
            ((Fragment) activityOrfragment).startActivityForResult(intent, mark);
        } else if (activityOrfragment instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) activityOrfragment).startActivityForResult(intent, mark);
        }
    }

    /**
     * 进行裁剪
     */
    public void startPhotoCrop(Uri uri) {
        Intent intent = getCropIntent(uri);

        if (activityOrfragment instanceof Activity) {
            ((Activity) activityOrfragment).startActivityForResult(intent, PHOTO_CROP);
        } else if (activityOrfragment instanceof Fragment) {
            ((Fragment) activityOrfragment).startActivityForResult(intent, PHOTO_CROP);
        } else if (activityOrfragment instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) activityOrfragment).startActivityForResult(intent, PHOTO_CROP);
        }
    }

    private Intent getCropIntent(Uri uri) {
        Intent intent;
        intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //裁剪框比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //图片输出大小
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);

        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        return intent;
    }

    private Intent getPhotoIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return intent;
    }

    private Intent getPhotoCropIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //裁剪框比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //图片输出大小
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);

        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        return intent;
    }

    /**
     * 根据URI获取位图
     *
     * @param uri
     * @return 对应的位图
     */
    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            InputStream is = null;
            if (activityOrfragment instanceof Activity) {
                is = ((Activity) cropInterface).getContentResolver().openInputStream(uri);
            } else if (activityOrfragment instanceof Fragment) {
                is = ((Fragment) activityOrfragment).getActivity().getContentResolver().openInputStream(uri);
            } else if (activityOrfragment instanceof android.support.v4.app.Fragment) {
                is = ((android.support.v4.app.Fragment) activityOrfragment).getActivity().getContentResolver().openInputStream(uri);
            }
//            bitmap = BitmapFactory.decodeStream(is);
            bitmap = BitmapFactory.decodeFile(uri.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private Bitmap decodeFileAsBitmap(File file) {
        return BitmapFactory.decodeFile(file.getPath());
    }

    public interface CropInterface {
        void cropResult(String path);

        void cancel();
    }
}