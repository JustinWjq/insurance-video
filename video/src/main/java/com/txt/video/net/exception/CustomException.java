package com.txt.video.net.exception;

//import com.google.gson.JsonParseException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


/**
 *
 * 本地异常处理。包括解析异常等其他异常
 */

public class CustomException {

    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;

    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;

    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 1002;

    /**
     * 协议错误
     */
    public static final int HTTP_ERROR = 1003;

    public static ApiException handleException(Throwable e) {
        ApiException ex;
//        if (e instanceof JsonParseException
//                || e instanceof JSONException
//                || e instanceof ParseException) {
//            //解析错误
//            ex = new ApiException(PARSE_ERROR, "解析错误");
//            return ex;
//        } else
        if (e instanceof ConnectException) {
            //网络错误
            ex = new ApiException(NETWORK_ERROR, "网络断开");
            return ex;
        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
            //连接错误
            ex = new ApiException(NETWORK_ERROR, "网络异常");
            return ex;
        }else {
            //未知错误
            ex = new ApiException(UNKNOWN, "未知错误");
            return ex;
        }
    }
}
