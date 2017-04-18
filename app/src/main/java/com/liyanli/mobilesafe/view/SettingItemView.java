package com.liyanli.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liyanli.mobilesafe.R;

/**
 * Created by liyanli on 2017/4/9.
 */
/**
 * 将xmlwenjian 转换成view对象，并在手机上显示
 * 不管调用哪个构造方法，嘴周都去调用第三个
 */

public class SettingItemView extends RelativeLayout {
    private static final String tag = "SettingItemView";
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.liyanli.mobilesafe";
    private CheckBox cb_box;
    private TextView tv_des;
    private TextView tv_title;
    private String mDestitle;
    private String mDeson;
    private String mDesoff;
    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, null,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml-->view 将设置界面的一个条目转换成哼view对象，直接添加到当前SettingItemView对应的view中
        View.inflate(context, R.layout.setting_item_view,this);
        //View view = View.inflate(context, R.layout.setting_item_view,null);
        //this.addView(view);
        /**
         * 自定义控件组合里的标题描述
         * 自定义控件组合里的内容描述
         * 自定义空间组合里的checkbox描述
         */
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        //获取自定义以及原生属性的操作，写在此处，AttributeSet attrs对象中获取
        initAttrs(attrs);
        tv_title.setText(mDestitle);

    }

    /**
     *
     * @param attrs 构造方法中维护好的属性集合，返回属性集合中自定义属性的属性值
     */
    public void initAttrs(AttributeSet attrs){
        /*//获取属性的总个数
        Log.i(tag,"attrs.getAttributeCount()="+attrs.getAttributeCount());
        //  获取属性名称和属性值
        for(int i=0;i<attrs.getAttributeCount();i++){
            Log.i(tag,"name = "+attrs.getAttributeName(i));
            Log.i(tag,"value = "+attrs.getAttributeValue(i));
            Log.i(tag,"分割线================");
        }*/
        //通过名空间和属性名称获取属性值
        mDestitle = attrs.getAttributeValue(NAMESPACE,"mDestitle");
        mDeson = attrs.getAttributeValue(NAMESPACE,"mDeson");
        mDesoff = attrs.getAttributeValue(NAMESPACE,"mDestoff");

        Log.i(tag,mDestitle);
        Log.i(tag,mDeson);
        Log.i(tag,mDesoff);
    }

    /**
     *
     * @return返回当前SettingItemView是否选中，cb_box返回true（SettingItemView开启）cb_box返回false（SettingItemView关闭）
     */
    public boolean isCheck(){
        return cb_box.isChecked();
    }

    /**
     *
     * @param isCheck 作为是否开启的变量，由点击过程去传递
     */
    public void setCheck(boolean isCheck){
        cb_box.setChecked(isCheck);
        if(isCheck){
            //isCheck为true，代表自动更新已开启
            tv_des.setText(mDeson);
        }else{
            tv_des.setText(mDesoff);
        }
    }
}
