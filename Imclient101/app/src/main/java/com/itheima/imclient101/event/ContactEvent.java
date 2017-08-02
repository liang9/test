package com.itheima.imclient101.event;

/**
 * Created by fullcircle on 2017/7/23.
 */

public class ContactEvent {
    public boolean isAdded;
    public String contactName;

    public ContactEvent(boolean isAdded, String contactName) {
        this.isAdded = isAdded;
        this.contactName = contactName;
    }
}
