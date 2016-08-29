package com.zbore.suite.core.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Map;

/**
 * 系统工具类
 * @author Stone 2011-02-09 
 *
 */
public class SystemUtil {
    /**
     * 跳转到一个新页面
     * @param oldAct
     * @param newActClass
     * @param closeMe
     */
    public static void gotoActivity(Activity oldAct, Class<? extends Activity> newActClass, boolean closeMe, Map<String, Serializable> parameters) {
        Intent intent = new Intent(oldAct, newActClass);
        if (parameters != null) {
            for (String s : parameters.keySet()) {
                intent.putExtra(s, parameters.get(s));
            }
        }
        oldAct.startActivity(intent);
        if (closeMe)
            oldAct.finish();
        return;
    }

    public static void gotoActivity(Activity oldAct, Class<? extends Activity> newActClass, boolean closeMe) {
        gotoActivity(oldAct, newActClass, closeMe, null);
    }

    /**
     * 跳转到一个新页面
     * @param oldAct
     * @param newActClass
     * @param closeMe
     * @param parameters
     * @param requestCode 请求代号
     */
    public static void gotoActivityForResult(Activity oldAct, Class<? extends Activity> newActClass, boolean closeMe,
            Map<String, Serializable> parameters, int requestCode) {
        Intent intent = new Intent(oldAct, newActClass);
        if (parameters != null) {
            for (String s : parameters.keySet()) {
                intent.putExtra(s, parameters.get(s));
            }
        }
        oldAct.startActivityForResult(intent, requestCode);
        if (closeMe)
            oldAct.finish();
        return;
    }

    public static void goBack(Activity act) {
        //act.onKeyDown(KeyEvent.KEYCODE_BACK, null);
        act.finish();
    }

    public static void closeMe(Activity act) {
        act.finish();
    }

    /**
     * @param context
     * @param packageName
     * @param lauchActivity
     * 微信 "com.tencent.mm" "com.tencent.mm.ui.LauncherUI"
     * QQ  "com.tencent.mobileqq" "com.tencent.mobileqq.activity.SplashActivity"
     */
    public static void gotoOtherApp(Context context, String packageName, String lauchActivity) {
        if(PackageUtil.isPackageExisted(context, packageName)) {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName(packageName, lauchActivity);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);

        } else {
            Toast.makeText(context.getApplicationContext(), "没有安装!", Toast.LENGTH_SHORT).show();
        }
    }
}
