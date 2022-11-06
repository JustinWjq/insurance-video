package com.txt.video.ui.video.barrage.view.adapter;

import com.txt.video.common.adapter.base.entity.MultiItemEntity;

public class TUIBarrageMsgEntity implements MultiItemEntity {

    public String userId;
    public String userName;
    public String content;
    public int type;
    public int color;
    boolean isSelf;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    @Override
    public int getItemType() {
        return isSelf ? 1 : 0;
    }
}
