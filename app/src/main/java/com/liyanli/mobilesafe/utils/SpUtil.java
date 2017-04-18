package com.liyanli.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by liuht on 2017/4/9.
 */

public class SpUtil {
    public static SharedPreferences sp;
    //写
    /**
     *写入一个boolean类型的变量到sp中
     * @param context 上下文环境
     * @param key 存储节点的文件名字
     * @param value 存储节点的值boolean
     */
    public static void putBoolean(Context context,String key,Boolean value){
        //存储节点的文件名字，读写方式
        if(sp == null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).commit();
    }
    /**
     *读取一个boolean类型的变量到sp中
     * @param context 上下文环境
     * @param key 节点名称
     * @param defValue 没有此节点默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static boolean getBoolean(Context context,String key,Boolean defValue){
        //存储节点的文件名字，读写方式
        if(sp == null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defValue);
    }
    //写
    /**
     *写入一个boolean类型的变量到sp中
     * @param context 上下文环境
     * @param key 存储节点的文件名字
     * @param value 存储节点的值String
     */
    public static void putString (Context context, String key, String value){
        //存储节点的文件名字，读写方式
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }
    /**
     *读取一个boolean类型的变量到sp中
     * @param context 上下文环境
     * @param key 节点名称
     * @param defValue 没有此节点默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static String getString(Context context, String key, String defValue) {
        //存储节点的文件名字，读写方式
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }
    /**
     * 从sp中移除指定节点
     * @param context 上下文环境
     * @param key 需要移除节点的名称
     */
    public static  void remove(Context context,String key){
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();
    }
}
