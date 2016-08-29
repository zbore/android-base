/**
 * Copyright (c) 2014-2016, zbore technology. All rights reserved.
*/

package com.zbore.suite.core.base;


import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.zbore.suite.core.util.DeviceUtil;
import com.zbore.suite.core.util.PackageUtil;
import com.zbore.suite.thirdlib.eventbus.logger.LogLevel;
import com.zbore.suite.thirdlib.eventbus.logger.Logger;

/**
 * @ClassName: BaseApplication
 * @Description: TODO 用一句话描述这个类的作用
 *
 * @author zbore 
 * @date 2014-6-3
 * @version v1.0
 */
public class BaseApplication extends Application {

    private int screenHeight;
    private int screenWidth;
    protected DisplayMetrics metrics;
    protected float density;
    
    private String versionName;
    private String versionCode;
    private String deviceId;

    public AppConfig appCore;
    
    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        appCore = AppConfig.getInstance(this);
        
        setVersionName(PackageUtil.getVersionName(this));
        setVersionCode(PackageUtil.getVersionCode(this) + "");
        setDeviceId(DeviceUtil.getDeviceId(this));
        
        // 设置日志
        Logger.init(appCore.getAppTag()).hideThreadInfo().setLogLevel(appCore.isDebug() ? LogLevel.FULL : LogLevel.NONE).setMethodCount(1)
                .setMethodOffset(0);
        
        CrashHandler.getInstance().init(this);
        initWindow();
    }
    
    public static BaseApplication getInstance(){
        return mInstance;
    }

    @SuppressWarnings("deprecation")
    public void initWindow() {
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        screenWidth = windowmanager.getDefaultDisplay().getWidth();
        screenHeight = windowmanager.getDefaultDisplay().getHeight();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowmanager.getDefaultDisplay().getMetrics(displaymetrics);
        setMetrics(displaymetrics);
        setDensity(displaymetrics.density);
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public DisplayMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(DisplayMetrics metrics) {
        this.metrics = metrics;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
