/**
 * Copyright (c) 2014-2016, zbore technology. All rights reserved.
*/

package com.zbore.suite.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.provider.Settings.Secure;

/**
 * @ClassName PackageUtil
 * @desc 用一句话描述这个类的作用
 *
 * @author zbore
 * @date 2016/8/29 0029
*/
public class PackageUtil {

    public static ActivityInfo getActivityInfo(Activity activity) {
        ActivityInfo activityinfo = null;
        try {
            activityinfo = activity.getPackageManager().getActivityInfo(activity.getComponentName(), 0);
        } catch (NameNotFoundException e) {

        }
        return activityinfo;
    }

    public static PackageInfo getApkInfo(Context context, String archiveFilePath) {
        return context.getPackageManager().getPackageArchiveInfo(archiveFilePath, PackageManager.GET_META_DATA);
    }

    public static Bundle getAppMetaData(Context context) {
        Bundle bundle;
        try {
            bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            bundle = null;
        }
        return bundle;
    }

    /**
     * 判断应用程序是否安装
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isPackageExisted(Context context, String packageName) {
        boolean result = false;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            if (info != null)
                result = true;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static boolean isInstalled(Context ctx, String pkgName) {
        try {
            ctx.getPackageManager().getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /** 
     * 返回当前程序版本
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
        }
        return versionName;
    }
    
    public static int getVersionCode(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info != null ? info.versionCode : Integer.MAX_VALUE;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return Integer.MAX_VALUE;
    }
    
    //
    public static String getDeviceId(Context ctx) {
        return Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
    }

    public static String getSignature(Context ctx, String pkgName) throws NameNotFoundException {
        PackageInfo pi = ctx.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
        String signature = pi.signatures[0].toCharsString();
        return signature;
    }

    public static boolean doSignaturesMatch(Context ctx, String pkg1, String pkg2) {
        boolean match = ctx.getPackageManager().checkSignatures(pkg1, pkg2) == PackageManager.SIGNATURE_MATCH;
        return match;
    }

}
