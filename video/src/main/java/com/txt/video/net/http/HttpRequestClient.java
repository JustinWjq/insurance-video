package com.txt.video.net.http;


import com.txt.video.net.exception.CustomException;
import com.txt.video.net.interceptor.ExMultipartBody;
import com.txt.video.net.interceptor.NetInterceptor;
import com.txt.video.net.utils.TxLogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by pc on 2017/3/21.
 */
public class HttpRequestClient {
    private static final String TAG = HttpRequestClient.class.getSimpleName();
    public OkHttpClient mOkhttpClient;
    private static HttpRequestClient mInstance;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public HttpRequestClient() {
        SSLContext sslContext = null;

        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null,new TrustManager[]{xtm} , new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        mOkhttpClient = new OkHttpClient().newBuilder()
                .sslSocketFactory(sslContext.getSocketFactory(),xtm)
                .hostnameVerifier(DO_NOT_VERIFY)
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES)
                .build();

    }

    public static HttpRequestClient getIntance() {
        if (mInstance == null) {
            synchronized (HttpRequestClient.class) {
                if (mInstance == null)
                    mInstance = new HttpRequestClient();
            }
        }
        return mInstance;
    }

    public void cancleClient() {
        if (postFileCall != null)
            postFileCall.cancel();
    }

    public interface UploadProgressListener {
        void onProgress(long totalLength, long currentLength);
    }

    private Call postFileCall;

    public void postFile(final String uri, final Map<String, String> map, final File file, final RequestHttpCallBack callBack, final UploadProgressListener callback) {
        TxLogUtils.d(TAG+"----postFile: uri" + uri);
        TxLogUtils.d(TAG+"----postFile: map" + map);

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        OkHttpClient mOkhttpClient1 = new OkHttpClient().newBuilder()
                .connectTimeout(100, TimeUnit.MINUTES)
                .readTimeout(100, TimeUnit.MINUTES)
                .writeTimeout(100, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(new NetInterceptor())
                .build();

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("file/*"), file);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            try {
                String encode = URLEncoder.encode(file.getName(), "UTF-8");
                requestBody.addFormDataPart("file", encode, body);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }


        ExMultipartBody exMultipartBody = new ExMultipartBody(requestBody.build(), new ExMultipartBody.UploadProgressListener() {
            @Override
            public void onProgress(long totalLength, long currentLength) {
                callback.onProgress(totalLength, currentLength);
            }
        });

        Request request = new Request.Builder().url(uri).post(exMultipartBody).build();

        postFileCall = mOkhttpClient1.newCall(request);

        postFileCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                TxLogUtils.i( "onFailure: err" + e.getMessage() + "errCode" + e.hashCode());
                if (callBack != null) {
                    callBack.onFail(e.getMessage(), e.hashCode());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                TxLogUtils.i( "onResponse: " + result);
                if (response.isSuccessful()) {


                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("errCode")) {
                            int errCode = jsonObject.getInt("errCode");
                            if (errCode==0) {
                                if (callBack != null) {
                                    callBack.onSuccess("上传成功！！！");
                                }
                            } else {
                                if (callBack != null) {
                                    String errInfo = jsonObject.getString("errInfo");
                                    callBack.onFail(errInfo, errCode);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (callBack != null) {
                            callBack.onFail("request Fail", -1);
                        }
                    }

                } else {
                    if (callBack != null) {
                        callBack.onFail("request Fail", -1);
                    }
                }

            }
        });


    }

    //get请求
    public void get(String url, String header, final RequestHttpCallBack callBack) {
        TxLogUtils.i( "get: url" + url);
        Request mRequest;
//        if (TextUtils.isEmpty(header)) {
////            mRequest = new Request.Builder().url(url).addHeader("App-Version", BuildConfig.VERSION_NAME).build();
//        } else {
//
//
//        }
        mRequest = new Request.Builder().url(url).addHeader("access-token", header).build();
        Call call = mOkhttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                TxLogUtils.i( "onFailure: err" + e.getMessage() + "errCode" + e.hashCode());
                if (callBack != null) {

                    callBack.onFail(e.getMessage(), CustomException.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                TxLogUtils.i( result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (response.isSuccessful()) {
                        if (jsonObject.getString("errCode").equals("0")) {
                            JSONObject result1 = jsonObject.optJSONObject("result");
                            if (result1 == null) {
                                callBack.onSuccess("");
                            } else {
                                callBack.onSuccess(result1.toString());
                            }

                        } else {
                            String errCode = jsonObject.getString("errCode");
                            String errInfo = jsonObject.getString("errInfo");
                            callBack.onFail(errInfo, Integer.parseInt(errCode));
                        }
                    } else {
                        String message = jsonObject.getString("message");
                        callBack.onFail(message, 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    TxLogUtils.i( e.getMessage());
                    callBack.onFail(e.getMessage(), 0);
                }

            }
        });
    }


    //get请求
    public void get(String url, final RequestHttpCallBack callBack) {
        TxLogUtils.i("get: url" + url);
        CacheControl build = new CacheControl.Builder().noCache().build();
        Request request = new Request.
                Builder().
                cacheControl(build).
                url(url).
                build();
        Call call = mOkhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                TxLogUtils.d(TAG+"----onFailure: err" + e.getMessage() + "errCode" + e.hashCode());

                if (callBack != null) {
                    callBack.onFail(e.getMessage(), e.hashCode());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                TxLogUtils.i("get: url" + "-----onResponse: result" + result);
                String errCode = null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("errCode")) {
                        errCode = jsonObject.getString("errCode");
                        if (errCode != null) {
                            if (errCode.equals("0")) {
                                if (callBack != null) {
                                    callBack.onSuccess(new JSONObject(result).getString("result"));
                                }
                            } else {
                                if (callBack != null) {
                                    callBack.onFail(new JSONObject(result).getString("errInfo"), Integer.parseInt(errCode));
                                }
                            }
                        } else {
                            if (callBack != null) {
                                callBack.onFail("fail", -1);
                            }
                        }
                    } else {
                        callBack.onSuccess(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void put(String url, String json, String aecsssToken, final RequestHttpCallBack callBack) {
        TxLogUtils.d( "put: url" + url);
        TxLogUtils.d( "put: json" + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = null;
        if (!aecsssToken.equals("")) {
            request = new Request.Builder()
                    .url(url).header("access-token", aecsssToken).addHeader("token", aecsssToken)
                    .put(body)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
        }
        TxLogUtils.d( "post: request.toString()" + request.toString());
        Call call = mOkhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callBack != null) {
                    callBack.onFail(e.getMessage(), e.hashCode());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                TxLogUtils.d( "onResponse: result" + result);
                String errCode = null;
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    errCode = jsonObject.getString("errCode");
                    if (errCode != null && errCode.equals("0")) {
                        if (callBack != null) {
                            if (jsonObject.has("result")) {
                                callBack.onSuccess(jsonObject.getString("result"));
                            } else {
                                callBack.onSuccess("");
                            }
                        }
                    } else {
                        if (callBack != null) {
                            callBack.onFail(new JSONObject(result).getString("errInfo"), Integer.parseInt(errCode));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //post请求
    public void post(String url, String json, String aecsssToken, final RequestHttpCallBack callBack) {
        TxLogUtils.i("post: url" + url);
        TxLogUtils.i( "post: json" + json);
        try {
            RequestBody body = RequestBody.create(JSON, json);
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .post(body);
            Request request;
            if (!aecsssToken.equals("")) {
                request = requestBuilder.addHeader("access-token", aecsssToken)
                        .build();
            } else {
                request = requestBuilder.post(body)
                        .build();
            }
            TxLogUtils.i( "post: request.toString()" + request.toString());
            Call call = mOkhttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    TxLogUtils.i( "onFailure: errCode" + e.hashCode() + "  e.getMessage()" + e.getMessage());
                    if (callBack != null) {
                        callBack.onFail(e.getMessage(), e.hashCode());
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    TxLogUtils.i( "onResponse: result" + result);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (response.isSuccessful()) {
                            if (jsonObject.getString("errCode").equals("0")) {
                                JSONObject result1 = jsonObject.optJSONObject("result");
                                if (result1 == null) {
                                    callBack.onSuccess("");
                                } else {
                                    callBack.onSuccess(result1.toString());
                                }

                            } else {
                                String errCode = jsonObject.getString("errCode");
                                String errInfo = jsonObject.getString("errInfo");
                                callBack.onFail(errInfo, Integer.parseInt(errCode));
                            }
                        } else {
                            String message = jsonObject.getString("message");
                            callBack.onFail(message, 0);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        callBack.onFail(e.getMessage(), 0);
                    }
                }
            });
        }catch (Exception e){
            callBack.onFail(e.getMessage(), 0);
        }

    }


    //delete请求
    public void delete(String url, String json, String aecsssToken, final RequestHttpCallBack callBack) {
        TxLogUtils.i( "post: url" + url);
        TxLogUtils.i( "post: json" + json);
        try {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = null;
            if (!aecsssToken.equals("")) {
                request = new Request.Builder()
                        .url(url)
                        .addHeader("access-token", aecsssToken)
                        .delete(body)
                        .build();
            } else {
                request = new Request.Builder()
                        .url(url)
                        .delete(body)
                        .build();
            }
            TxLogUtils.i( "post: request.toString()" + request.toString());
            Call call = mOkhttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    TxLogUtils.i( "onFailure: errCode" + e.hashCode() + "  e.getMessage()" + e.getMessage());
                    if (callBack != null) {
                        callBack.onFail(e.getMessage(), e.hashCode());
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    TxLogUtils.i( "onResponse: result" + result);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (response.isSuccessful()) {
                            if (jsonObject.getString("errCode").equals("0")) {
                                JSONObject result1 = jsonObject.optJSONObject("result");
                                if (result1 == null) {
                                    callBack.onSuccess("");
                                } else {
                                    callBack.onSuccess(result1.toString());
                                }

                            } else {
                                String errCode = jsonObject.getString("errCode");
                                String errInfo = jsonObject.getString("errInfo");
                                callBack.onFail(errInfo, Integer.parseInt(errCode));
                            }
                        } else {
                            String message = jsonObject.getString("message");
                            callBack.onFail(message, 0);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        callBack.onFail(e.getMessage(), 0);
                    }
                }
            });
        }catch (Exception e){
            callBack.onFail(e.getMessage(), 0);
        }

    }

    public interface RequestHttpCallBack {
        public void onSuccess(String json);

        public void onFail(String err, int code);
    }

    X509TrustManager xtm = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            X509Certificate[] x509Certificates = new X509Certificate[0];
            return x509Certificates;
        }
    };

}
