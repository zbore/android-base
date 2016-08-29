/**
 * Copyright (c) 2014-2016, zbore technology. All rights reserved.
*/

package com.zbore.suite.core.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zbore.suite.core.R;
import com.zbore.suite.core.util.SystemUtil;

public class BaseActivity extends Activity {

    public Dialog mDialog = null;// 等待对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void gotoBack(View view) {
        SystemUtil.goBack(this);
    }

    protected void showCustomDialog(int resId) {
        showCustomDialog(getString(resId));
    }

    protected void showCustomDialog(String message) {
        if (mDialog == null) {
            mDialog = initCustomDialog();
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(true);
        }

        TextView msgText = (TextView) mDialog.findViewById(R.id.message);
        if (message == null || message.length() == 0) {
            msgText.setText(R.string.app_name);
        } else {
            msgText.setText(message);
        }

        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dismissCustomDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public Dialog initCustomDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        return dialog;
    }
}
