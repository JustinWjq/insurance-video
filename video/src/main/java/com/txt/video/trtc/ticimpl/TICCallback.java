package com.txt.video.trtc.ticimpl;

/**
 * Created by JustinWjq
 *
 * @date 2020/8/25.
 * description：
 */

/**
 * ILive通用返回回调
 */
public interface TICCallback<T> {

    /**
     *
     * 操作成功
     * @param data 成功返回值
     */
    void onSuccess(T data);

    /**
     * 操作失败
     * @param module    出错模块
     * @param errCode   错误码
     * @param errMsg    错误描述
     */
    void onError(String module, int errCode, String errMsg);
}