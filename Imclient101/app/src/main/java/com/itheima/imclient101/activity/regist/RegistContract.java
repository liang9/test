package com.itheima.imclient101.activity.regist;

import com.itheima.imclient101.BasePresenter;
import com.itheima.imclient101.BaseView;
import com.itheima.imclient101.activity.splash.SplashContract;

/**
 * Created by fullcircle on 2017/7/22.
 */

public interface RegistContract {
    interface Presenter extends BasePresenter{
        /**
         * 传入用户名密码 去服务端注册用户
         * @param username
         * @param password
         */
        void registNewUser(String username,String password);
    }
    interface View extends BaseView<Presenter>{
        /**
         * 获取到注册之后的结果
         * @param isSuccess 是否注册成功
         * @param errorMsg 注册失败的错误信息
         */
        void onGetRegistResult(boolean isSuccess, String errorMsg);
    }
}
