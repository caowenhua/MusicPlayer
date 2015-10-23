package org.yekeqi.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by yekeqi on 2015/9/29.
 */
public class HelpUtil {
    public static String getSize(int size){
        double length = (double)size;
        if(length > 1024 * 1024){
            return getDecimalFormat(2, length / 1024 / 1024) + "Mb";
        }
        else if(length > 1024){
            return getDecimalFormat(2, length / 1024)  + "Kb";
        }
        else{
            return getDecimalFormat(2, length) + "b";
        }
    }

    // 取小数点后 X 位
    public static double getDecimalFormat(int X, double a) {
        String num = "#.0";
        if (X == 0) {
            num = "#";
        } else if (X == 1) {
            num = "#.0";
        } else if (X == 2) {
            num = "#.00";
        } else if (X == 3) {
            num = "#.000";
        } else {
            num = "#.0";
        }
        DecimalFormat df = new DecimalFormat(num);

        if (a > 0) {
            if (df.format(a) == null) {
                a = Double.valueOf(0);
            } else {
                a = Double.valueOf(df.format(a));
            }
        } else {
            a = Double.valueOf(0);
        }
        return a;
    }

    public static String getDownloadPath(){
        return Environment.getExternalStorageDirectory().getPath() + "/ymusic/download/";
    }

    public static boolean isDownloadDirExist(){
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/ymusic/download/");
        return file.exists();
    }

    public static String setDownloadDir(Context context) {
        String filePath;
        if (isSDCardExist()) {
            String dir = Environment.getExternalStorageDirectory().getPath()
                    + "/ymusic";
            File dirFile = new File(dir);
            if (!dirFile.exists() && !dirFile.mkdir()) {
            }

            String imgDir = Environment.getExternalStorageDirectory().getPath()
                    + "/ymusic/download";
            File imgFile = new File(imgDir);
            if (!imgFile.exists() && !imgFile.mkdir()) {
            }

            filePath = Environment.getExternalStorageDirectory().getPath()
                    + "/ymusic/download/";
        } else {
            String dir = context.getCacheDir().getAbsolutePath() + "/ymusic/download";
            File imgFile = new File(dir);
            if (!imgFile.exists() && !imgFile.mkdir()) {
            }
            filePath = context.getCacheDir().getAbsolutePath() + "/ymusic/download/";
        }
        File file = new File(filePath);
        if (!file.exists()) {
            boolean b = file.mkdirs();
        }
        return filePath;
    }

    /**
     * sdcard 是否存在
     * @return
     */
    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
}
