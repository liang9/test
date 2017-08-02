package com.itheima.imclient101.activity.addFriend;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.itheima.imclient101.R;
import com.itheima.imclient101.activity.BaseActivity;
import com.itheima.imclient101.adapter.AddFriendAdapter;
import com.itheima.imclient101.utils.ToastUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddFriendActivity extends BaseActivity implements AddFriendContract.View {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @InjectView(R.id.iv_nodata)
    ImageView ivNodata;
    @InjectView(R.id.rv_addfriend)
    RecyclerView rvAddfriend;
    @InjectView(R.id.fl_container)
    FrameLayout flContainer;
    @InjectView(R.id.activity_main)
    LinearLayout activityMain;

    private AddFriendAdapter adapter;

    private AddFriendContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.inject(this);
        initToolBar();
        new AddFriendPresenter(this);
    }

    private void initToolBar() {
        tbToolbar.setTitle("");
        setSupportActionBar(tbToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        //通过menu找到菜单条目 一共就一条 传入索引0
        MenuItem item = menu.getItem(0);
        SearchView searchView = (SearchView) item.getActionView();
        //设置弹出搜索框的时候显示的提示文字
        searchView.setQueryHint("输入好友昵称");
        //设置搜索文字的监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //当点击了键盘的回车
                mPresenter.searchFriend(query);
                adapter = new AddFriendAdapter(null,null);
                rvAddfriend.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvAddfriend.setAdapter(adapter);
                adapter.setListener(new AddFriendAdapter.MyOnClickListener() {
                    @Override
                    public void onClick(String username) {
                        mPresenter.addFriend(username);
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //当searchview的输入框文字发生变化的时候
                if(newText != null&& newText.trim().length()>0)
                ToastUtils.showToast(getApplicationContext(),newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onGetSearchResult(List<AVUser> userList, List<String> contacts, String errMsg) {
        if(userList == null){
            ToastUtils.showToast(getApplicationContext(),errMsg);
            ivNodata.setVisibility(View.VISIBLE);
            rvAddfriend.setVisibility(View.GONE);
        }else{
            rvAddfriend.setVisibility(View.VISIBLE);
            adapter.setContact(contacts);
            adapter.setUserList(userList);
            adapter.notifyDataSetChanged();
            ivNodata.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetAddFriendResult(boolean isSuccess, String errMsg) {
        ToastUtils.showToast(getApplicationContext(),isSuccess?"添加好友的请求提交成功":errMsg);
    }

    @Override
    public void setPresenter(AddFriendContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
