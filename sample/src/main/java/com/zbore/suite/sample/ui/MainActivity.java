/**
 * Copyright (c) 2014-2016, zbore technology. All rights reserved.
 */

package com.zbore.suite.sample.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbore.suite.core.base.BaseActivity;
import com.zbore.suite.core.util.SystemUtil;
import com.zbore.suite.sample.R;
import com.zbore.suite.sample.constant.Constants;
import com.zbore.suite.sample.enums.MainItemEnum;
import com.zbore.suite.sample.model.EventMessage;
import com.zbore.suite.thirdlib.eventbus.EventBus;
import com.zbore.suite.thirdlib.quickadapter.BaseAdapterHelper;
import com.zbore.suite.thirdlib.quickadapter.QuickAdapter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: MainActivity
 *
 * @author liujun
 * @date 2015-7-31
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.navigate_title)).setText(R.string.app_name);
        ListView lv = (ListView) findViewById(R.id.lv_common);
        final QuickAdapter<MainItemEnum> adatper =
                new QuickAdapter<MainItemEnum>(getApplicationContext(), R.layout.item_main_lv, Arrays.asList(MainItemEnum.values())) {

                    @Override
                    protected void convert(BaseAdapterHelper helper, MainItemEnum item) {
                        helper.setText(R.id.item_title, item.title);
                    }
                };
        lv.setAdapter(adatper);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainItemEnum item = adatper.getItem(position);
                Map<String, Serializable> parms = new HashMap<String, Serializable>();
                parms.put(Constants.KEY_OBJECT, item);

                SystemUtil.gotoActivity(MainActivity.this, item.toClass, false, parms);
            }
        });

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(EventMessage.DialClosed event) {
        Toast.makeText(getApplicationContext(), "MusicPlayerActivity right button back !", Toast.LENGTH_SHORT).show();

    }

    public void onEventMainThread(EventMessage.DialBackEvent event) {
        Toast.makeText(getApplicationContext(), "MusicPlayerActivity left button back !!", Toast.LENGTH_SHORT).show();
    }
}
