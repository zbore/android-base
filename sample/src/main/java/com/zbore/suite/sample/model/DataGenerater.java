package com.zbore.suite.sample.model;

import com.zbore.suite.sample.model.ModelWrapper.MArea;
import com.zbore.suite.sample.model.ModelWrapper.MHuxin;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DataGenerater
 * @desc 用一句话描述这个类的作用
 *
 * @author zbore
 * @date 2016/8/29 0029
*/
public class DataGenerater {

    public final static String CUSTOM = "自定义";

    public static List<MHuxin> getAllHuxin() {
        List<MHuxin> list = new ArrayList<MHuxin>();
        list.add(new MHuxin("不限"));
        list.add(new MHuxin("单身公寓"));
        list.add(new MHuxin("一室"));
        list.add(new MHuxin("二室"));
        list.add(new MHuxin("三室"));
        list.add(new MHuxin("四室"));
        list.add(new MHuxin("五室及以上"));
        return list;
    }

    public static List<MArea> getAllArea() {
        List<MArea> list = new ArrayList<MArea>();
        list.add(new MArea("不限", 0, 0));
        list.add(new MArea("50平米以下", 0, 50));
        list.add(new MArea("50-70平米", 50, 70));
        list.add(new MArea("70-90平米", 70, 90));
        list.add(new MArea("90-110平米", 90, 110));
        list.add(new MArea("110-130平米", 110, 130));
        list.add(new MArea("130-150平米", 130, 150));
        list.add(new MArea("150-200平米", 150, 200));
        list.add(new MArea("200-300平米", 200, 300));
        list.add(new MArea("300-500平米", 300, 500));
        list.add(new MArea("500平米以上", 500, 20000));
        list.add(new MArea(CUSTOM, -1, -1));
        return list;
    }


}
