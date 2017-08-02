package com.itheima.imclient101.fragment.contract;

import com.itheima.imclient101.BasePresenter;
import com.itheima.imclient101.BaseView;

import java.util.List;

/**
 * Created by fullcircle on 2017/7/23.
 */

public interface ContactContract {
    interface Presenter extends BasePresenter{
        /**
         * 先从本地加载 再去服务端加载
         */
        void initContact(String username);

        /**
         * 去服务端更新联系人数据
         */
        void updateContact(String username);

        /**
         * 删除联系人
         */
        void deleteContact(String username);
    }
    interface View extends BaseView<Presenter>{

        void onInitContact(List<String> contact);
        void onUpdateContact(List<String> contact,String errorMsg);
        void onDeleteContact(boolean isSuccess,String errMsg);
    }
}
