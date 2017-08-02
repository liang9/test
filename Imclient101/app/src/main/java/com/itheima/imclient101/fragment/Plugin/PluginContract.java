package com.itheima.imclient101.fragment.Plugin;

import com.itheima.imclient101.BasePresenter;
import com.itheima.imclient101.BaseView;
import com.itheima.imclient101.activity.login.LoginContract;

/**
 * Created by fullcircle on 2017/7/23.
 */

public interface PluginContract {
    interface Presenter extends BasePresenter{
        void logout();
    }
    interface View extends BaseView<Presenter>{
        void onGetLogoutResult(boolean isSuccess,String errMsg);
    }
}
