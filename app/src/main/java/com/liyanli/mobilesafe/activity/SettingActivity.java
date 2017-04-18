package com.liyanli.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.liyanli.mobilesafe.R;
import com.liyanli.mobilesafe.utils.ConstantValues;
import com.liyanli.mobilesafe.utils.SpUtil;
import com.liyanli.mobilesafe.view.SettingItemView;

/**
 * Created by liyanli on 2017/4/9.
 */

public class SettingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
    }

    /**
     * 获取版本更新开关
     */
    protected void initUpdate(){
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        //获取已有的开关状态用作显示
        boolean open_update = SpUtil.getBoolean(this, ConstantValues.OPEN_UPDATE,false);
        //是否选中，根据上一次的操作结果去做选择
        siv_update.setCheck(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果之前是选中，点击后编程未选中
                //如果之前没有选中，点击过后编程选中
                //获取之前的选中状态
                boolean isCheck = siv_update.isCheck();
                //将原有状态取反，就实现了功能
                siv_update.setCheck(!isCheck);
                //将取反后的状态存储到相应的sp中
                SpUtil.putBoolean(getApplicationContext(),ConstantValues.OPEN_UPDATE,!isCheck);
            }
        });
    }
}
