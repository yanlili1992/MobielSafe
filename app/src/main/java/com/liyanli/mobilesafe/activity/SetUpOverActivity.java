package com.liyanli.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.liyanli.mobilesafe.R;
import com.liyanli.mobilesafe.utils.ConstantValues;
import com.liyanli.mobilesafe.utils.SpUtil;

/**
 * Created by liyanli on 2017/4/10.
 */

public class SetUpOverActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //TextView textView = new TextView(this);
        //textView.setText("SetUpOverActivity");
       // setContentView(textView);


        boolean setup_over = SpUtil.getBoolean(this, ConstantValues.SETUP_OVER,false);
        if(setup_over){
            //密码设置成功并且完成手机防盗导航界面的设置，跳转到功能列表界面
            setContentView(R.layout.activity_setup_over);
        }else{
            //密码设置成功，但是没有完成导航界面的设置，跳转到导航界面的第一个
            Intent intent = new Intent(this,Setup1Activity.class);
            startActivity(intent);
            //开启新的界面以后，关闭功能列表界面
            finish();
        }
    }
}
