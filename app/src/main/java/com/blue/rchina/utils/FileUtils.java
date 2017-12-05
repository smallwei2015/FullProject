package com.blue.rchina.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import cn.finalteam.toolsfinal.BitmapUtils;

public class FileUtils {

    public static final String CACHEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + "融城中国";
    //错误日志路径
    public static final String LOGPATH = CACHEPATH + File.separator + "log_err.txt";

    // 图片存放路径
    public static final String IMAGEPATH = CACHEPATH + File.separator + "images";

    //图片缓存路径
    public static final String IMG_CACHE = CACHEPATH + File.separator + "imgcache";

    //用户头像存放路径
    public static final String USERICON = CACHEPATH + File.separator + "user_icon.jpg";

    public static final String SHARE_APP=CACHEPATH+File.separator+"qrcode.png";

    public static final String APP_LOGO=CACHEPATH+File.separator+"logo.png";


    /**
     * 将字符串保存到文件中
     *
     * @param content
     * @param fileName
     * @return
     */
    public static boolean string2File(String content, String fileName) {
        File file = new File(fileName);
        if (file.exists())
            file.delete();
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }


    /**
     * 复制文件
     *
     * @param fromPath
     * @param toPath
     * @return
     */
    public static boolean copyFile(String fromPath, String toPath) {
        File fromFile = new File(fromPath);
        File toFile = new File(toPath);
        return copyFile(fromFile, toFile);
    }

    /**
     * 复制文件
     *
     * @param fromFile
     * @param toFile
     * @return
     */
    public static boolean copyFile(File fromFile, File toFile) {
        try {
            if (toFile.exists())
                toFile.delete();
            FileInputStream fis = new FileInputStream(fromFile);
            FileOutputStream fos = new FileOutputStream(toFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int a;
            while ((a = bis.read()) != -1) {
                bos.write(a);
            }
            bis.close();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 如果下面还有文件
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


    public static String getCacheSize(File file) throws Exception {
        return getFormatSize(getFolderSize(file));
    }


    public static void getAppDetailSettingIntent(Context context){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if(Build.VERSION.SDK_INT <= 8){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }


    public static void creatImgFromAsset(Context context,String name) {

        File file1 = new File(FileUtils.CACHEPATH);

        if (!file1.exists()) {
            file1.mkdirs();
        }
        InputStream is = null;
        try {
            File file = new File(FileUtils.CACHEPATH + File.separator + name);
            if (!file.exists()) {
                file.createNewFile();
            }
            is = context.getAssets().open(name);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            BitmapUtils.saveBitmap(bitmap, file);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
