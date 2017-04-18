package com.liyanli.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.liyanli.mobilesafe.R;
import com.liyanli.mobilesafe.utils.ConstantValues;
import com.liyanli.mobilesafe.utils.SpUtil;
import com.liyanli.mobilesafe.utils.StreamUtil;
import com.liyanli.mobilesafe.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.lang.String;

/**
 * Created by liyanli on 2017/4/7.
 */

public class SplashActivity extends Activity {

    public TextView tv_version_name;
    public RelativeLayout  rl_root;
    public int mLocalVersionCode;
    protected static final String tag = "SplashActivity";
    public Message msg;
    private static final int UPDATE_VERSION = 100;
    private static final int ENTER_HOME = 101;
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int JSON_ERROR = 104;
    private String mVersionDes;
    private String mVersionName;
    private String  mVersionCode;
    private String mDownloadUrl;



    private Handler mHandler = new Handler(){
      public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case UPDATE_VERSION:
                    //提示用户进行版本更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    //进入应用程序主界面,activity跳转过程
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtil.show(getApplicationContext(),"url异常");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtil.show(getApplicationContext(),"读取异常");
                    enterHome();
                   break;
                case JSON_ERROR:
                    ToastUtil.show(getApplicationContext(),"json解析异常");
                    enterHome();
                    break;
                default:
                    break;
            }
      }
  };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //初始化控件
        initUI();
        //初始化数据
        initData();
        //初始化动画
        initAnimation();
    }
    /**
     * 初始化界面动画,淡入淡出
     */
    private void initAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(3000);
        rl_root.startAnimation(alphaAnimation);
    }


    /**
     * 获取数据方法
     */
    private void initData() {
        //应用版本名称
        tv_version_name.setText("版本名称:" + getVersionName());
        //检测是否有更新(本地版本号和服务器版本号比较),如果有新版本，提示用户更新(member)
        mLocalVersionCode = getVersionCode();
        //获取服务器版本号
        if(SpUtil.getBoolean(this, ConstantValues.OPEN_UPDATE,false)){
            checkVersion();
        }else{
            //直接进入应用程序主界面，消息机制
            //enterHome();
           // mHandler.sendMessageDelayed(msg,4000);
            //在发送消息4s后再处理
            mHandler.sendEmptyMessageDelayed(ENTER_HOME,4000);
        }


    }

    /**
     * 获取版本名称：清单方法中
     *
     * @return 应用版本名称，返回null代表异常
     */
    private String getVersionName() {
        //获取包管理者对象
        PackageManager pm = getPackageManager();
        //从packageManager里获得版本基本信息（版本名称，版本号），0代表基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //获取版本名称
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本地版本号
     *
     * @return 版本号，返回0代表异常
     */
    private int getVersionCode() {
        //获取包管理者对象
        PackageManager pm = getPackageManager();
        //从packageManager里获得版本基本信息（版本名称，版本号），0代表基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取服务器版本号
     */
    protected void checkVersion() {
        new Thread() {
            public void run() {
                msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try{
                    //1. 封装url地址
                    URL url = new URL("http://10.0.2.2:8080/updateMobileSafe.json");
                    //2. 获取连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3.设置请求链接的参数（请求头）
                    //请求超时
                    connection.setConnectTimeout(2000);
                    //读取超时
                    connection.setReadTimeout(2000);
                    //默认GET请求方式
                    connection.setRequestMethod("GET");
                    //4.获取响应码
                    if(connection.getResponseCode()==200){
                        //5.以流的形式获取到数据
                        InputStream is = connection.getInputStream();
                        //6.将数据转换成字符串解读（用工具类实现封装）
                        String json = StreamUtil.Stream2String(is);
                        Log.i(tag,json);
                        //7.json解析

                            JSONObject jsonObject = new JSONObject(json);
                            mVersionName = jsonObject.getString("versionName");
                            mVersionDes = jsonObject.getString("versionDes");
                            mVersionCode = jsonObject.getString("versionCode");
                            mDownloadUrl = jsonObject.getString("downloadUrl");
                            Log.i(tag,mVersionName);
                            Log.i(tag,mVersionCode);
                            Log.i(tag,mVersionDes);
                            Log.i(tag,mDownloadUrl);
                            //8.比对本地版本号和服务器版本号，如果需要就弹出对话框提示用户（UI线程），消息机制

                            if(mLocalVersionCode < Integer.parseInt(mVersionCode)){
                                //弹出对话框，提示用户更新版本
                                msg.what = UPDATE_VERSION;
                            }else {
                                //用户可以进入程序主界面
                                msg.what = ENTER_HOME;
                            }
                        }

                }  catch(MalformedURLException e){
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                }catch(IOException e){
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                }catch (JSONException e){
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
            }finally {
                    //指定网络请求时间为4s,如果不足4s，强制睡眠4s
                    long endTime = System.currentTimeMillis();
                    if(endTime - startTime < 4000){
                        try{
                            Thread.sleep(4000-(endTime-startTime));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                };
        }
        // }
        // new Thread(new Runnable() {
        //  @Override
        //  public void run() {
//
        //   }
        // }).start();

    }.start();}

    /**
     * 获取控件方法
     */
    private void initUI() {
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
    }

    /**
     * 进入应用程序主界面
     */
    public void enterHome(){
        Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
        startActivity(intent);
        //当开启一个新界面的时候，要关闭导航界面
        finish();
    }

    /**
     * 是否更新的逻辑
     */
    public void showUpdateDialog(){
        //对话框是依赖activity存在的
       // AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());//会出错,getApplicationContext是一个全局的context
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("版本更新");
        //版本更新内容展示
        builder.setMessage(mVersionDes);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载链接URL，downloadUrl
                downloadAPK();
            }
        });
        builder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //用户进入程序主界面
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //即使用户点击取消，也需要让其进入应用程序主界面
                enterHome();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 下载apk
     */
    protected void downloadAPK(){
        //1. 判断sd卡是否挂载了
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //2. 获取sd卡路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator+"mobilesafe.apk";
            //3. 发送请求，获取apk，并且放置到指定路径
            HttpUtils httpUtils = new HttpUtils();
            //4. 发送请求，传递参数（下载地址，下载应用放置位置）
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功(下载后放置在sd卡中的apk)
                    Log.i(tag,"下载成功");
                    File file = responseInfo.result;
                    //提示用户去安装
                    installApk(file);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    //下载失败
                    Log.i(tag,"下载失败");
                }
                //刚开始下载的方法
                @Override
                public void onStart(){
                    Log.i(tag,"刚刚开始下载");
                    super.onStart();
                }
                //下载过程中的方法
                @Override
                public void onLoading(long total, long current, boolean isUploading){
                    Log.i(tag,"下载中......");
                    Log.i(tag,"total="+total);
                    Log.i(tag,"current="+current);
                    super.onLoading(total,current,isUploading);
                }
            });

        }

    }

    /**
     * 安装对应的apk
     * @param file 安装文件
     */
    protected void installApk(File file){
        //系统应用中的界面，通过源码找到安装apk入口(需要参考源码去写代码)
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        /*//文件作为数据源
        intent.setData(Uri.fromFile(file));
        //设置安装的类型
        intent.setType("application/vnd.android.package-archive");*/
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        //startActivity(intent);
        startActivityForResult(intent,0);
    }
    /**
     * 开启一个activity后，返回结果调用的方法(不大理解)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        enterHome();
        super.onActivityResult(requestCode,resultCode,data);
    }
}
