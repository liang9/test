package com.itheima.imclient101.fragment.Conversation;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by fullcircle on 2017/7/24.
 */

public class ConversationPresenter implements ConversationContract.Presenter {
    private ConversationContract.View mView;

    public ConversationPresenter(ConversationContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void getAllConversation() {
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        Collection<EMConversation> values = allConversations.values();
        List<EMConversation> conversations = new ArrayList<>();
        conversations.addAll(values);
        Collections.sort(conversations, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation o1, EMConversation o2) {
                return (int)(o2.getLastMessage().getMsgTime()-o1.getLastMessage().getMsgTime());
            }
        });
        mView.onGetAllConversation(conversations);

    }

    @Override
    public void clearUnreadMessage(String username){
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        if(conversation != null){
            //标记当前会话所有消息为已读
            conversation.markAllMessagesAsRead();
        }
    }

    @Override
    public int getALLUnreadMessaegCound() {

        return  EMClient.getInstance().chatManager().getUnreadMessageCount();
    }

    @Override
    public void clearAllUnreadMessaeg() {
        EMClient.getInstance().chatManager().markAllConversationsAsRead();
    }
}
