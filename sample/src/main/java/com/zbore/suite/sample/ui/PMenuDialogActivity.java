/** Copyright (c) 2014-2016, zbore technology. All rights reserved.
 *
*/

package com.zbore.suite.sample.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbore.suite.core.base.BaseActivity;
import com.zbore.suite.core.util.SystemUtil;
import com.zbore.suite.sample.R;
import com.zbore.suite.sample.callback.ViewBaseAction;
import com.zbore.suite.sample.constant.Constants;
import com.zbore.suite.sample.enums.MainItemEnum;
import com.zbore.suite.sample.model.EventMessage;
import com.zbore.suite.sample.model.EventMessage.DialClosed;
import com.zbore.suite.sample.model.ModelWrapper.MArea;
import com.zbore.suite.sample.widget.AreaView;
import com.zbore.suite.sample.widget.HuxinView;
import com.zbore.suite.sample.widget.QFPopupDialog;
import com.zbore.suite.thirdlib.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PMenuDialogActivity
 * @Description: TODO 用一句话描述这个类的作用
 *
 * @author liujun
 * @date 2015-7-8
 */
public class PMenuDialogActivity extends BaseActivity implements OnClickListener {
    private MainItemEnum item;
    
    
    private Rect rect = new Rect();
    
    private View rootView;
    private View huxinMenu, areaMenu;
    private View selectedMenu;

    private TextView huxinText, areaText;

    private HuxinView huxinView;
    private AreaView areaView;
    
    private List<RelativeLayout> mViewArray = new ArrayList<RelativeLayout>();

    // 所有的弹出试图
    private List<View> popupViews = new ArrayList<View>();

    private QFPopupDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmemu_dialog);
        
        item = (MainItemEnum) getIntent().getSerializableExtra(Constants.KEY_OBJECT);
        
        rootView = findViewById(R.id.rootView);
        
        initView();
        initListener();
    }

    private void initView() {
        ((TextView)findViewById(R.id.navigate_title)).setText(item.title);
        findViewById(R.id.btn_navigate_right).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_navigate_right).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DialClosed(null));
                SystemUtil.goBack(PMenuDialogActivity.this);
            }
        });
        
        
        /*不同的弹出菜单 */
        huxinView = new HuxinView(this);
        areaView = new AreaView(this);
        
        mViewArray.add(genParentLayout(huxinView));
        mViewArray.add(genParentLayout(areaView));

        popupViews.add(huxinView);
        popupViews.add(areaView);

        // 菜单的tag顺序 和 弹出视图顺序一样
        huxinMenu = findViewById(R.id.rl_huxin);
        huxinMenu.setTag(0);

        areaMenu = findViewById(R.id.rl_area);
        areaMenu.setTag(1);

        huxinText = (TextView) findViewById(R.id.tv_huxin);
        areaText = (TextView) findViewById(R.id.tv_area);

        huxinMenu.setOnClickListener(this);
        areaMenu.setOnClickListener(this);

        dialog = new QFPopupDialog(this, R.style.Theme_Dialog_Generic, findViewById(R.id.rl_huxin));
        dialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                clearSelectedMenu();
            }
        });
    }

    private void initListener() {
        huxinView.setOnSelectListener(new HuxinView.OnSelectListener() {

            @Override
            public void onSelectValue(String value, String text) {
                onPressBack();
                huxinText.setText(text);
            }
        });

        areaView.setOnSelectListener(new AreaView.OnSelectListener() {

            @Override
            public void onSelectValue(MArea value, String text) {
                onPressBack();
                areaText.setText(text);
            }
        });
    }
    
    @Override
    public void gotoBack(View view) {
        EventBus.getDefault().post(new EventMessage.DialBackEvent());
        super.gotoBack(view);
    }
    
    /*生成子试图的父试图 */
    private RelativeLayout genParentLayout(View view) {
        RelativeLayout relLayout = new RelativeLayout(this);
//        relLayout.setBackgroundColor(getResources().getColor(R.color.popup_main_background));
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relLayout.addView(view, parms);

        // 弹出View 点击事件
        relLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressBack();
            }
        });
        return relLayout;
    }

    private void hideView(int position) {
        View tView = popupViews.get(position);
        if (tView instanceof ViewBaseAction) {
            ViewBaseAction f = (ViewBaseAction) tView;
            f.hide();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_huxin:
                showQFPopDialog(v, mViewArray.get(0));
                break;

            case R.id.rl_area:
                showQFPopDialog(v, mViewArray.get(1));
                break;

            default:
                break;
        }
    }

    private void showQFPopDialog(View menuView, ViewGroup contentView) {
        if (selectedMenu != null && selectedMenu != menuView) {
            // 清除选中状态
            clearSelectedMenu();
        }

        selectedMenu = menuView;
        selectedMenu.setSelected(!selectedMenu.isSelected());

        if (selectedMenu.isSelected()) {
            // 先取消之前的，再显示
            rootView.getWindowVisibleDisplayFrame(rect);
            dialog.showDlg(contentView, rect.bottom);

        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private void clearSelectedMenu() {
        if (selectedMenu != null) {
            selectedMenu.setSelected(false);
            // menu 对应的 view 消失
            hideView((Integer) selectedMenu.getTag());
            selectedMenu = null;
        }
    }

    /**
     * 如果菜单成展开状态，则让菜单收回去
     */
    public boolean onPressBack() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            clearSelectedMenu();
            return true;
        } else {
            return false;
        }
    }
}
