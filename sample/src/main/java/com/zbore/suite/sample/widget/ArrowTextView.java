/**
 * com.zbore.pulllmenudown.view.ArrowTextView
 * description:
 * version：
 * author：liujun
 * create at 2015-7-6 
 */
package com.zbore.suite.sample.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbore.suite.sample.R;

/**
 * @ClassName: ArrowTextView
 * @Description: 右边带箭头的TextView
 *
 * @author liujun
 * @date 2015-7-6
 */
public class ArrowTextView extends RelativeLayout {

    private TextView menuText;
    private ImageView arrowImage;

    public ArrowTextView(Context context) {
        super(context);
        initView(context);
    }

    public ArrowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ArrowTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        /*
        LayoutInflater.from(context).inflate(R.layout.view_arrowtext_item, this);
        menuText = (TextView) findViewById(R.id.tv_menu_name);
        */
        LinearLayout.LayoutParams selfParms =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(
                        R.dimen.menu_height));
        selfParms.weight = 1.0f;
        setLayoutParams(selfParms);
        setBackgroundResource(R.drawable.menu_item_selector);

        // 构建menu text
        menuText = new TextView(context);
        menuText.setId(R.id.item_title);
        menuText.setTextSize(14.0f);
        menuText.setTextColor(getResources().getColorStateList(R.color.menu_font_selector));

        LayoutParams parms =
                new LayoutParams(LayoutParams.WRAP_CONTENT, (LayoutParams.WRAP_CONTENT));
        parms.addRule(RelativeLayout.CENTER_IN_PARENT);

        addView(menuText, parms);

        // 右边的箭头
        arrowImage = new ImageView(context);
        arrowImage.setImageResource(R.drawable.arrow_item_selector);

        parms = new LayoutParams(LayoutParams.WRAP_CONTENT, (LayoutParams.WRAP_CONTENT));
        parms.addRule(RelativeLayout.CENTER_IN_PARENT);
        parms.addRule(RelativeLayout.RIGHT_OF, R.id.item_title);
        parms.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.menu_arrow_padding);
        addView(arrowImage, parms);
    }

    public void setArrowText(String text) {
        menuText.setText(text);
    }

    public CharSequence getArrowText() {
        return menuText.getText();
    }
}
