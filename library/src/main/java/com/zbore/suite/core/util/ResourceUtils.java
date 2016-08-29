package com.zbore.suite.core.util;

import android.content.Context;
import android.util.TypedValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public final class ResourceUtils {

    public static int dpToPx(Context ctx, int val) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val, ctx.getResources().getDisplayMetrics());
    }

    public static String valueForKey(Context ctx, int keysArrId, int valuesArrId, String key) {
        String[] keysArr = ctx.getResources().getStringArray(keysArrId);
        String[] valuesArr = ctx.getResources().getStringArray(valuesArrId);
        int idx = Arrays.asList(keysArr).indexOf(key);
        return (idx != -1) ? valuesArr[idx] : null;
    }

    public static String readRawResource(Context ctx, int resId) throws IllegalArgumentException {
        InputStream is = null;
        try {
            is = ctx.getResources().openRawResource(resId);
            return IOUtils.readToString(is);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } finally {
            IOUtils.silentlyClose(is);
        }
    }

    public static int getLayoutId(Context ctx, String resourceName) {
        return getIdentifier(ctx, "layout", resourceName);
    }

    public static int getDrawableId(Context ctx, String resourceName) {
        return getIdentifier(ctx, "drawable", resourceName);
    }

    public static int getColorId(Context ctx, String resourceName) {
        return getIdentifier(ctx, "color", resourceName);
    }

    public static int getStyleId(Context ctx, String resourceName) {
        return getIdentifier(ctx, "style", resourceName);
    }

    public static int getStringId(Context ctx, String stringName) {
        return getIdentifier(ctx, "string", stringName);
    }

    public static int getId(Context ctx, String resourceName) {
        return getIdentifier(ctx, "id", resourceName);
    }

    private static int getIdentifier(Context ctx, String type, String name) {
        return ctx.getResources().getIdentifier(name, type, ctx.getPackageName());
    }

}
