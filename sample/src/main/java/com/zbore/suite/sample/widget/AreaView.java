/**
 * com.zbore.pulllmenudown.view.AreaView
 * description:
 * version：
 * author：liujun
 * create at 2015-7-7 
 */
package com.zbore.suite.sample.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.zbore.suite.sample.R;
import com.zbore.suite.sample.adapter.FilterOptionAdater;
import com.zbore.suite.sample.callback.IDisplay;
import com.zbore.suite.sample.callback.ViewBaseAction;
import com.zbore.suite.sample.model.DataGenerater;
import com.zbore.suite.sample.model.ModelWrapper.MArea;

import java.util.List;


/**
 * @ClassName: AreaView
 * @Description: TODO 用一句话描述这个类的作用
 *
 * @author liujun
 * @date 2015-7-7
 */
public class AreaView extends FrameLayout implements ViewBaseAction, View.OnClickListener{
    
    public static interface OnSelectListener {
        public void onSelectValue(MArea value, String text);
    }

    private Context mContext;

    private ListView listView;
    private FilterOptionAdater adatper;
    
    private View customView;
    private EditText minAreaEdit, maxAreaEdit;

    private OnSelectListener mOnSelectListener;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public AreaView(Context context) {
        super(context);
        init(context);
    }

    public AreaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AreaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    
    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_area, this, true);
        
        listView = (ListView) findViewById(R.id.lv_popup);
        adatper = new FilterOptionAdater(context, DataGenerater.getAllArea(), -1);
        listView.setAdapter(adatper);
        
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adatper.setSelectIndex(position);
                MArea area = (MArea) adatper.getmObjects().get(position);
                if(area.getDisplayName().equals(DataGenerater.CUSTOM)) {
                    showListView(false);
                } else {
                    
                    adatper.setSelectIndex(position);
                    if (mOnSelectListener != null) {
                        mOnSelectListener.onSelectValue(area, area.getDisplayName());
                    }
                }
            }
        });
        
        customView = findViewById(R.id.custom_view);
        minAreaEdit = (EditText) findViewById(R.id.et_min_area);
        maxAreaEdit = (EditText) findViewById(R.id.et_max_area);
        
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);
    }
    
    public void setObjects(List<? extends IDisplay> mObjects) {
        adatper.setmObjects(mObjects);
    }
    
    public void setSelectIndex(int selectIndex) {
        adatper.setSelectIndex(selectIndex);
    }
    
    public void showListView(boolean show) {
        listView.setVisibility(show ? View.VISIBLE : View.GONE);
        customView.setVisibility(!show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                showListView(true);
                break;
                
            case R.id.btn_ok:
                doCustomArea();
                break;

            default:
                break;
        }
    }
    
    private void doCustomArea() {
        // TODO liujun 输入校验
        String minAreaStr = minAreaEdit.getText().toString().trim();
        String maxAreaStr = maxAreaEdit.getText().toString().trim();

        showListView(true);
        
        // 假定合法
        int minArea = Integer.valueOf(minAreaStr).intValue();
        int maxArea = Integer.valueOf(maxAreaStr).intValue();
        String name = new StringBuilder().append(minArea).append("-").append(maxArea).append("平米").toString();
        MArea area = new MArea(name, minArea, maxArea);
        if (mOnSelectListener != null) {
            mOnSelectListener.onSelectValue(area, area.getDisplayName());
        }
    }

    @Override
    public void hide() {
        showListView(true);
    }

    @Override
    public void show() {

    }
}
