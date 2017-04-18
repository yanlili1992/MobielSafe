package com.liyanli.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.liyanli.mobilesafe.R;
import com.liyanli.mobilesafe.utils.ConstantValues;
import com.liyanli.mobilesafe.utils.SpUtil;
import com.liyanli.mobilesafe.utils.ToastUtil;

/**
 * Created by liyanli on 2017/4/10.
 */

public class Setup4Activity extends Activity {
    private CheckBox cb_box;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initUI();
    }
    public void nextPage(View view){

        Boolean open_security = SpUtil.getBoolean(this,ConstantValues.OPEN_SECURITY,false);
        if(open_security){
            Intent intent = new Intent(getApplicationContext(),SetUpOverActivity.class);
            startActivity(intent);
            finish();
            SpUtil.putBoolean(this, ConstantValues.SETUP_OVER,true);
        }else{
            ToastUtil.show(getApplicationContext(),"请开启防盗保护");
        }

    }
    public void prePage(View view){
        Intent intent = new Intent(getApplicationContext(),Setup3Activity.class);
        startActivity(intent);
        finish();
    }
    private void initUI(){
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        //是否选中状态的回显
        Boolean open_security = SpUtil.getBoolean(this,ConstantValues.OPEN_SECURITY,false);
        //根据状态修改check_box的文字显示
        cb_box.setChecked(open_security);
        if(open_security){
            cb_box.setText("安全设置已经开启");
        }else{
            cb_box.setText("安全设置未开启");
        }
        //点击过程中，check_box的状态的切换，监听状态改变,以及状态切换的存储
        cb_box.setChecked(!cb_box.isChecked());
        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //isChecked点击后的状态
                //存储点击后的状态
                SpUtil.putBoolean(getApplicationContext(),ConstantValues.OPEN_SECURITY,isChecked);
                //根据开启还是关闭的状态去修改显示的文字
                if(isChecked){
                    cb_box.setText("安全设置已经开启");
                }else{
                    cb_box.setText("安全设置未开启");
                }
            }
        });
        //切换后的状态存储
      //  SpUtil.putBoolean(this,ConstantValues.OPEN_SECURITY,!cb_box.isChecked());
    }
}
