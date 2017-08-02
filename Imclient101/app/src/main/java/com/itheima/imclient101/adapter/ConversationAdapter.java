package com.itheima.imclient101.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.itheima.imclient101.R;

import java.util.Date;
import java.util.List;

/**
 * Created by fullcircle on 2017/7/24.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MyViewHolder> {
    private List<EMConversation> conversationList;
   // private String username;

    public ConversationAdapter(List<EMConversation> conversationList) {
        this.conversationList = conversationList;
    }

    public void setConversationList(List<EMConversation> conversationList) {
        this.conversationList = conversationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_conversation, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //获取当前条目对应的会话对象
        EMConversation emConversation = conversationList.get(position);
        //通过会话对象获取最近一条消息
        EMMessage lastMessage = emConversation.getLastMessage();

        final String username1;
        //如果最后一条消息是发送的消息
        if(lastMessage.direct() == EMMessage.Direct.SEND){
            // 发送的对象 就是当前会话对应的username
            username1 = lastMessage.getTo();
        }else{
            //如果是接收的消息  发送消息的人是 当前会话对应的username
            username1 = lastMessage.getFrom();
        }
        //发送消息的用户的用户名
//        username = lastMessage.getFrom();
//        if(username.equals(EMClient.getInstance().getCurrentUser())){
//            username = lastMessage.getTo();
//        }
        EMTextMessageBody body = (EMTextMessageBody) lastMessage.getBody();
        holder.tv_message.setText(body.getMessage());
        holder.tv_username.setText(username1);
        holder.tv_time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
        int unreadMsgCount = emConversation.getUnreadMsgCount();
        if(unreadMsgCount == 0){
            holder.tv_unreadcount.setVisibility(View.GONE);
        }else if(unreadMsgCount>99){
            holder.tv_unreadcount.setVisibility(View.VISIBLE);
            holder.tv_unreadcount.setText("99");
        }else{
            holder.tv_unreadcount.setVisibility(View.VISIBLE);
            holder.tv_unreadcount.setText(""+unreadMsgCount);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(username1);
                }
            }
        });
    }

    private MyOnclickListener listener;

    public void setListener(MyOnclickListener listener) {
        this.listener = listener;
    }

    public interface MyOnclickListener {
        public void onClick(String username);
    }

    @Override
    public int getItemCount() {
        return conversationList == null ?0:conversationList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_message;
        TextView tv_time;
        TextView tv_username;
        TextView tv_unreadcount;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_unreadcount = (TextView) itemView.findViewById(R.id.tv_unread);

        }
    }
}
