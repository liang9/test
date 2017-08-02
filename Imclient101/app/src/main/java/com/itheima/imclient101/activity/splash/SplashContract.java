package com.itheima.imclient101.activity.splash;

import com.itheima.imclient101.BasePresenter;
import com.itheima.imclient101.BaseView;

/**
 * Created by fullcircle on 2017/7/22.
 */

public interface SplashContract {
    interface View extends BaseView<Presenter>{
        /**
         * 获取到登录状态之后 调用这个方法
         * @param isLoginBefore 如果是true 说明已经登录了
         */
        void onGetIsLoginResult(boolean isLoginBefore);
    }

    interface Presenter extends BasePresenter{
        /**
         * 检测当前客户端是否登录了
         */
        void checkIsLogin();
    }
}
