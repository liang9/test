package com.itheima.imclient101;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.itheima.imclient101.activity.MainActivity;
import com.itheima.imclient101.activity.chat.ChatActivity;
import com.itheima.imclient101.db.DBUtils;
import com.itheima.imclient101.event.ContactEvent;
import com.itheima.imclient101.event.MessageEvent;
import com.itheima.imclient101.event.OfflineEvent;
import com.itheima.imclient101.utils.ThreadUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

/**
 * Created by fullcircle on 2017/7/22.
 */

public class MyApplication extends Application {

    private SoundPool soundPool;
    private int foregroundSound;
    private int backgroundSound;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化环信客户端
        initEMClient();
        //初始化leancloudsdk
        initLeanCloud();
        //初始化数据库工具类
        DBUtils.initDBUtils(this);
        initSoundPool();

    }

    private void initSoundPool() {
        //第一个参数 描述声音池最多存放几个声音
        //参数2  当前声音播放使用的 声音类型 通过AudioManager.STREAM_MUSIC 使用播放媒体文件的声音类型
//        AudioManager.STREAM_ALARM  闹钟
//                AudioManager.STREAM_RING 响铃
        //参数3  目前没用 传入0 作为默认值
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        //load 加载声音到声音池 参数1 上下文 参数2 声音的资源id 一般放到raw目录下 参数3 没用到 传入默认值1
        foregroundSound = soundPool.load(this, R.raw.duan, 1);
        backgroundSound = soundPool.load(this, R.raw.yulu, 1);
    }

    private void initLeanCloud() {
        // 初始化LeanCloud参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"rU7CFMKuCikTNvjBb4EqmIPM-gzGzoHsz","Uyf0s4rmkowaRB7Wj7NbNte6");
        //开启LeanCloud的debug模式
        AVOSCloud.setDebugLogEnabled(true);
    }

    private void initEMClient() {
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
//初始化

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        EMClient.getInstance().init(getApplicationContext(), options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);

        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

//            @Override
//            public void onContactAgreed(String username) {
//                //好友请求被同意
//            }
//
//            @Override
//            public void onContactRefused(String username) {
//                //好友请求被拒绝
//            }

            @Override
            public void onContactInvited(String username, String reason) {
                String name = Thread.currentThread().getName();
                //收到好友邀请
                Log.e("Application",username+"==="+reason+"===="+name);

                try {
                    EMClient.getInstance().contactManager().acceptInvitation(username);
                    Log.e("Application","同意");
                    EventBus.getDefault().post(new ContactEvent(true,username));
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    Log.e("Application","异常");
                }
            }

            @Override
            public void onFriendRequestAccepted(String s) {
                //好友请求被同意
            }

            @Override
            public void onFriendRequestDeclined(String s) {
                //好友请求被拒绝
            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
                EventBus.getDefault().post(new ContactEvent(false,username));
            }


            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
                EventBus.getDefault().post(new ContactEvent(true,username));
            }
        });


        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                System.out.println("onMessageReceived");
                EventBus.getDefault().post(new MessageEvent());
                if(isCurrentAppForeground()){
                    System.out.println("处于前台");
                    //play 播放声音池中的声音
                    //参数1 load()方法加载音频之后的返回值
                    //参数2 参数3 左右声道的音量 0.0~1.0
                    //参数4 优先级 0 优先级最低
                    //参数5 重复播放的次数 0 不重复 -1 无限循环播放
                    //参数6 播放的速度 取值范围 0.5倍速度~ 2.0倍
                    soundPool.play(foregroundSound,1,1,1,0,1);
                }else{
                    System.out.println("处于后台");
                    soundPool.play(backgroundSound,1,1,1,0,1);
                    sendNotification(list.get(list.size()-1));
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
            }

            @Override
            public void onMessageRead(List<EMMessage> list) {
            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {
            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {
            }
        });

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(final int error) {
                ThreadUtils.RunOnUIThread(new Runnable() {

                    @Override
                    public void run() {
                        if(error == EMError.USER_REMOVED){
                            // 显示帐号已经被移除
                        }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                            // 显示帐号在其他设备登录
                            System.out.println("收到设备下线通知");
                            EventBus.getDefault().post(new OfflineEvent());
                        } else {

                        }
                    }
                });
            }
        });



    }

    private void sendNotification(EMMessage emMessage) {
       EMTextMessageBody body = (EMTextMessageBody) emMessage.getBody();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("收到一条新的消息");
        builder.setContentText(body.getMessage());
        builder.setContentInfo("来自"+emMessage.getFrom());
        builder.setSmallIcon(R.mipmap.message);
        builder.setAutoCancel(true);
        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
        Intent intent2 = new Intent(getApplicationContext(), ChatActivity.class);
        intent2.putExtra("username",emMessage.getFrom());
        Intent[] intents = new Intent[]{intent1,intent2};
        PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(),1,intents,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1,notification);

    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private boolean isCurrentAppForeground(){
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //获取当前设备运行的所有任务栈信息
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(500);
        //获取第一个任务栈中 第一个activity的包名 如果和当前包名相同 认为当前应用处于前台
        if(runningTasks.get(0).topActivity.getPackageName().equals(getPackageName())){
            return true;
        }

        return false;
    }
}
