package com.txt.video.net.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author ：Justin
 * time ：2021/3/29.
 * des ：
 */
public class RoomParamsBean implements Parcelable {

    /**
     * serviceId : 60618f0659f239068d3c1598
     * sdkAppId : 1400438933
     * roomId : 49722992
     * userId : wjqdev123
     * userRole : owner
     * groupId : 49722992
     * extraData : {}
     * userSig : eJwtzE0LgkAUheH-MuuQO19mQpta1EIyLWotzC1ujTGZXIPov2fq8jwH3o84ZoeIsRGpUBGI2bDJ4aOlCw3c3Z4OWSo9nS93r0IgJ1JpAIxOFlqPT0s19hrLOUCsrRkV34Gav1trFQBMFbr27bzLZQZ6a2psixMzr9knLsPaq5XnvbG76lxWm7KQxVJ8f*HYMmM_
     * extraUserId : wjqdev123extra
     * extraUserSig : eJwtzcEOgjAQBNB-6dmQLaWIJJ6IMUE56Ykb2oUsBYNQCmr8dxE47pvJ7IddzxfHYstC5jrANvNNCh*Gcpp5KJ8KLXcFjqbN1kandNY0pFjIPQBPBDshlsRQjZP6fAvgC*ktimND7d*llC4ArCtUTA-e2k*7MtW672OeWPmqDsmg8BjE0XCTRZEZntv6pKv4vmffH2IzNeQ_
     */

    private String serviceId;
    private int sdkAppId;
    private int roomId;
    private String userId;
    private String userRole;
    private String groupId;
    private String userSig;
    private String extraUserId;
    private String extraUserSig;
    /**
     * inviteNumber : 57308044
     * maxRoomUser : 9
     * maxRoomTime : 120
     */

    private String inviteNumber;
    private int maxRoomUser;
    private int maxRoomTime;
    /**
     * angetName : 123
     */

    private String agentName;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getSdkAppId() {
        return sdkAppId;
    }

    public void setSdkAppId(int sdkAppId) {
        this.sdkAppId = sdkAppId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public String getExtraUserId() {
        return extraUserId;
    }

    public void setExtraUserId(String extraUserId) {
        this.extraUserId = extraUserId;
    }

    public String getExtraUserSig() {
        return extraUserSig;
    }

    public void setExtraUserSig(String extraUserSig) {
        this.extraUserSig = extraUserSig;
    }


    public RoomParamsBean() {
    }

    public String getInviteNumber() {
        return inviteNumber;
    }

    public void setInviteNumber(String inviteNumber) {
        this.inviteNumber = inviteNumber;
    }

    public int getMaxRoomUser() {
        return maxRoomUser;
    }

    public void setMaxRoomUser(int maxRoomUser) {
        this.maxRoomUser = maxRoomUser;
    }

    public int getMaxRoomTime() {
        return maxRoomTime;
    }

    public void setMaxRoomTime(int maxRoomTime) {
        this.maxRoomTime = maxRoomTime;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.serviceId);
        dest.writeInt(this.sdkAppId);
        dest.writeInt(this.roomId);
        dest.writeString(this.userId);
        dest.writeString(this.userRole);
        dest.writeString(this.groupId);
        dest.writeString(this.userSig);
        dest.writeString(this.extraUserId);
        dest.writeString(this.extraUserSig);
        dest.writeString(this.inviteNumber);
        dest.writeInt(this.maxRoomUser);
        dest.writeInt(this.maxRoomTime);
        dest.writeString(this.agentName);
    }

    protected RoomParamsBean(Parcel in) {
        this.serviceId = in.readString();
        this.sdkAppId = in.readInt();
        this.roomId = in.readInt();
        this.userId = in.readString();
        this.userRole = in.readString();
        this.groupId = in.readString();
        this.userSig = in.readString();
        this.extraUserId = in.readString();
        this.extraUserSig = in.readString();
        this.inviteNumber = in.readString();
        this.maxRoomUser = in.readInt();
        this.maxRoomTime = in.readInt();
        this.agentName = in.readString();
    }

    public static final Creator<RoomParamsBean> CREATOR = new Creator<RoomParamsBean>() {
        @Override
        public RoomParamsBean createFromParcel(Parcel source) {
            return new RoomParamsBean(source);
        }

        @Override
        public RoomParamsBean[] newArray(int size) {
            return new RoomParamsBean[size];
        }
    };
}
