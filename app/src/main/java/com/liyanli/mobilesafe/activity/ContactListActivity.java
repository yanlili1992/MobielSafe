package com.liyanli.mobilesafe.activity;

import android.app.Activity;
import android.app.Notification;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;

import com.liyanli.mobilesafe.R;
import com.liyanli.mobilesafe.utils.StreamUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.logging.LogRecord;

/**
 * Created by liyanli on 2017/4/10.
 */

public class ContactListActivity extends Activity{
    private static final String tag = "ContactListActivity";
    public ListView lv_contact;
    public MyAdapter myAdapter;
    private List<HashMap<String,String>> contactsLists = new ArrayList<HashMap<String, String>>();
    private android.os.Handler mHandler = new android.os.Handler(){
        public void handleMessage(android.os.Message msg){
            //填充数据适配器
            myAdapter = new MyAdapter();
            lv_contact.setAdapter(myAdapter);
        };
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initUI();
        initData();
    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return contactsLists.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return contactsLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(),R.layout.lv_contacts_view,null);
            TextView tv_name =(TextView) view.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            tv_name.setText(getItem(position).get("name"));
            tv_phone.setText(getItem(position).get("phone"));
            return view;
        }
    }
    protected void initUI(){
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取点中条目的索引指向集合中的对象
                if(myAdapter!=null){
                    HashMap<String,String> hashMap = myAdapter.getItem(position);
                    //获取当前条目指向集合对应的电话号码
                    String phone = hashMap.get("phone");
                    //此电话号码需要给第三个导航界面使用,结束此界面回到导航界面的时候是需要返回数据的
                    Intent intent = new Intent();
                    intent.putExtra("phone",phone);
                    setResult(0,intent);
                    finish();
                }
            }
        });
    }

    /**
     *    获取系统联系人数据的方法
     */
    protected void initData(){
        //因为读取系统联系人可能是耗时操作
        new Thread(){
            public void run(){
                //获取内容解析器对象
                ContentResolver contentResolver = getContentResolver();
                //查询系统联系人数据库表过程(加权限，读取联系人)
                Cursor cursor = contentResolver.query(
                        Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"},
                        null,null,null);
                contactsLists.clear();//集合作为成员变量时要清空一下，避免数据出现重复
                //循环游标直到没有数据为止
                while(cursor.moveToNext()){
                    String id = cursor.getString(0);
                    Log.i(tag,"id = "+id);
                    //根据用户唯一性id值，查询data和mimetype表生成的视图，获取data以及mimetype字段
                    Cursor indexCursor = contentResolver.query(
                            Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1","mimetype"},
                            "raw_contact_id = ?",new String[]{id},null
                    );
                    //循环获取每一个联系人的电话号码和姓名，以及数据类型
                    HashMap<String,String> hashMap = new HashMap<String, String>();
                    while(indexCursor.moveToNext()){
                        String data = indexCursor.getString(0);
                        String mimitype = indexCursor.getString(1);
                        //区分类型
                        if(mimitype.equals("vnd.android.cursor.item/phone_v2")){
                            //数据不为空
                            if(!TextUtils.isEmpty(data)){
                                hashMap.put("phone",data);
                            }
                        }else if(data.equals("vnd.android.cursor.item/name")){
                            if(!TextUtils.isEmpty(data)){
                                hashMap.put("name",data);
                            }
                        }
                    }
                    indexCursor.close();
                    contactsLists.add(hashMap);
                }
                cursor.close();
                //消息机制,发送一条空的消息，告诉主线程可以使用子线程已经填充好的数据集合
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }
}
