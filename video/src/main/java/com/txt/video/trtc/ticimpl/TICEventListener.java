package com.txt.video.trtc.ticimpl;

import java.util.List;

/**
 * Created by JustinWjq
 *
 * @date 2020/8/25.
 * description：
 */
//TIC 事件回调
public interface TICEventListener {
    void onTICUserVideoAvailable(final String userId, boolean available);
    void onTICUserSubStreamAvailable(final String userId, boolean available);
    void onTICUserAudioAvailable(final String userId, boolean available);

    void onTICMemberJoin(List<String> userList);
    void onTICMemberQuit(List<String> userList);

    void onTICVideoDisconnect(int errCode, String errMsg);
    void onTICClassroomDestroy();

    /**
     * 发送离线录制对时信息通知
     * @param code				错误码;0表示成功，其他值为失败;
     * @param desc				错误信息;
     * @note 进房成功后,TIC会自动发送离线录制需要的对时信息;只有成功发送对时信息的课堂才能进行课后离线录制; 注: 可能在子线程中执行此回调;
     */
    void onTICSendOfflineRecordInfo(int code, final String desc);
}
