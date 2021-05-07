package com.txt.video.trtc.ticimpl.utils;

import com.tencent.liteav.basic.log.TXCLog;
import com.txt.video.trtc.ticimpl.TICCallback;

public class CallbackUtil {

    public static void notifySuccess(TICCallback callBack, Object data) {
        if (null != callBack) {
            callBack.onSuccess(data);
        }
    }

    public static void notifyError(TICCallback callBack, String module, int errCode, String errMsg) {
        if (null != callBack) {
            callBack.onError(module, errCode, errMsg);
        }
        TXCLog.e(module, errMsg);
    }
}
