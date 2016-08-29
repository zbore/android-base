/**
 * Copyright (c) 2014-2016, zbore technology. All rights reserved.
 */

package com.zbore.suite.sample.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zbore.suite.core.base.BaseActivity;
import com.zbore.suite.sample.R;
import com.zbore.suite.sample.constant.Constants;
import com.zbore.suite.sample.enums.MainItemEnum;
import com.zbore.suite.sample.model.ModelWrapper.MArea;
import com.zbore.suite.sample.widget.AreaView;
import com.zbore.suite.sample.widget.ArrowTextView;
import com.zbore.suite.sample.widget.HuxinView;
import com.zbore.suite.sample.widget.PullMenuDown;
import com.zbore.suite.sample.widget.PullMenuDown.MenuGenListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PMenuWindowActivity
 * @Description: TODO 用一句话描述这个类的作用
 *
 * @author liujun
 * @date 2015-7-6
 */
public class PMenuWindowActivity extends BaseActivity {

    private MainItemEnum item;

    private PullMenuDown expandTabView;
    private List<View> mViewArray = new ArrayList<View>();

    private HuxinView leftView;
    private AreaView rightView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmenu_window);

        item = (MainItemEnum) getIntent().getSerializableExtra(Constants.KEY_OBJECT);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        ((TextView) findViewById(R.id.navigate_title)).setText(item.title);

        expandTabView = (PullMenuDown) findViewById(R.id.expandtab_view);

        /*不同的弹出菜单 */
        leftView = new HuxinView(this);
        rightView = new AreaView(this);
    }

    private void initData() {
        mViewArray.add(leftView);
        mViewArray.add(rightView);

        List<String> mTextArray = new ArrayList<String>();
        mTextArray.add("户型");
        mTextArray.add("价格");

        // 初始化填充数据
        expandTabView.setValue(mTextArray, mViewArray, new MenuGenListener() {

            @Override
            public ArrowTextView genMenu(int index, String text) {
                // 标题按钮
//                ArrowTextView view = (ArrowTextView) LayoutInflater.from(PullMainActivity.this).inflate(R.layout.view_arrowtext, expandTabView, false);
                ArrowTextView view = new ArrowTextView(PMenuWindowActivity.this);
                view.setArrowText(text);
                return view;
            }
        });
    }

    private void initListener() {
        leftView.setOnSelectListener(new HuxinView.OnSelectListener() {

            @Override
            public void onSelectValue(String value, String text) {
                onRefresh(leftView, text);
            }

        });

        rightView.setOnSelectListener(new AreaView.OnSelectListener() {

            @Override
            public void onSelectValue(MArea value, String text) {
                onRefresh(rightView, text);
            }
        });
    }

    private void onRefresh(View view, String showText) {
        expandTabView.onPressBack();
        int position = getPositon(view);
        if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
            expandTabView.setTitle(showText, position);
        }
        Toast.makeText(PMenuWindowActivity.this, showText, Toast.LENGTH_SHORT).show();
    }

    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onBackPressed() {
        if (!expandTabView.onPressBack()) {
            finish();
        }
    }
}
