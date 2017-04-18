package com.liyanli.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.liyanli.mobilesafe.R;

/**
 * Created by liyanli on 2017/4/10.
 */

public class Setup1Activity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    protected void nextPage(View view){
        Intent intent = new Intent(getApplicationContext(),Setup2Activity.class);
        startActivity(intent);
        finish();
    }
}
