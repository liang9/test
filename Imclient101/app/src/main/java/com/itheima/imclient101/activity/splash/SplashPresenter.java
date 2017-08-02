package com.itheima.imclient101.activity.splash;

import com.hyphenate.chat.EMClient;

/**
 * Created by fullcircle on 2017/7/22.
 */

public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View mView;

    public SplashPresenter(SplashContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void checkIsLogin() {
        EMClient emClient = EMClient.getInstance();
        //isConnected 判断是否跟服务端连着
        //isLoggedInBefore 判断之前是否登录了
        if(emClient.isConnected()&&emClient.isLoggedInBefore()){
            mView.onGetIsLoginResult(true);
        }else{
            mView.onGetIsLoginResult(false);
        }
    }
}
