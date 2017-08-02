package com.itheima.imclient101.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.itheima.imclient101.R;

import java.util.Date;
import java.util.List;

/**
 * Created by fullcircle on 2017/7/24.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    public void setEmMessageList(List<EMMessage> emMessageList) {
        this.emMessageList = emMessageList;
    }

    private List<EMMessage> emMessageList;

    public ChatAdapter(List<EMMessage> emMessageList) {
        this.emMessageList = emMessageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if(viewType == 0){
           itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_item_send,parent,false);
        }else if (viewType == 1){
            itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_item,parent,false);

        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //获取消息的对象
        EMMessage message = emMessageList.get(position);
        //获取消息的内容 转换成EMTextMessageBody
        EMTextMessageBody body = (EMTextMessageBody) message.getBody();
        //提取消息的文字内容
        String message1 = body.getMessage();
        //通过textview展示文字内容
        holder.tv_message.setText(message1);
        holder.tv_time.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
        if(position ==0){
            //如果是第1条消息 与当前的系统时间比较 如果消息发送/收到的时间和当前时间足够近就不需要展示
            //显示消息时间的textView
            if(DateUtils.isCloseEnough(message.getMsgTime(),System.currentTimeMillis())){
                holder.tv_time.setVisibility(View.GONE);
            }else{
                holder.tv_time.setVisibility(View.VISIBLE);
            }
        }else{
            //如果不是第一条 就需要跟当前消息的上一条消息比较
            if(DateUtils.isCloseEnough(message.getMsgTime(),emMessageList.get(position-1).getMsgTime())){
                holder.tv_time.setVisibility(View.GONE);
            }else{
                holder.tv_time.setVisibility(View.VISIBLE);
            }

        }

        if(message.direct() == EMMessage.Direct.SEND){
            switch (message.status()){
                case SUCCESS:
                    //如果成功了 隐藏显示状态的图标
                    holder.iv_state.setVisibility(View.GONE);
                    break;
                case FAIL:
                    //如果失败了 显示发送状态图标为失败
                    holder.iv_state.setVisibility(View.VISIBLE);
                    holder.iv_state.setImageResource(R.mipmap.msg_error);
                    break;
                case INPROGRESS:
                    //如果是正在发送 展示旋转的帧动画
                    holder.iv_state.setVisibility(View.VISIBLE);
                    holder.iv_state.setImageResource(R.drawable.send_animation);
                    AnimationDrawable drawable = (AnimationDrawable) holder.iv_state.getDrawable();
                    drawable.start();
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return emMessageList == null ? 0:emMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage message = emMessageList.get(position);
        EMMessage.Direct direct = message.direct();
        return direct == EMMessage.Direct.SEND ? 0:1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_time;
        private TextView tv_message;
        private ImageView iv_state;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            iv_state = (ImageView) itemView.findViewById(R.id.iv_state);
        }
    }
}
