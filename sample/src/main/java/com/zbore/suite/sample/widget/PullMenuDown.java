/**
 * com.zbore.example.ui.view.PullMenuDown
 * description:
 * version：
 * author：liujun
 * create at 2015-7-1 
 */
package com.zbore.suite.sample.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbore.suite.sample.R;
import com.zbore.suite.sample.callback.ViewBaseAction;
import com.zbore.suite.sample.widget.ArrowTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PullMenuDown
 * @Description:  菜单控件头部
 *
 * @author liujun
 * @date 2015-7-1
 */
public class PullMenuDown extends LinearLayout implements OnDismissListener {

    private Context mContext;


    /**
     * 菜单初始化文字
     */
    private List<String> mTextArray = new ArrayList<String>();

    /**
     * 菜单列表
     */
    private List<ArrowTextView> memuArray = new ArrayList<ArrowTextView>();

    /**
     * 菜单对应布局列表
     */
    private List<RelativeLayout> mViewArray = new ArrayList<RelativeLayout>();

    /**
     * 当前选中的菜单
     */
    private ArrowTextView selectedView;

    private int selectPosition = -1;

    /**
     * 弹出window;
     */
    private PopupWindow popupWindow;

    private int displayWidth;
    private int displayHeight;

    private LinearLayout contentView;

    public PullMenuDown(Context context) {
        super(context);
        init(context);
    }

    public PullMenuDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        displayWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        displayHeight = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        setOrientation(LinearLayout.VERTICAL);

        contentView = new LinearLayout(context);
        contentView.setOrientation(LinearLayout.HORIZONTAL);
        addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));

        View bottomLine = new View(context);
        bottomLine.setBackgroundColor(context.getResources().getColor(R.color.color_E8));
        addView(bottomLine,
                new LayoutParams(LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(
                        R.dimen.divide_with)));
    }

    /**
     * 根据选择的位置设置tabitem显示的值
     */
    public void setTitle(String value, int position) {
        if (position < memuArray.size()) {
            memuArray.get(position).setArrowText(value);
        }
    }

    /**
     * 根据选择的位置获取tabitem显示的值
     */
    public String getTitle(int position) {
        if (position < memuArray.size() && memuArray.get(position).getArrowText() != null) {
            return memuArray.get(position).getArrowText().toString();
        }
        return "";
    }

    public void setValue(List<String> textArray, List<View> viewArray, MenuGenListener listener) {
        if (mContext == null || textArray == null || viewArray == null || textArray.size() != viewArray.size() || listener == null) {
            return;
        }

        mTextArray = textArray;

        for (int i = 0; i < viewArray.size(); i++) {
            final RelativeLayout relLayout = new RelativeLayout(mContext);
            int maxHeight = (int) (displayHeight * 0.6);
            RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, maxHeight);
            relLayout.addView(viewArray.get(i), parms);

            // 弹出View 点击事件
            relLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPressBack();
                }
            });

            // TODO 设置弹出view 背景色
            relLayout.setBackgroundColor(mContext.getResources().getColor(R.color.popup_main_background));
            mViewArray.add(relLayout);

            // 生成菜单
            ArrowTextView menu = listener.genMenu(i, mTextArray.get(i));
            if (menu == null) {
                continue;
            }

            menu.setTag(i);

            contentView.addView(menu);
            memuArray.add(menu);

            // 分割线
            View line = new TextView(mContext);
            line.setBackgroundColor(mContext.getResources().getColor(R.color.color_E8));
            if (i < viewArray.size() - 1) {
                LayoutParams lp =
                        new LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.divide_with),
                                LayoutParams.MATCH_PARENT);
                contentView.addView(line, lp);
            }

            menu.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (selectedView != null && selectedView != v) {
                        // 清除选中状态
                        selectedView.setSelected(false);
                    }

                    // 记录选中的memu
                    selectedView = (ArrowTextView) v;
                    selectPosition = (Integer) selectedView.getTag();
                    selectedView.setSelected(!selectedView.isSelected());

                    // 弹出View
                    startAnimation();

                    if (mOnButtonClickListener != null && selectedView.isSelected()) {
                        mOnButtonClickListener.onClick(selectPosition);
                    }
                }
            });
        }
    }

    private void startAnimation() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(mViewArray.get(selectPosition), displayWidth, displayHeight);
            popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);

            // 设置为false, 弹出view not focus
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(true);
        }

        if (selectedView.isSelected()) {
            if (!popupWindow.isShowing()) {
                showPopup(selectPosition);
            } else {
                popupWindow.setOnDismissListener(this);
                popupWindow.dismiss();
                hideView();
            }
        } else {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                hideView();
            }
        }
    }

    private void showPopup(int position) {
        View tView = mViewArray.get(selectPosition).getChildAt(0);
        if (tView instanceof ViewBaseAction) {
            ViewBaseAction f = (ViewBaseAction) tView;
            f.show();
        }
        if (popupWindow.getContentView() != mViewArray.get(position)) {
            popupWindow.setContentView(mViewArray.get(position));
        }
        popupWindow.showAsDropDown(this, 0, 0);
    }

    /**
     * 如果菜单成展开状态，则让菜单收回去
     */
    public boolean onPressBack() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            hideView();
            if (selectedView != null) {
                selectedView.setSelected(false);
            }
            return true;
        } else {
            return false;
        }

    }

    private void hideView() {
        View tView = mViewArray.get(selectPosition).getChildAt(0);
        if (tView instanceof ViewBaseAction) {
            ViewBaseAction f = (ViewBaseAction) tView;
            f.hide();
        }
    }

    @Override
    public void onDismiss() {
        showPopup(selectPosition);
        popupWindow.setOnDismissListener(null);
    }

    private OnButtonClickListener mOnButtonClickListener;

    /**
     * 设置tabitem的点击监听事件
     */
    public void setOnButtonClickListener(OnButtonClickListener l) {
        mOnButtonClickListener = l;
    }

    /**
     * 自定义tabitem点击回调接口
     */
    public static interface OnButtonClickListener {
        // 子类必须覆盖, 根据text生成菜单view
        public void onClick(int selectPosition);
    }

    public static interface MenuGenListener {
        ArrowTextView genMenu(int index, String text);
    }

    class MyPopWindow extends PopupWindow {

        public MyPopWindow(Context context) {
            super(context);
        }


        public MyPopWindow(View contentView, int width, int height) {
            super(contentView, width, height);
        }

        @Override
        public void dismiss() {
            super.dismiss();
            if (selectedView != null) {
                selectedView.setSelected(false);
            }
        }
    }
}
