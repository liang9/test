package com.itheima.imclient101.activity.regist;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.itheima.imclient101.utils.ThreadUtils;

/**
 * Created by fullcircle on 2017/7/22.
 */

public class RegistPresenter implements RegistContract.Presenter {
    private RegistContract.View mView;

    public RegistPresenter(RegistContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void registNewUser(final String username, final String password) {
        //① 去leancload注册用户 AVUser 这个对象是LeanCloud Api自带的对象
        //它对应服务端的 _User这个表 这个表也是应用创建起来之后 leanCloud自动生成的
        final AVUser avUser = new AVUser();
        avUser.setUsername(username);
        avUser.setPassword(password);
        //signUpInBackground 异步注册 会开启子线程
        avUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(final AVException e) {
                if(e == null){
                   //②注册成功 需要到环信 去注册

                    ThreadUtils.RunOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //③到环信注册的方法分  createAccount 同步方法
                                EMClient.getInstance().createAccount(username,password);
                                //如果没有异常 就说明注册成功了
                                ThreadUtils.RunOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //通知界面注册成功
                                        mView.onGetRegistResult(true,null);
                                    }
                                });
                            } catch (final HyphenateException e1) {
                                e1.printStackTrace();
                                //④环信注册失败  还需要删除在leancloud上注册的用户
                                try {
                                    avUser.delete();
                                } catch (AVException e2) {
                                    e2.printStackTrace();
                                }
                                ThreadUtils.RunOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //注册失败
                                        mView.onGetRegistResult(false,e1.getMessage());
                                    }
                                });
                            }
                        }
                    });

                }else{
                    //② 注册失败 通知界面显示信息
                    mView.onGetRegistResult(false,e.getMessage());
                }
            }
        });
    }
}
