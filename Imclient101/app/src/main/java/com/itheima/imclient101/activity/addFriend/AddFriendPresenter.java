package com.itheima.imclient101.activity.addFriend;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.itheima.imclient101.db.DBUtils;
import com.itheima.imclient101.utils.ThreadUtils;

import java.util.List;

/**
 * Created by fullcircle on 2017/7/23.
 */

public class AddFriendPresenter implements AddFriendContract.Presenter {
    private AddFriendContract.View mView;

    public AddFriendPresenter(AddFriendContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void searchFriend(String keyword) {
        final String currentUser = EMClient.getInstance().getCurrentUser();
        //AVQuery 代表一个查询的对象  构造传入的参数 表名
        AVQuery<AVUser> query = new AVQuery<>("_User");
        // 传入查询的关键字 whereStartsWith 以传入的关键字为开头
        query.whereStartsWith("username",keyword)
              .whereNotEqualTo("username", currentUser)//查询的时候不包含传入的内容
              .findInBackground(new FindCallback<AVUser>() {//开启子线程到leancloud服务端查询
                  @Override
                  public void done(List<AVUser> list, AVException e) {//开启子线程查询
                      if(e == null){
                          if(list != null && list.size()>0){
                              //从数据库中获取好友集合
                              List<String> contacts = DBUtils.initContact(currentUser);
                              mView.onGetSearchResult(list,contacts,null);
                          }else{
                              //查询成功但是没有满足条件的结果
                              mView.onGetSearchResult(null,null,"没有满足条件的结果");
                          }
                      }else{
                          mView.onGetSearchResult(null,null,"查询失败:"+e.getMessage());
                      }
                  }
              });
    }

    @Override
    public void addFriend(final String contactUsername) {
        final String currentUser = EMClient.getInstance().getCurrentUser();
        ThreadUtils.RunOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(contactUsername, currentUser+"想添加你为好友");
                    ThreadUtils.RunOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onGetAddFriendResult(true,null);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.RunOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onGetAddFriendResult(false,"添加好友请求发送失败:"+e.getMessage());
                        }
                    });
                }
            }
        });

    }
}
