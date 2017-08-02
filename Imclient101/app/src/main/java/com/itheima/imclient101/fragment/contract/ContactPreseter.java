package com.itheima.imclient101.fragment.contract;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.DateUtils;
import com.itheima.imclient101.db.DBUtils;
import com.itheima.imclient101.utils.ThreadUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by fullcircle on 2017/7/23.
 */

public class ContactPreseter implements ContactContract.Presenter {
    private ContactContract.View mView;

    public ContactPreseter(ContactContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void initContact(String username) {
        //先从数据库中查询联系人
        List<String> contacts = DBUtils.initContact(username);
        //通知界面更新数据
        mView.onInitContact(contacts);
        //联网更新联系人
        updateContact(username);
    }

    @Override
    public void updateContact(final String username) {
        ThreadUtils.RunOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //排序联系人的集合
                    Collections.sort(usernames, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.compareTo(o2);
                        }
                    });
                    //更新联系人集合到数据库中
                    DBUtils.updateContactsDB(usernames,username);
                    //通知界面更新数据
                    ThreadUtils.RunOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onUpdateContact(usernames,null);
                        }
                    });


                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.RunOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onUpdateContact(null,e.getMessage());
                        }
                    });

                }
            }
        });

    }

    @Override
    public void deleteContact(final String username) {
        ThreadUtils.RunOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(username);
                    ThreadUtils.RunOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onDeleteContact(true,null);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.RunOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onDeleteContact(false,e.getMessage());
                        }
                    });
                }
            }
        });

    }
}
