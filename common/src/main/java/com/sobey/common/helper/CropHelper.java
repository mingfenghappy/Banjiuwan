package com.sobey.common.helper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.Toast;

import com.sobey.common.utils.FileUtil;
import com.sobey.common.utils.others.BitmapUtil;
import com.sobey.common.utils.others.FileUtils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/1/19.
 */
public class CropHelper {
    //    private File file;
    private String path;
    private static final int PHOTO_CAPTURE = 0xf311;// 拍照
    private static final int PHOTO_RESULT = 0xf312;// 结果
    private static final int PHOTO_CROP = 0xf313;// 裁剪
    private boolean needCrop = false;   //默认不需要裁剪
    private boolean needPress = true;   //默认需要压缩
    private CropInterface cropInterface;
    private Context context;

    public CropHelper(CropInterface cropInterface) {
        this.cropInterface = cropInterface;

        if (cropInterface instanceof Activity) {
            context = ((Activity) cropInterface);
        } else if (cropInterface instanceof Fragment) {
            context = ((Fragment) cropInterface).getActivity();
        } else if (cropInterface instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) cropInterface).getActivity();
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
                        Uri uri = Uri.fromFile(new File(path));
                        startPhotoCrop(uri,uri);
                    } else {
                        if (needPress){
                            path = compress(path,path);
                        }
                        cropInterface.cropResult(path);
                    }
                } else {
                    File file = new File(path);
                    if (file != null && file.exists()) {
                        file.delete();
                    }
                }
                break;
            case PHOTO_RESULT:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        if (needCrop) {
                            path = FileUtil.getPhotoFullPath();
                            startPhotoCrop(uri,Uri.fromFile(new File(path)));
                        }else {
                            //获取真实的图片路径，下面2种方法都可以
                            //String pathFromUri = getPathFromUri(context, uri);
                            String pathFromUri = FileUtil.getRealFilePath(context, uri);
                            if (needPress) {
                                pathFromUri = compress(pathFromUri, FileUtil.getPhotoFullPath());
                            }
                            cropInterface.cropResult(pathFromUri);
                        }
                    }
                } else {
                    //没选择，不删除
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
                }
                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    final Uri resultUri = UCrop.getOutput(data);
                    cropInterface.cropResult(path);
                } else {
                    File file = new File(path);
                    if (file != null && file.exists()) {
                        file.delete();
                    }
                }
                break;
        }
    }

    private String compress(String FromPath,String toPath){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapUtil.revitionImageSize(FromPath);
            toPath = BitmapUtil.saveBitmap(bitmap, toPath);
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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));

        if (cropInterface instanceof Activity) {
            ((Activity) cropInterface).startActivityForResult(intent, PHOTO_CAPTURE);
        } else if (cropInterface instanceof Fragment) {
            ((Fragment) cropInterface).startActivityForResult(intent, PHOTO_CAPTURE);
        } else if (cropInterface instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) cropInterface).startActivityForResult(intent, PHOTO_CAPTURE);
        }
    }

    /**
     * 调用相册并裁剪
     */
    public void startPhoto() {
        Intent intent;
        int mark;
            intent = getPhotoIntent();
            mark = PHOTO_RESULT;

        if (cropInterface instanceof Activity) {
            ((Activity) cropInterface).startActivityForResult(intent, mark);
        } else if (cropInterface instanceof Fragment) {
            ((Fragment) cropInterface).startActivityForResult(intent, mark);
        } else if (cropInterface instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) cropInterface).startActivityForResult(intent, mark);
        }
    }

    /**
     * 进行裁剪
     */
    public void startPhotoCrop(Uri sourse,Uri uri) {
        UCrop.of(sourse, uri)
                .withAspectRatio(16, 16)
                .withMaxResultSize(600, 600)
                .start(getActivity(cropInterface));
    }

    private Context getContext(CropInterface cropInterface){
        if (cropInterface instanceof Activity) {
            return (Activity)cropInterface;
        } else if (cropInterface instanceof Fragment) {
            return ((Fragment)cropInterface).getActivity();
        } else if (cropInterface instanceof android.support.v4.app.Fragment) {
            return ((android.support.v4.app.Fragment)cropInterface).getActivity();
        }else {
            return null;
        }
    }

    private Activity getActivity(CropInterface cropInterface){
        if (cropInterface instanceof Activity) {
            return (Activity)cropInterface;
        } else if (cropInterface instanceof Fragment) {
            return ((Fragment)cropInterface).getActivity();
        } else if (cropInterface instanceof android.support.v4.app.Fragment) {
            return ((android.support.v4.app.Fragment)cropInterface).getActivity();
        }else {
            return null;
        }
    }

    private Intent getPhotoIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return intent;
    }

    public interface CropInterface {
        void cropResult(String path);
    }
}