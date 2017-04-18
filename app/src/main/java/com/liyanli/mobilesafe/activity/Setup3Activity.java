package com.liyanli.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.liyanli.mobilesafe.R;
import com.liyanli.mobilesafe.utils.ConstantValues;
import com.liyanli.mobilesafe.utils.SpUtil;
import com.liyanli.mobilesafe.utils.ToastUtil;

/**
 * Created by liyanli on 2017/4/10.
 */

public class Setup3Activity extends Activity{
    private EditText et_phone_number;
    private Button bt_select_number;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initUI();
    }
    public void nextPage(View view){
        //点击按钮以后，需要获取输入框中的联系人，再做下一页操作
        String phone = et_phone_number.getText().toString();

        //在sp存储相关联系人之后才能跳转到下一个界面
      //  String contact_phone = SpUtil.getString(getApplicationContext(),ConstantValues.CONTACT_PHONE,"");
        //只需要判断输入框里面是不是有数据
        if(!TextUtils.isEmpty(phone)){
            Intent intent = new Intent(getApplicationContext(),Setup4Activity.class);
            startActivity(intent);
            finish();
            //如果是输入的电话号码，还需要保存
            SpUtil.putString(getApplicationContext(),ConstantValues.CONTACT_PHONE,phone);
        }else{
            ToastUtil.show(this,"请输入电话号码");
        }
    }
    public void prePage(View view){
        Intent intent = new Intent(getApplicationContext(),Setup2Activity.class);
        startActivity(intent);
        finish();
    }
    private void initUI(){
        //显示电话号码的输入框
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        //获取联系人电话号码的回显过程
        String phone = SpUtil.getString(this,ConstantValues.CONTACT_PHONE,"");
        et_phone_number.setText(phone);
        //选择联系人的对话框
        bt_select_number = (Button) findViewById(R.id.bt_select_number);
        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ContactListActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(data!=null){
            //返回到当前界面的时候，接收结果的方法
            String phone = data.getStringExtra("phone");
            //将特殊字符过滤(将中划线转换成字符串，将中间的空格去掉，并过滤掉空格(前端和后端的是trim))
            phone = phone.replace("-","").replace(" ","").trim();
            et_phone_number.setText(phone);
            //存储联系人至sp中
            SpUtil.putString(getApplicationContext(), ConstantValues.CONTACT_PHONE,phone);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}
