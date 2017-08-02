package com.itheima.imclient101.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.itheima.imclient101.R;
import com.itheima.imclient101.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fullcircle on 2017/7/23.
 */

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.MyViewHolder> {
    private List<AVUser> userList;
    private List<String> contact = new ArrayList<>();

    public AddFriendAdapter(List<AVUser> userList, List<String> contact) {
        this.userList = userList;
        this.contact = contact;
    }

    public void setUserList(List<AVUser> userList) {
        this.userList = userList;
    }

    public void setContact(List<String> contact) {
        this.contact = contact;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_addfriend, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AVUser user = userList.get(position);
        final String username = user.getUsername();
        String date = Utils.getDate(user.getCreatedAt());
        holder.tv_username.setText(username);
        holder.tv_createDate.setText(date);

        if(contact.contains(username)){
            //如果好友的列表中包含了查询出的内容 说明 这个用户已经是好友了
            holder.btn_add.setText("已经是好友");
            holder.btn_add.setEnabled(false);
        }else{
            holder.btn_add.setText("添加好友");
            holder.btn_add.setEnabled(true);
        }
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(username);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList == null? 0:userList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_username;
        TextView tv_createDate;
        Button btn_add;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_createDate = (TextView) itemView.findViewById(R.id.tv_regist_time);
            btn_add = (Button) itemView.findViewById(R.id.btn_add);
        }
    }

    private MyOnClickListener listener;

    public void setListener(MyOnClickListener listener) {
        this.listener = listener;
    }

    public interface MyOnClickListener{
        void onClick(String username);
    }
}
