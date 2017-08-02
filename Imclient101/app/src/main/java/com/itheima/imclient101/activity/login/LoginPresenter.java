package com.itheima.imclient101.activity.login;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.itheima.imclient101.callback.MyCallBack;

/**
 * Created by fullcircle on 2017/7/22.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;

    public LoginPresenter(LoginContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void login(String username, String pwd) {
        EMClient.getInstance().login(username, pwd, new MyCallBack() {
            @Override
            public void success() {
                mView.onGetLoginResult(true,null);
            }

            @Override
            public void error(int i, String s) {
                mView.onGetLoginResult(false,s);
            }
        });
    }
}
