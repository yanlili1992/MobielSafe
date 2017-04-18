package com.liyanli.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.liyanli.mobilesafe.R;
import com.liyanli.mobilesafe.utils.ConstantValues;
import com.liyanli.mobilesafe.utils.SpUtil;
import com.liyanli.mobilesafe.utils.ToastUtil;
import com.liyanli.mobilesafe.view.SettingItemView;

/**
 * Created by liyanli on 2017/4/10.
 */

public class Setup2Activity extends Activity {
    private SettingItemView siv_sim_bound;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUI();
    }
    public void nextPage(View view){
        String serialNumber = SpUtil.getString(this,ConstantValues.SIM_NUMBER,"");
        if(!TextUtils.isEmpty(serialNumber)){
            Intent intent = new Intent(getApplicationContext(),Setup3Activity.class);
            startActivity(intent);
            finish();
        }else{
            ToastUtil.show(this,"请绑定sim卡");
        }
    }
    public void prePage(View view){
        Intent intent = new Intent(getApplicationContext(),Setup1Activity.class);
        startActivity(intent);
        finish();
    }
    private void initUI(){
        siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
        //回显，读取已有的绑定状态，用作显示，sp里是否已有sim卡的序列号
        String sim_bound = SpUtil.getString(this, ConstantValues.SIM_NUMBER,"");
        //判断是否序列号为""
        if(TextUtils.isEmpty(sim_bound)){
            siv_sim_bound.setCheck(false);
        }else{
            siv_sim_bound.setCheck(true);
        }
        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取原来的状态
               boolean isCheck =  siv_sim_bound.isCheck();//内部类使用外部的变量，要么在外部变量前加final，或者把外部变量变成类的成员变量
                //将原有的状态取反,状态设置给当前条目
                siv_sim_bound.setCheck(!isCheck);
                if(!isCheck){
                    //存储（序列号）
                    //获取sim卡序列号TelephonyManager
                    TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    //获取sim卡序列号
                   String simSerialNumber = manager.getSimSerialNumber();
                    //存储
                    SpUtil.putString(getApplicationContext(),ConstantValues.SIM_NUMBER,simSerialNumber);
                }else{
                    //将存储序列号的节点从sp中删除掉
                    SpUtil.remove(getApplicationContext(),ConstantValues.SIM_NUMBER);
                }
            }
        });
    }
}
