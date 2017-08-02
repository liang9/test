package com.itheima.imclient101.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.hyphenate.chat.EMClient;
import com.itheima.imclient101.R;
import com.itheima.imclient101.activity.addFriend.AddFriendActivity;
import com.itheima.imclient101.fragment.BaseFragment;
import com.itheima.imclient101.utils.FragmentFactory;
import com.itheima.imclient101.utils.ToastUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @InjectView(R.id.fl_container)
    FrameLayout flContainer;
    @InjectView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;
    @InjectView(R.id.activity_main)
    LinearLayout activityMain;

    String [] titles = {"消息","联系人","动态"};
    private TextBadgeItem badgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initToolbar();
        initBottomBar();

        clearCacheFramgents();

        initFirstFragment();
    }

    private void clearCacheFramgents() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments!=null && fragments.size()>0){

            for(Fragment fragment:fragments){
                FragmentTransaction transaction = fragmentManager.beginTransaction();
               transaction.remove(fragment);
                transaction.commit();
            }
        }

    }

    private void initFirstFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BaseFragment fragment = FragmentFactory.getFragment(0);
        transaction.add(R.id.fl_container,fragment);
        transaction.commit();
    }

    private void initBottomBar() {
        BottomNavigationItem msgItem = new BottomNavigationItem(R.mipmap.conversation_selected_2, "消息");
       // msgItem.setActiveColor()可以给每个条目设置单独的选中颜色

        //底部导航 条目右上角的气泡 BadgeItem
        badgeItem = new TextBadgeItem();
        //创建气泡对象之后 设置到消息的条目上
        msgItem.setBadgeItem(badgeItem);
        //先隐藏起来气泡
        badgeItem.hide();
        setMessageCount(EMClient.getInstance().chatManager().getUnreadMessageCount());


        BottomNavigationItem contactItem = new BottomNavigationItem(R.mipmap.contact_selected_2, "联系人");
        BottomNavigationItem pluginItem = new BottomNavigationItem(R.mipmap.plugin_selected_2, "动态");

        //给所有的条目 设置统一的选中颜色
        bottomNavigationBar.setActiveColor(R.color.btn_normal);
        bottomNavigationBar.addItem(msgItem)
                            .addItem(contactItem)
                            .addItem(pluginItem)
                            .setFirstSelectedPosition(0)
                            .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                //tab被选中 就显示对应的fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                BaseFragment fragment = FragmentFactory.getFragment(position);
                if(fragment.isAdded()){
                    //如果返回true 说明这个fragment已经添加过了 直接显示就可以了
                    transaction.show(fragment);
                }else{
                    transaction.add(R.id.fl_container,fragment);
                }
                transaction.commit();
                tvTitle.setText(titles[position]);
            }

            @Override
            public void onTabUnselected(int position) {
                // 刚刚还是被选中的状态 现在没被选中 这个fragment应该被隐藏起来
                Log.e("MainActivity","onTabUnselected position"+position);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                BaseFragment fragment = FragmentFactory.getFragment(position);
                transaction.hide(fragment);
                transaction.commit();
            }

            @Override
            public void onTabReselected(int position) {
                Log.e("MainActivity","onTabReselected position"+position);

            }
        });


    }

    private void initToolbar() {
        tbToolbar.setTitle("");
        setSupportActionBar(tbToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        MenuBuilder builder = (MenuBuilder) menu;
        //设置菜单的Icon图标可见
        builder.setOptionalIconsVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.menu_about:
                ToastUtils.showToast(getApplicationContext(),"关于");
                break;
            case R.id.menu_addFriend:
                //ToastUtils.showToast(getApplicationContext(),"添加好友");
                Intent intent = new Intent(getApplicationContext(), AddFriendActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_scan:
                ToastUtils.showToast(getApplicationContext(),"扫一扫");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setMessageCount(int count){
        if(count == 0){
            //没有未读消息隐藏气泡
            badgeItem.hide();
        }else if(count>99){
            //有未读消息的时候显示出来
            badgeItem.show();
            badgeItem.setText("99");
        }else{
            //有未读消息的时候显示出来
            badgeItem.show();
            badgeItem.setText(""+count);
        }
    }
}
