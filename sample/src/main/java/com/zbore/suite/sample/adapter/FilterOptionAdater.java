/**
 * @Title: FilterOptionAdater.java Copyright (c) 2014, Huimeng Technology. All rights reserved.
 */
package com.zbore.suite.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbore.suite.sample.R;
import com.zbore.suite.sample.callback.IDisplay;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: FilterOptionAdater
 * @Description: TODO 用一句话描述这个类的作用
 *
 * @author zbore
 * @date 2014-7-7
 * @version v1.0
 */
public class FilterOptionAdater extends BaseAdapter {

    protected List<? extends IDisplay> mObjects;
    protected Context context;
    protected LayoutInflater mInflater;

    private int selectIndex = -1;

    public List<? extends IDisplay> getmObjects() {
        return mObjects;
    }

    public void setmObjects(List<? extends IDisplay> mObjects) {
        this.mObjects = mObjects;
        this.notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public FilterOptionAdater(Context context, int selectIndex) {
        this(context, new ArrayList<IDisplay>(), selectIndex);
    }

    public FilterOptionAdater(Context context, List<? extends IDisplay> mObjects, int selectIndex) {
        this.mObjects = mObjects;
        mInflater = LayoutInflater.from(context.getApplicationContext());
        this.context = context;

        this.selectIndex = selectIndex;
    }

    @Override
    public int getCount() {
        if (null != mObjects && mObjects.size() > 0) {
            return this.mObjects.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return this.mObjects == null ? null : this.mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PropHoder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_main_lv, parent, false);
            holder = new PropHoder((TextView) convertView.findViewById(R.id.item_title));
            convertView.setTag(holder);

        } else {
            holder = (PropHoder) convertView.getTag();
        }

        IDisplay disply = getmObjects().get(position);
        holder.propName.setText(disply.getDisplayName());

        if (selectIndex == position) {
            holder.propName.setBackgroundColor(context.getResources().getColor(R.color.choose_item_press_color));
        } else {
            holder.propName.setBackgroundResource(R.drawable.list_item_selector);
        }
        return convertView;
    }

    public class PropHoder {
        public TextView propName;

        public PropHoder(TextView propName) {
            super();
            this.propName = propName;
        }
    }
}
