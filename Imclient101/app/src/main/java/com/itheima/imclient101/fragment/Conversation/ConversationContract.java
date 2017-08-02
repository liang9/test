package com.itheima.imclient101.fragment.Conversation;

import com.hyphenate.chat.EMConversation;
import com.itheima.imclient101.BaseView;

import java.util.List;

/**
 * Created by fullcircle on 2017/7/24.
 */

public interface ConversationContract {
    interface  Presenter{
        void getAllConversation();
        int getALLUnreadMessaegCound();
        void clearAllUnreadMessaeg();
        void clearUnreadMessage(String username);
    }
    interface View extends BaseView<Presenter>{
        void onGetAllConversation(List<EMConversation> conversationList);
    }
}
