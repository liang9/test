package com.itheima.imclient101.fragment.Plugin;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.itheima.imclient101.callback.MyCallBack;

/**
 * Created by fullcircle on 2017/7/23.
 */

public class LogoutPresenter implements PluginContract.Presenter {
    private PluginContract.View View;

    public LogoutPresenter(PluginContract.View view) {
        View = view;
        view.setPresenter(this);
    }

    @Override
    public void logout() {
        EMClient.getInstance().logout(true, new MyCallBack() {
            @Override
            public void success() {
                View.onGetLogoutResult(true,null);
            }

            @Override
            public void error(int i, String s) {
                View.onGetLogoutResult(false,s);
            }
        });
    }
}
