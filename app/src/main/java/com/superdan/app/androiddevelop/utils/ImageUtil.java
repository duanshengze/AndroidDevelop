package com.superdan.app.androiddevelop.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * Created by Administrator on 2016/4/5.
 */
public class ImageUtil {
    public  static Bitmap decodeScaleImage(String path,int targetWidth,int targetHeight){
        BitmapFactory.Options bitmapOptions = getBitmapOptions(path);
        bitmapOptions.inSampleSize = calculateInSampleSize(bitmapOptions, targetWidth,
                targetHeight);
        bitmapOptions.inJustDecodeBounds = false;
        Bitmap noRotatingBitmap = BitmapFactory.decodeFile(path, bitmapOptions);
        int degree = readPictureDegree(path);
        Bitmap rotatingBitmap;
        if (noRotatingBitmap != null && degree != 0) {
            rotatingBitmap = rotatingImageView(degree, noRotatingBitmap);
            noRotatingBitmap.recycle();
            return rotatingBitmap;
        } else {
            return noRotatingBitmap;
        }

    }

    public static BitmapFactory.Options getBitmapOptions(String pathName){
        BitmapFactory.Options opts=new BitmapFactory.Options();
        opts.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(pathName,opts);
        return  opts;

    }
    /**
     * 旋转ImageView
     */
    public static Bitmap rotatingImageView(int degree, Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) degree);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }
    public static int readPictureDegree(String filename) {
        short degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filename);
            int anInt = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            switch (anInt) {
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                case ExifInterface.ORIENTATION_TRANSPOSE:
                case ExifInterface.ORIENTATION_TRANSVERSE:
                default:
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degree;
    }

    /**
     * 计算样本大小
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int targetWidth, int targetHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int scale = 1;
        if (height > targetHeight || width > targetWidth) {
            int heightScale = Math.round((float) height / (float) targetHeight);
            int widthScale = Math.round((float) width / (float) targetWidth);
            scale = heightScale > widthScale ? heightScale : widthScale;
        }
        return scale;
    }

}
