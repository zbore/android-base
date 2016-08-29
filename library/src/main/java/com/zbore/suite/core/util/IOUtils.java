package com.zbore.suite.core.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zbore.suite.core.constant.CoreConstant;
import com.zbore.suite.thirdlib.eventbus.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class IOUtils {

    public static void silentlyClose(Closeable... closeables) {
        for (Closeable cl : closeables) {
            try {
                if (cl != null) {
                    cl.close();
                }
            } catch (Exception e) {
                Logger.e(e.getMessage(), e);
            }
        }
    }

    public static String readToString(InputStream is) throws IOException {
        byte[] data = readToByteArray(is);
        return new String(data, CoreConstant.UTF8);
    }

    public static byte[] readToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copy(is, baos);
        return baos.toByteArray();
    }

    public static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[CoreConstant.BUFFER_SIZE];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
    }

    public static ArrayList<File> getFileList(File dir, String... fileExtensions) {
        final ArrayList<File> files = new ArrayList<File>();
        final File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile()) {
                    if (fileExtensions.length == 0) {
                        files.add(file);
                    } else {
                        String fileName = file.getName().toLowerCase();
                        for (String ext : fileExtensions) {
                            if (fileName.endsWith(ext)) {
                                files.add(file);
                                break;
                            }
                        }
                    }
                } else {
                    files.addAll(getFileList(file, fileExtensions));
                }
            }
        }
        return files;
    }

    public static void copy(File fileFrom, File fileTo) throws IOException {
        if (fileTo.exists()) {
            fileTo.delete();
        }
        FileChannel src = null;
        FileChannel dst = null;
        try {
            src = new FileInputStream(fileFrom).getChannel();
            dst = new FileOutputStream(fileTo).getChannel();
            dst.transferFrom(src, 0, src.size());
        } finally {
            silentlyClose(src, dst);
        }
    }

    public static void dumpDBToCacheDir(Context ctx, SQLiteDatabase db) {
        String dbFilePath = db.getPath();
        String dbFileName = dbFilePath.substring(dbFilePath.lastIndexOf('/', dbFilePath.length()));
        File fileTo = new File(ctx.getExternalCacheDir(), dbFileName);
        try {
            IOUtils.copy(new File(dbFilePath), fileTo);
            Logger.d("Copied DB file to '%s'.", fileTo.getAbsolutePath());
        } catch (IOException e) {
            Logger.e(e, e.getMessage());
        }
    }

}
