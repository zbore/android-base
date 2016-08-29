package com.zbore.suite.core.base;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.zbore.suite.thirdlib.eventbus.logger.Logger;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 捕捉App全局异常,并由用户决定是否发送到服务器
 */
public class CrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = CrashHandler.class.getSimpleName();
    private static CrashHandler instance;
    private Context mContext;
    private UncaughtExceptionHandler mDefaultHandler;

    /** 使用Properties来保存设备的信息和错误堆栈信息 */
    private Properties mCrashInfo = new Properties();
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    /** 错误报告文件的扩展名 */
    public static final String CRASH_REPORTER_EXTENSION = ".crash";
    /** 错误报告文件名中的日期格式 */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");

    private CrashHandler() {}

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }

        return instance;
    }

    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else { // 如果自己处理了异常，则不会弹出错误对话框，则需要手动退出app
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }

            exitCurrentApp();
        }
    }

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }

        final String message = ex.getMessage();

        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "程序出现异常,3秒后即将关闭该页面,请联系我们,我们会尽快处理,给你带来的不便请原谅!", Toast.LENGTH_LONG).show();
                Logger.e("error: " + message);

                collectDeviceInfo(mContext);
                saveCrashInfoToFile(ex);
                Looper.loop();
            }

        }.start();

        return true;
    }

    /**
     * 强制关闭程序<br>
     * FIXME 并不能退出所有Activity,目前尚未找到比较优雅的做法
     */
    private void exitCurrentApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private void collectDeviceInfo(Context ctx) {
        mCrashInfo.put(VERSION_NAME, ((BaseApplication)ctx).getVersionName());
        mCrashInfo.put(VERSION_CODE, ((BaseApplication)ctx).getVersionCode());
        
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String fieldStr = "";
                try {
                    fieldStr = field.get(null).toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCrashInfo.put(field.getName(), fieldStr);
            } catch (Exception e) {
                Log.e(TAG, "Error while collecting device info", e);
            }
        }
    }

    private void saveCrashInfoToFile(Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);

        printWriter.write("\n=========printStackTrace()==========\n");
        ex.printStackTrace(printWriter);

        printWriter.write("\n\n=========getCause()==========\n");
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        String stackTrace = info.toString();
        printWriter.close();

        try {
            String fileName = dateFormat.format(new Date(System.currentTimeMillis())) + CRASH_REPORTER_EXTENSION;
            // 保存文件
            FileOutputStream trace = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            mCrashInfo.store(trace, "");
            trace.write(stackTrace.getBytes());
            trace.flush();
            trace.close();
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing report file", e);
        }
    }
}
