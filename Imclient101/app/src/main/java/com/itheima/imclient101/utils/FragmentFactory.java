package com.itheima.imclient101.utils;

import com.itheima.imclient101.fragment.BaseFragment;
import com.itheima.imclient101.fragment.contract.ContactFragment;
import com.itheima.imclient101.fragment.Conversation.ConversationFragment;
import com.itheima.imclient101.fragment.Plugin.PluginFragment;

/**
 * Created by fullcircle on 2017/7/22.
 */

public class FragmentFactory {
    public static ContactFragment contactFragment = null;
    public static ConversationFragment conversationFragment = null;
    public static PluginFragment pluginFragment = null;

    public static BaseFragment getFragment(int position){
        switch (position){
            case 0:
                if(conversationFragment == null){
                    conversationFragment = new ConversationFragment();
                }
                return  conversationFragment;
            case 1:
                if(contactFragment == null){
                    contactFragment = new ContactFragment();
                }
                return  contactFragment;
            case 2:
                if(pluginFragment == null){
                    pluginFragment = new PluginFragment();
                }
                return pluginFragment;
        }
        return null;
    }
}
