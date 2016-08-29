/**
 * com.zbore.example.enums.MainItemEnum
 * description:
 * version：
 * author：liujun
 * create at 2015-8-4 
 */
package com.zbore.suite.sample.enums;

import com.zbore.suite.sample.ui.PMenuDialogActivity;
import com.zbore.suite.sample.ui.PMenuWindowActivity;

import java.io.Serializable;

/**
 * @ClassName: MainItemEnum
 * @Description: TODO 用一句话描述这个类的作用
 *
 * @author liujun
 * @date 2015-8-4
 */
public enum MainItemEnum implements Serializable {

    PMenuWindow("弹出菜单(PopupWindow)", "弹出菜单(PopupWindow)", PMenuWindowActivity.class),
    PMenuDialog("弹出菜单(Dialog)", "弹出菜单(Dialog)", PMenuDialogActivity.class);

    /*
    CrashHandler("程序崩溃处理/CrashHandler", "测试程序崩溃处理", CrashHandlerActivity.class),
    MeizuSmartBar("系统测试", "系统测试", MeizuSmartBarActivity.class),
    PMenuWindow("弹出菜单(PopupWindow)", "弹出菜单(PopupWindow)", PMenuWindowActivity.class),
    PMenuDialog("弹出菜单(Dialog)", "弹出菜单(Dialog)", PMenuDialogActivity.class),
    VolleyDemo("Volley Demo", "Volley Demo", VolleyBaseActivity.class),
    MusicPlayerDemo("音乐播放器", "Music Player Demo", MusicPlayerActivity.class),
    HandlerTimerDemo("Hanler+Timer", "Hanler + Timer Demo", TimerActivity.class),
    PhoneStateDemo("PhoneStateDemo", "电话状态改变监听", PhoneStateActivity.class),
    CompressImgDemo("CompressImgDemo", "压缩图片测试", CompressImageActivity.class),
    ChooseMultiImgDemo("多选图片", "多选图片测试", MainChooseImgActivity.class),
    SlideImgDemo("图片轮播", "图片轮播", SlideShowActivity.class),
    SlideImgDemo2("图片轮播2", "图片轮播2", ADMainActivity.class),
    WebProgessDemo("本地网页打开App", "WebView", WebProgessBarActivity.class),
    BottomViewDemo("底部弹出View", "底部弹出View", BottomViewActivity.class),
    PagerSlidingDemo("PagerSliding", "PagerSliding Demo", PagerSlidingTabStripDemo.class),
    ListViewDemo("ListViewDemo", "ListViewDemo", com.zbore.example.ui.ListViewDemo.class),
    ViewTouchDemo("ViewTouchDemo", "ViewTouchDemo", com.zbore.example.ui.ViewTouchActivity.class);
    */

    public String title;
    
    public String desc;
    
    @SuppressWarnings("rawtypes")
    public Class toClass;

    @SuppressWarnings("rawtypes")
    private MainItemEnum(String title, String desc, Class toClass) {
        this.title = title;
        this.desc = desc;
        this.toClass = toClass;
    }
}
