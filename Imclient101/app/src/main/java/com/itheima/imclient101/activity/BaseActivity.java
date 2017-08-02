package com.itheima.imclient101.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.hyphenate.util.DateUtils;
import com.itheima.imclient101.activity.login.LoginActivity;
import com.itheima.imclient101.event.MessageEvent;
import com.itheima.imclient101.event.OfflineEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

/**
 * Created by fullcircle on 2017/7/22.
 */

public class BaseActivity extends AppCompatActivity {
    protected ProgressDialog progressDialog;
    private LocalBroadcastManager broadcastManager;
    private MyExitReceiver receiver;

    protected void showProgressDialog(String msg){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new MyExitReceiver();
        broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        broadcastManager.registerReceiver(receiver,new IntentFilter("com.itheima.exit"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog =null;
        }
        broadcastManager.unregisterReceiver(receiver);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getOfflineMessage(OfflineEvent event){
        System.out.println("getOfflineMessage");
        //获取下线消息
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("下线通知");
        builder.setMessage("您的账号于"+ DateUtils.getTimestampString(new Date())+"在其它设备登录,如非本人操作请及时修改密码!");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //关闭所有的activity

                broadcastManager.sendBroadcast(new Intent("com.itheima.exit"));
                //开启登录activity
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        builder.show();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private class MyExitReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if("com.itheima.exit".equals(intent.getAction())){
                finish();
            }
        }
    }
}
