package com.itheima.imclient101.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.itheima.imclient101.R;
import com.itheima.imclient101.utils.Utils;

import java.util.List;

/**
 * Created by fullcircle on 2017/7/3.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHoder> {
    public List<String> getContact() {
        return contact;
    }

    public void setContact(List<String> contact) {
        this.contact = contact;
    }

    List<String> contact = null;

    public ContactAdapter(List<String> contact) {
        this.contact = contact;
    }

    @Override
    public MyViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.list_contact_item, null);
        return new MyViewHoder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHoder holder, final int position) {
        String current = contact.get(position);
        holder.tv_contact.setText(current);
        if(position == 0){
            //如果是第一个条目 一定要展示 首字母的布局
            holder.tv_section.setText(Utils.getFirstChar(current));
        }else{
            //如果不是第一个条目 跟之前的条目的首字母比较
            String last = contact.get(position - 1);
            //如果当前条目和上一个条目的首字母相同 那么 不用展示
            if(Utils.getFirstChar(last).equals(Utils.getFirstChar(current))){
                holder.tv_section.setVisibility(View.GONE);
            }else{
                //如果当前条目和上一个条目的首字母不同 那么 展示出显示首字母的布局
                holder.tv_section.setVisibility(View.VISIBLE);
                holder.tv_section.setText(Utils.getFirstChar(current));
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(contact.get(position));
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(listener!=null){
                    listener.onLongClick(contact.get(position));
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return contact == null ? 0: contact.size();
    }

    static class MyViewHoder extends RecyclerView.ViewHolder{
        private TextView tv_section;
        private TextView tv_contact;

        public MyViewHoder(View itemView) {
            super(itemView);
            tv_contact = (TextView) itemView.findViewById(R.id.tv_contact);
            tv_section = (TextView) itemView.findViewById(R.id.tv_section);
        }
    }

    private MyClickListener listener;

    public void setListener(MyClickListener listener) {
        this.listener = listener;
    }

    public interface MyClickListener{
        void onClick(String username);
        void onLongClick(String username);
    }
}
