package com.itheima.imclient101.activity.chat;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.itheima.imclient101.callback.MyCallBack;
import com.itheima.imclient101.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fullcircle on 2017/7/24.
 */

public class ChatPresenter implements ChatContract.Presenter {
    private ChatContract.View mView;

    public ChatPresenter(ChatContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    private List<EMMessage> emMessages = new ArrayList<>();

    @Override
    public void getAllMessage(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
//获取此会话的所有消息
       // List<EMMessage> messages = conversation.getAllMessages();
//SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多

        //EMConversation 代表一个会话 就是和某一个用户的聊天 如果是第一次和某个人聊天 这个conversation 是null
        //一定要做非空判断
        if(conversation != null){
            //获取最后一条消息
            EMMessage lastMessage = conversation.getLastMessage();
            String msgId = lastMessage.getMsgId();
            int allMsgCount = conversation.getAllMsgCount();
            //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
            //这个方法获取到的集合 不包括msgId 所对应的消息
            List<EMMessage> messages = conversation.loadMoreMsgFromDB(msgId, allMsgCount);
            emMessages.clear();
            emMessages.addAll(messages);
            emMessages.add(lastMessage);
            mView.onGetAllMessage(emMessages);
        }else{
            mView.onGetAllMessage(null);
        }

    }

    @Override
    public void sendMessage(String content, String username) {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        final EMMessage message = EMMessage.createTxtSendMessage(content, username);
        emMessages.add(message);
        message.setMessageStatusCallback(new MyCallBack() {
            @Override
            public void success() {
                //发送成功
                mView.updateChatView();
            }

            @Override
            public void error(int i, String s) {
                //发送失败
                mView.updateChatView();
            }

            @Override
            public void onProgress(int i, String s) {
                //正在发送
                mView.updateChatView();
            }
        });
        ThreadUtils.RunOnSubThread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().chatManager().sendMessage(message);
            }
        });
        mView.onGetAllMessage(emMessages);
    }

    @Override
    public void clearUnreadMessage(String username){
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        if(conversation != null){
            //标记当前会话所有消息为已读
            conversation.markAllMessagesAsRead();
        }
    }
}
