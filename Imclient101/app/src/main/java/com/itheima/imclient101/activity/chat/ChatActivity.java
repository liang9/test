package com.itheima.imclient101.activity.chat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.itheima.imclient101.R;
import com.itheima.imclient101.activity.BaseActivity;
import com.itheima.imclient101.adapter.ChatAdapter;
import com.itheima.imclient101.event.ContactEvent;
import com.itheima.imclient101.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity implements ChatContract.View{

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @InjectView(R.id.rv_chat)
    RecyclerView rvChat;
    @InjectView(R.id.et_message)
    EditText etMessage;
    @InjectView(R.id.btn_send)
    Button btnSend;
    @InjectView(R.id.activity_main)
    LinearLayout activityMain;

    private ChatContract.Presenter presenter;
    private ChatAdapter adapter;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.inject(this);
        username = getIntent().getStringExtra("username");
        tvTitle.setText("与"+ username +"聊天中");
        initToobar();

        rvChat.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(null);
        rvChat.setAdapter(adapter);

        //创建presenter对象
        new ChatPresenter(this);
        presenter.getAllMessage(username);
        presenter.clearUnreadMessage(username);
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               if(s.toString().length()==0){
                   btnSend.setEnabled(false);
               }else{
                   btnSend.setEnabled(true);
               }
            }
        });
    }

    private void initToobar() {
        tbToolbar.setTitle("");
        setSupportActionBar(tbToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.btn_send)
    public void onClick() {
        //获取消息内容
        String content = etMessage.getText().toString().trim();
        //调用presenter方法 发送消息
        presenter.sendMessage(content,username);
        //清空edittext
        etMessage.setText("");
        //收起软键盘
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(rvChat.getWindowToken(),0);
    }

    @Override
    public void onGetAllMessage(List<EMMessage> messages) {
        if(messages != null && messages.size()>0){
            adapter.setEmMessageList(messages);
            adapter.notifyDataSetChanged();
            rvChat.smoothScrollToPosition(messages.size()-1);
        }
    }

    @Override
    public void onSendMessage(boolean isSuccess, String errMsg) {

    }

    @Override
    public void updateChatView() {
       adapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(MessageEvent event){
        //收到别人发来的消息
        presenter.getAllMessage(username);
    }


}
