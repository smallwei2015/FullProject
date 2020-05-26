package com.blue.rchina.utils;

/**
 * Created by smallwei on 2020/4/16.
 */

import android.content.Context;
import android.content.Intent;

import java.io.File;

public class OpenFileUtils {

    /**
     * 声明各种类型文件的dataType
     **/
    public static final String DATA_TYPE_APK = "application/vnd.android.package-archive";
    public static final String DATA_TYPE_VIDEO = "video/*";
    public static final String DATA_TYPE_AUDIO = "audio/*";
    public static final String DATA_TYPE_HTML = "text/html";
    public static final String DATA_TYPE_IMAGE = "image/*";
    public static final String DATA_TYPE_PPT = "application/vnd.ms-powerpoint";
    public static final String DATA_TYPE_EXCEL = "application/vnd.ms-excel";
    public static final String DATA_TYPE_WORD = "application/msword";
    public static final String DATA_TYPE_CHM = "application/x-chm";
    public static final String DATA_TYPE_TXT = "text/plain";
    public static final String DATA_TYPE_PDF = "application/pdf";
    /**
     * 未指定明确的文件类型，不能使用精确类型的工具打开，需要用户选择
     */
    public static final String DATA_TYPE_ALL = "*/*";


    /**
     * 打开文件
     * @param mContext
     * @param file
     */
    public static void openFile(Context mContext, File file) {
        if (!file.exists()) {
            return;
        }
        Intent intent;
        // 取得文件扩展名
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
        // 依扩展名的类型决定MimeType
        switch (end) {
            case "3gp":
            case "mp4":
            case "mov":
                openVideoFileIntent(mContext, file);
                break;
            case "m4a":
            case "mp3":
            case "mid":
            case "xmf":
            case "ogg":
            case "wav":
                openAudioFileIntent(mContext, file);
                break;
            case "doc":
            case "docx":
                commonOpenFileWithType(mContext, file, DATA_TYPE_WORD);
                break;
            case "xls":
            case "xlsx":
                commonOpenFileWithType(mContext, file, DATA_TYPE_EXCEL);
                break;
            case "jpg":
            case "gif":
            case "png":
            case "jpeg":
            case "bmp":

                commonOpenFileWithType(mContext, file, DATA_TYPE_IMAGE);
                break;
            case "txt":
                commonOpenFileWithType(mContext, file, DATA_TYPE_TXT);
                break;
            case "htm":
            case "html":
                commonOpenFileWithType(mContext, file, DATA_TYPE_HTML);
                break;
            case "apk":

                /*if (mContext.getPackageManager().canRequestPackageInstalls()) {
                    commonOpenFileWithType(mContext, file, DATA_TYPE_APK);
                }else {
                    intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    mContext.startActivity(intent);
                }*/
                break;
            case "ppt":
            case "pptx":
                commonOpenFileWithType(mContext, file, DATA_TYPE_PPT);
                break;
            case "pdf":
                commonOpenFileWithType(mContext, file, DATA_TYPE_PDF);
                break;
            case "chm":
                commonOpenFileWithType(mContext, file, DATA_TYPE_CHM);
                break;
            default:
                commonOpenFileWithType(mContext, file, DATA_TYPE_ALL);
                break;
        }
    }



    public static String getFileType(File file) {
        if (!file.exists()) {
            return null;
        }
        Intent intent;
        // 取得文件扩展名
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
        // 依扩展名的类型决定MimeType
        switch (end) {
            case "3gp":
            case "mp4":
            case "mov":
                return DATA_TYPE_VIDEO;
            case "m4a":
            case "mp3":
            case "mid":
            case "xmf":
            case "ogg":
            case "wav":
                return DATA_TYPE_AUDIO;
            case "doc":
            case "docx":
                return DATA_TYPE_WORD;
            case "xls":
            case "xlsx":
                return DATA_TYPE_EXCEL;
            case "jpg":
            case "gif":
            case "png":
            case "jpeg":
            case "bmp":
                return DATA_TYPE_IMAGE;
            case "txt":
                return DATA_TYPE_TXT;
            case "htm":
            case "html":
                return DATA_TYPE_HTML;
            case "apk":
                return DATA_TYPE_APK;
            case "ppt":
            case "pptx":
                return DATA_TYPE_PPT;
            case "pdf":
                return DATA_TYPE_PDF;
            case "chm":
                return DATA_TYPE_CHM;
            default:
                return DATA_TYPE_ALL;
        }
    }

    /**
     * Android传入type打开文件
     * @param mContext
     * @param file
     * @param type
     */
    public static void commonOpenFileWithType(Context mContext, File file, String type) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.addCategory(Intent.CATEGORY_DEFAULT);
        FileProviderUtils.setIntentDataAndType(mContext, intent, type, file, true);
        mContext.startActivity(intent);
    }

    /**
     * Android打开Video文件
     * @param mContext
     * @param file
     */
    public static void openVideoFileIntent(Context mContext, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        FileProviderUtils.setIntentDataAndType(mContext, intent, DATA_TYPE_VIDEO, file, false);
        mContext.startActivity(intent);
    }

    /**
     * Android打开Audio文件
     * @param mContext
     * @param file
     */
    private static void openAudioFileIntent(Context mContext, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        FileProviderUtils.setIntentDataAndType(mContext, intent, DATA_TYPE_AUDIO, file, false);
        mContext.startActivity(intent);
    }
}
