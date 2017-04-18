package com.liyanli.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.liyanli.mobilesafe.R;
import com.liyanli.mobilesafe.utils.ConstantValues;
import com.liyanli.mobilesafe.utils.SpUtil;
import com.liyanli.mobilesafe.utils.ToastUtil;

/**
 * Created by liyanli on 2017/4/9.
 */

public class HomeActivity extends Activity {
    public GridView gv_home;
    public String[] mTitleStr;
    public int[] mDrawableIds;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //初始化控件
        initUI();
        //初始化数据的方法
        initData();
    }

    private void initData(){
        mTitleStr = new String[] {
                "手机防盗","通信卫士","软件管理","进程管理","流量统计",
                "手机杀毒","缓存管理","高级工具","设置中心"
        };
        mDrawableIds = new int[]{
                R.drawable.homesafe,R.drawable.callmsgsafe,R.drawable.apps,
                R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
                R.drawable.sysoptimize,R.drawable.hoemtools,R.drawable.settings
        };
        //九宫格控件设置数据适配器（等同于listView适配器）
        gv_home.setAdapter(new MyAdapter());
        //注册九宫格单个条目的点击事件
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //点中列表条目索引position
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        showDialog();
                        break;
                    case 8:
                        Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    protected void showDialog() {
        //判断本地是否有密码(sp，读写字符串)
        String psd = SpUtil.getString(this, ConstantValues.MOBILE_SAFE_PASSWORD,"");
        //1.初始设置密码对话框
        if(TextUtils.isEmpty(psd)){
                showSetPsdDialog();
        }else{ //2.确认密码对话框
                showConfirmPsdDialog();
        }
    }

    /**
     * 设置密码的对话框
     */
    protected void showSetPsdDialog(){
        //因为需要自己定义对话框的样式，所以要用dialog.setView(view)
        //view是由自己编写的xml转换成的view对象
        //Builder builder = new AlertDialog.Builder(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();//内部类获取外部变量的时候，需要把外部变量设置成final
        final View view = View.inflate(this,R.layout.dialog_set_psd,null);
        //让对话框显示一个自定义的对话框效果
       // dialog.setView(view);
        //为了兼容低版本，在给对话框设置布局的时候，让其没有内边距（安卓系统默认提供的）
        dialog.setView(view,0,0,0,0);
        dialog.show();
       // Button bt_submit = (Button)findViewById(R.id.bt_submit);//不加view对应的是HomeActivity的布局
        Button bt_submit = (Button)view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button)view.findViewById(R.id.bt_cancel);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_set_psd = (EditText)view.findViewById(R.id.et_set_psd);
                EditText et_confirm_psd = (EditText)view.findViewById(R.id.et_confirm_psd);

                String psd = et_set_psd.getText().toString().trim();
                String confirmPsd =  et_confirm_psd.getText().toString().trim();

                if(!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(confirmPsd)){
                    if(psd.equals(confirmPsd)){
                        //进入手机防盗模块,开启一个新的activity
                        Intent intent = new Intent(getApplicationContext(),SetUpOverActivity.class);
                        startActivity(intent);
                        //跳转到新的界面以后需要关闭（隐藏）对话框
                        dialog.dismiss();
                        //把输入的密码写入sp中
                        SpUtil.putString(getApplicationContext(),ConstantValues.MOBILE_SAFE_PASSWORD,psd);
                    }else{
                        ToastUtil.show(getApplicationContext(),"输入密码和确认密码不一致，请确认");
                    }
                }else{
                    ToastUtil.show(getApplicationContext(),"输入密码为空，请确认");
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    /**
     * 确认密码的对话框
     */
    protected void showConfirmPsdDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();//内部类获取外部变量的时候，需要把外部变量设置成final
        final View view = View.inflate(this,R.layout.dialog_confirm_psd,null);
        //让对话框显示一个自定义的对话框效果
        //dialog.setView(view);
        dialog.setView(view,0,0,0,0);
        dialog.show();
        // Button bt_submit = (Button)findViewById(R.id.bt_submit);//不加view对应的是HomeActivity的布局
        Button bt_submit = (Button)view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button)view.findViewById(R.id.bt_cancel);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_confirm_psd = (EditText)view.findViewById(R.id.et_confirm_psd);
                String confirmPsd =  et_confirm_psd.getText().toString().trim();
                String psd = SpUtil.getString(getApplicationContext(),ConstantValues.MOBILE_SAFE_PASSWORD,"");
                if(!TextUtils.isEmpty(confirmPsd)){
                    if(psd.equals(confirmPsd)){
                        //进入手机防盗模块,开启一个新的activity
                        Intent intent = new Intent(getApplicationContext(),SetUpOverActivity.class);
                        startActivity(intent);
                        //跳转到新的界面以后需要关闭（隐藏）对话框
                        dialog.dismiss();
                    }else{
                        ToastUtil.show(getApplicationContext(),"输入密码和确认密码不一致，请确认");
                    }
                }else{
                    ToastUtil.show(getApplicationContext(),"输入密码为空，请确认");
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            //条目的总数 文字总数==图片张数
            return mTitleStr.length;
        }
        @Override
        public Object getItem(int position) {
            return mTitleStr[position];
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(),R.layout.gridview_item,null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);

            tv_title.setText(mTitleStr[position]);
            iv_icon.setBackgroundResource(mDrawableIds[position]);
            return view;
        }
    }
    private void initUI(){
        gv_home = (GridView) findViewById(R.id.gv_home);
    }
}
