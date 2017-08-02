package com.itheima.imclient101.activity.chat;

import com.hyphenate.chat.EMMessage;
import com.itheima.imclient101.BaseView;

import java.util.List;

/**
 * Created by fullcircle on 2017/7/24.
 */

public interface ChatContract {
    interface Presenter{
        void getAllMessage(String username);
        void sendMessage(String content,String username);
        void clearUnreadMessage(String username);
    }
    interface View extends BaseView<Presenter>{
        void onGetAllMessage(List<EMMessage> messages);
        void onSendMessage(boolean isSuccess,String errMsg);
        void updateChatView();
    }
}
