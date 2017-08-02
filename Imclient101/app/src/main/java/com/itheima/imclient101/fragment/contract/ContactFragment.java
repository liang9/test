package com.itheima.imclient101.fragment.contract;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.imclient101.R;
import com.itheima.imclient101.activity.chat.ChatActivity;
import com.itheima.imclient101.adapter.ContactAdapter;
import com.itheima.imclient101.event.ContactEvent;
import com.itheima.imclient101.fragment.BaseFragment;
import com.itheima.imclient101.utils.SPUtils;
import com.itheima.imclient101.utils.ToastUtils;
import com.itheima.imclient101.widget.ContactLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment implements ContactContract.View{

    private ContactContract.Presenter mPreseter;
    private ContactLayout contactLayout;
    private ContactAdapter adapter;
    private String username;

    public ContactFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        contactLayout = (ContactLayout) view.findViewById(R.id.contact);
        new ContactPreseter(this);
        username = SPUtils.getString(getContext(), "username");
        mPreseter.initContact(username);
    }

    @Override
    public void onInitContact(List<String> contact) {
        adapter = new ContactAdapter(contact);
        contactLayout.setAdapter(adapter);
        contactLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPreseter.updateContact(username);
            }
        });
        adapter.setListener(new ContactAdapter.MyClickListener() {
            @Override
            public void onClick(String username) {
                //跳转到聊天界面
                Intent intent = new Intent(getContext(),ChatActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }

            @Override
            public void onLongClick(final String username) {
                //弹出SnackBar 删除好友
                Snackbar.make(contactLayout,"真的要删除"+username+"吗?",Snackbar.LENGTH_SHORT)
                        .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPreseter.deleteContact(username);
                    }
                }).show();
            }
        });
    }

    @Override
    public void onUpdateContact(List<String> contact, String errorMsg) {
        contactLayout.setRefreshing(false);
        if(contact == null){
            ToastUtils.showToast(getContext(),errorMsg);
        }else{
            adapter.setContact(contact);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDeleteContact(boolean isSuccess, String errMsg) {
        if(isSuccess){
            ToastUtils.showToast(getContext(),"删除成功");
        }else{
            ToastUtils.showToast(getContext(),"删除失败"+errMsg);
        }
    }

    @Override
    public void setPresenter(ContactContract.Presenter presenter) {
        this.mPreseter = presenter;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void contactChange(ContactEvent event){
        //收到联系人变化的消息
        mPreseter.updateContact(username);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
