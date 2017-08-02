package com.itheima.imclient101.activity.addFriend;

import com.avos.avoscloud.AVUser;
import com.itheima.imclient101.BaseView;

import java.util.List;

/**
 * Created by fullcircle on 2017/7/23.
 */

public interface AddFriendContract {
    interface Presenter{
        void searchFriend(String keyword);
        void addFriend(String contactUsername);
    }
    interface View extends BaseView<Presenter>{
        void onGetSearchResult(List<AVUser> userList,List<String> contacts,String errMsg);
        void onGetAddFriendResult(boolean isSuccess, String errMsg);
    }
}
