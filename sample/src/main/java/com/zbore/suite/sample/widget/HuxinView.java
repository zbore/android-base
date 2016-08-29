/**
 * com.zbore.pulllmenudown.view.HuxinView
 * description:
 * version：
 * author：liujun
 * create at 2015-7-6 
 */
package com.zbore.suite.sample.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zbore.suite.sample.R;
import com.zbore.suite.sample.adapter.FilterOptionAdater;
import com.zbore.suite.sample.callback.IDisplay;
import com.zbore.suite.sample.callback.ViewBaseAction;
import com.zbore.suite.sample.model.DataGenerater;

import java.util.List;

/**
 * @ClassName: HuxinView
 * @Description: TODO 用一句话描述这个类的作用
 *
 * @author liujun
 * @date 2015-7-6
 */
public class HuxinView extends RelativeLayout implements ViewBaseAction {
    
    public static interface OnSelectListener {
        public void onSelectValue(String value, String text);
    }
    
    private Context mContext;
    
    private ListView listView;
    
    private FilterOptionAdater adatper;
    
    private OnSelectListener mOnSelectListener;
    
    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }
    
    public HuxinView(Context context) {
        super(context);
        init(context);
    }

    public HuxinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    public HuxinView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    
    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_huxin, this, true);
        
        listView = (ListView) findViewById(R.id.lv_popup);
        adatper = new FilterOptionAdater(context, DataGenerater.getAllHuxin(), -1);
        listView.setAdapter(adatper);
        
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adatper.setSelectIndex(position);
                
                if (mOnSelectListener != null) {
                    IDisplay display = adatper.getmObjects().get(position);
                    mOnSelectListener.onSelectValue(display.getDisplayName(), display.getDisplayName());
                }
            }
        });
    }
    
    public void setObjects(List<? extends IDisplay> mObjects) {
        adatper.setmObjects(mObjects);
    }
    
    public void setSelectIndex(int selectIndex) {
        adatper.setSelectIndex(selectIndex);
    }
    
    @Override
    public void hide() {
        
    }

    @Override
    public void show() {
        
    }

}
