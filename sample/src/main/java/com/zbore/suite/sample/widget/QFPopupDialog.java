/**
 * com.zbore.pulllmenudown.view.QFPopupDialog
 * description:
 * version：
 * author：liujun
 * create at 2015-7-9 
 */
package com.zbore.suite.sample.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.zbore.suite.sample.R;

/**
 * @ClassName: QFPopupDialog
 * @Description: TODO 用一句话描述这个类的作用
 *
 * @author liujun
 * @date 2015-7-9
 */
public class QFPopupDialog extends Dialog {

    private Context context;
    private View menu;

    Animation animIn;
    Animation aninOut;
    
    public QFPopupDialog(Context context, int theme, View menu) {
        super(context, theme);
        this.menu = menu;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        animIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_top_group_use);
    }

    public void showDlg(ViewGroup contentView, int contentHeight) {
        if(isShowing()) {
           dismiss();
        }
        
        setSizeAndLocation(contentHeight);
        
        // contentView 子view 为0.9
        View childView = contentView.getChildAt(0);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(getWindow().getAttributes().width, (int) (getWindow().getAttributes().height * 0.9));
        childView.setLayoutParams(params);
        
        setContentView(contentView);
        show();

        if (isShowing()) {
            contentView.startAnimation(animIn);
        } else {
            contentView.setAnimation(animIn);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (aninOut != null) {
            aninOut.reset();
        }
    }

    // 设置位置和大小
    public void setSizeAndLocation(int contentHeight) {
        Window window = getWindow();
        window.setSoftInputMode(32);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        layoutParams.flags = 32;
        layoutParams.format = -3;
        window.setGravity(51);

        int menuHeight = context.getResources().getDimensionPixelSize(R.dimen.menu_height);
        int[] mLocation = new int[2];
        menu.getLocationOnScreen(mLocation);
        
        // 表示相对上边的偏移
        int windowY = menuHeight + mLocation[1];
        layoutParams.x = 0;
        layoutParams.y = windowY;

        // 魅族手机做特殊处理
//        layoutParams.height =
//                "meizu".equalsIgnoreCase(Build.BRAND) || "MX4".equals(Build.MODEL) || "M351".equals(Build.MODEL) ? displayHeight - windowY
//                        - (menuHeight + 10) : displayHeight - windowY;
        layoutParams.height = contentHeight - windowY;
        window.setAttributes(layoutParams);
    }


}
