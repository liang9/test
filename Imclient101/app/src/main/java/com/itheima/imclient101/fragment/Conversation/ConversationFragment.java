package com.itheima.imclient101.fragment.Conversation;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMConversation;
import com.itheima.imclient101.R;
import com.itheima.imclient101.activity.MainActivity;
import com.itheima.imclient101.activity.chat.ChatActivity;
import com.itheima.imclient101.adapter.ConversationAdapter;
import com.itheima.imclient101.event.MessageEvent;
import com.itheima.imclient101.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends BaseFragment implements ConversationContract.View {
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    private ConversationContract.Presenter mPresenter;
    private ConversationAdapter adapter;

    public ConversationFragment() {
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        ButterKnife.inject(this, view);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ConversationAdapter(null);
        adapter.setListener(new ConversationAdapter.MyOnclickListener() {
            @Override
            public void onClick(String username) {
                Intent intent =new Intent(getContext(), ChatActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                mPresenter.clearUnreadMessage(username);
            }
        });
        recyclerview.setAdapter(adapter);
        new ConversationPresenter(this);
       // mPresenter.getAllConversation();
        return view;
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onGetAllConversation(List<EMConversation> conversationList) {
        if(conversationList!= null){
            adapter.setConversationList(conversationList);
            adapter.notifyDataSetChanged();
        }
        MainActivity activity = (MainActivity) getActivity();
        activity.setMessageCount(mPresenter.getALLUnreadMessaegCound());
    }

    @Override
    public void setPresenter(ConversationContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getAllConversation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        mPresenter.clearAllUnreadMessaeg();
        mPresenter.getAllConversation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(MessageEvent event){
        //收到别人发来的消息
        mPresenter.getAllConversation();
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
