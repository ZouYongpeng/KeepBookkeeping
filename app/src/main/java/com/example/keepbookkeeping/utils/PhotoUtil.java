package com.example.keepbookkeeping.utils;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

/**
 * @author 邹永鹏
 * @date 2019/2/24
 * @description :
 */
public class PhotoUtil {
    /**
     * 任意图片类型
     */
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int NONE = 0;
    /**
     * 拍照
     */
    public static final int PHOTOGRAPH = 1;
    /**
     * 缩放
     */
    public static final int PHOTOZOOM = 2;
    /**
     * 结果
     */
    public static final int PHOTORESOULT = 3;
    public static final int PICTURE_HEIGHT = 500;
    public static final int PICTURE_WIDTH = 500;
    public static String imageName;

    /**
     * 从系统相册中选取照片上传
     * @param activity
     */
    public static void selectPictureFromAlbum(Activity activity){
        // 调用系统的相册
        Intent intent=new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);
        //调用剪切功能
        activity.startActivityForResult(intent,PHOTOZOOM);
    }

}
