/**
 * Copyright (c) 2014-2016, zbore technology. All rights reserved.
*/

package com.zbore.suite.core.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.zbore.suite.core.util.PackageUtil;

import java.io.File;

/**
 * @ClassName: AppConfig
 * @Description: TODO 用一句话描述这个类的作用
 *
 * @author zbore
 * @date 2015-8-3
 */
public class AppConfig {

    private static AppConfig instance = null;

    private Context context;

    private String appName;
    private String appTag;
    private boolean isDebug;

    public static AppConfig getInstance(Context context) {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig(context);
                }
            }
        }
        return instance;
    }

    private AppConfig() {

    }

    private AppConfig(Context context) {
        initConfig(context);
    }

    private void initConfig(Context context) {
        this.context = context;

        Bundle bundle = PackageUtil.getAppMetaData(context);
        if (bundle != null) {
            appName = bundle.getString("APP_NAME");
            appTag = bundle.getString("APP_TAG");
            isDebug = bundle.getBoolean("DEBUG", true);
        }
    }

    public String getAppName() {
        return appName;
    }

    public String getAppTag() {
        return appTag;
    }

    public boolean isDebug() {
        return isDebug;
    }

    /*
     * http://developer.android.com/intl/zh-cn/training/displaying-bitmaps/cache-bitmap.html
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
                                context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }
    */

    public String getAppSDPath() {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) /*|| !Environment.isExternalStorageRemovable()*/? Environment
                        .getExternalStorageDirectory().getPath() : context.getFilesDir().getPath();
        File file = new File(cachePath + File.separator + appTag);
        if (!file.exists())
            file.mkdir();
        return file.getAbsolutePath();
    }

    public String getAppApkFile() {
        return (new StringBuilder(String.valueOf(getAppSDPath()))).append("/").append(appTag).append("_update.apk").toString();
    }

    public String getAppCacheSDPath() {
        File file = new File(getAppSDPath(), "cache");
        if (!file.exists())
            file.mkdirs();
        return file.getAbsolutePath();
    }

    public String getAppDownloadSDPath() {
        File file = new File(getAppSDPath(), "download");
        if (!file.exists())
            file.mkdirs();
        return file.getAbsolutePath();
    }

    public String getAppImageSDPath() {
        File file = new File(getAppSDPath(), "image");
        if (!file.exists())
            file.mkdirs();
        return file.getAbsolutePath();
    }

    public String getAppTmpSDPath() {
        File file = new File(getAppSDPath(), "temp");
        if (!file.exists())
            file.mkdirs();
        return file.getAbsolutePath();
    }
}
