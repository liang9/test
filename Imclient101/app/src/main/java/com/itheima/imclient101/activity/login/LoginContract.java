package com.itheima.imclient101.activity.login;

import com.itheima.imclient101.BasePresenter;
import com.itheima.imclient101.BaseView;

/**
 * Created by fullcircle on 2017/7/22.
 */

public interface LoginContract {
    interface Presenter extends BasePresenter{
        void login(String username,String pwd);
    }
    interface View extends BaseView<Presenter>{
        void onGetLoginResult(boolean isSuccess,String errorMessage);
    }
}
