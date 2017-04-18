package com.liyanli.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by liyanli on 2017/4/7.
 */

public class ToastUtil {
    public static void show(Context context, String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
