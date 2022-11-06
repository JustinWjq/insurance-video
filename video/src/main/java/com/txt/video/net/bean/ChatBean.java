package com.txt.video.net.bean;


import com.txt.video.common.adapter.base.entity.AbstractExpandableItem;
import com.txt.video.common.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by JustinWjq
 *
 * @date 2020/8/31.
 * description： 群聊类
 */
public class ChatBean  implements MultiItemEntity {

    String name;
    String message;
    int type;
    boolean isSelf;

    public ChatBean(String name, String message, int type, boolean isSelf) {
        this.name = name;
        this.message = message;
        this.type = type;
        this.isSelf = isSelf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
