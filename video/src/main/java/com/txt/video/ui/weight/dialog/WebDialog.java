package com.txt.video.ui.weight.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
//import android.webkit.CookieManager;
//import android.webkit.CookieSyncManager;
//import android.webkit.GeolocationPermissions;
//import android.webkit.PermissionRequest;
//import android.webkit.WebChromeClient;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
//
//import com.google.gson.JsonObject;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.txt.video.R;
import com.txt.video.TXSdk;
import com.txt.video.common.callback.onShareWhiteBroadDialogListener;
import com.txt.video.common.utils.MainThreadUtil;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.trtc.ConfigHelper;
import com.txt.video.trtc.feature.AudioConfig;
import com.txt.video.trtc.videolayout.Utils;
import com.txt.video.ui.video.VideoActivity;
import com.txt.video.ui.weight.easyfloat.utils.DisplayUtils;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by justin on 2017/8/25.
 */
public class WebDialog extends Dialog implements View.OnClickListener {
    private onShareWhiteBroadDialogListener mListener;
    private Context mContext;
    private boolean isShare = false;

    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
    }


    private String mUrl = "";
    private String mCookie;
    private VideoActivity activity;

    public WebDialog(Context context,VideoActivity activity, String url, String cookie) {
        super(context, R.style.tx_MyDialog);
        mContext = context;
        this.activity = activity;
        this.mUrl = url;
        this.mCookie = cookie;


    }

    public void setOnShareWhiteBroadDialogListener(onShareWhiteBroadDialogListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_dialog_web);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.width = DisplayUtils.INSTANCE.getScreenWidth(mContext);
        window.setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        injectCookie(mCookie);
        initView();
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    TxLogUtils.i("onCancel");
                    return true;
                }
                return false;
            }
        });
    }

    public void changeUi(int width, int heigh) {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.width = DisplayUtils.INSTANCE.getScreenWidth(mContext);
        window.setAttributes(attributes);
//        Window window = getWindow();
//        WindowManager.LayoutParams attributes = window.getAttributes();
//        attributes.height = DisplayUtils.INSTANCE.getScreenHeight(mContext);
//        attributes.width = DisplayUtils.INSTANCE.getScreenWidth(mContext);
    }

    WebView webView;
    ImageButton tx_audio;
    TextView iv_close;
    TextView tv_endshare;
    //    TextView tv_title;
    CookieManager mCookieManager;

    public void injectCookie(String cookie) {
        this.mCookie = cookie;
        webView = findViewById(R.id.webView);
//        CookieManager.setAcceptFileSchemeCookies(true);
        mCookieManager = CookieManager.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //这个代码是清除webview里的所有cookie
            mCookieManager.removeAllCookies(null);
        } else {
            CookieSyncManager.createInstance(mContext);
            mCookieManager.removeAllCookie();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCookieManager.setAcceptThirdPartyCookies(webView, false);
        }
        mCookieManager.setAcceptCookie(true);
        mCookieManager.acceptCookie();
        setCookie(mCookie);
    }

    private void initView() {


        tv_endshare = findViewById(R.id.tv_endshare);
        iv_close = findViewById(R.id.iv_close);
        tx_audio = findViewById(R.id.tx_audio);
//        tv_title = findViewById(R.id.tv_title);
        tv_endshare.setOnClickListener(this);
        tx_audio.setOnClickListener(this);

        tv_endshare.setOnTouchListener(new TouchListener(
                DisplayUtils.INSTANCE.getScreenWidth(mContext),
                DisplayUtils.INSTANCE.rejectedNavHeight(mContext)- DisplayUtils.INSTANCE.getStatusBarHeight(mContext)-40,
                tv_endshare.getWidth(),
                tv_endshare.getHeight()
        ));
        tx_audio.setOnTouchListener(new TouchListener(
                DisplayUtils.INSTANCE.getScreenWidth(mContext),
                DisplayUtils.INSTANCE.rejectedNavHeight(mContext)- DisplayUtils.INSTANCE.getStatusBarHeight(mContext)-40,
                tx_audio.getWidth(),
                tx_audio.getHeight()
        ));
        iv_close.setOnClickListener(this);


        AudioConfig audioConfig = ConfigHelper.getInstance().getAudioConfig();
        checkAudio(!audioConfig.isEnableAudio());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(0);
        }

        webView.setWebChromeClient(new UIWebViewChromeClient(activity));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }
        });

    }

    public void request(String url, boolean isAgent, String title) {
        if (!isAgent) {
            iv_close.setVisibility(View.VISIBLE);
            tv_endshare.setVisibility(View.GONE);
        } else {
            iv_close.setVisibility(View.GONE);
            tv_endshare.setVisibility(View.VISIBLE);
        }
//        tv_title.setText(title);
        webView.loadUrl(url);
    }

    public void checkAudio(boolean isSelected) {
        tx_audio.setSelected(isSelected);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (mListener != null) {
            if (id == R.id.tv_endshare || id == R.id.iv_close) {
                mListener.onCheckFileWhiteBroad();
            } else if (id == R.id.tx_audio) {
                mListener.onCheckBroad();
            } else if (id == R.id.atv_exit) {
                dismiss();
            }
        }

    }

    void setCookie(String cookie) {
        if (mCookie.isEmpty()) {
            TxLogUtils.i("cookie------isEmpty");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(cookie);
            TxLogUtils.i("setCookie------" + jsonObject.optString("token"));
            JSONObject agentInfo = jsonObject.optJSONObject("agentInfo");
            mCookieManager.setCookie("." + new URL(mUrl).getHost(),
                    String.format("domain=%s", new URL(mUrl).getHost()));
            mCookieManager.setCookie("." + new URL(mUrl).getHost(),
                    "userinfo=" + agentInfo);
            mCookieManager.setCookie("." + new URL(mUrl).getHost(),
                    "token=" + jsonObject.optString("token"));
            mCookieManager.setCookie("." + new URL(mUrl).getHost(),
                    "agentCodeQNB=" + agentInfo.optString("usercode"));
            mCookieManager.setCookie("." + new URL(mUrl).getHost(),
                    "JSESSIONID=" + jsonObject.optString("servicePuk"));
            mCookieManager.setCookie("." + new URL(mUrl).getHost(),
                    "platform=slup");
            mCookieManager.setCookie(".sinosig.com", String.format("domain=%s", new URL(mUrl).getHost()));
            mCookieManager.setCookie(".sinosig.com", "userinfo=" + jsonObject.optJSONObject("agentInfo"));
            mCookieManager.setCookie(".sinosig.com", "agentCodeoc=" + jsonObject.optString("token"));

            String cookieStr = mCookieManager.getCookie(mUrl);
            String jsessionId = "";
            if (!TextUtils.isEmpty(cookieStr)) {
                String[] cookieLists = cookieStr.split(";");
                for (int i = 0; i < cookieLists.length; i++) {
                    if (cookieLists[i].startsWith("JSESSIONID=")) {
                        jsessionId = cookieLists[i];
                        break;
                    }
                }
            }
            mCookieManager.setCookie("." + new URL(mUrl).getHost(), jsessionId);
            TxLogUtils.i("cookieStr---" + cookieStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class TouchListener implements View.OnTouchListener {
        private int hideSize = 0;
        private float lastX;
        private float lastY;
        private long downTime;
        private long delay;
        private boolean isMove;
        private boolean canDrag;
        private float leftBorder;
        private int rightBorder;
        private int bottomBorder;
        private float viewWidth;
        private float viewHeight;

        private TouchListener(int screenWidth, int screenHeight, float viewWidth, float viewHeight) {
            this.leftBorder = 0;
            this.rightBorder = screenWidth;
            this.bottomBorder = screenHeight;
            this.viewWidth = viewWidth;
            this.viewHeight = viewHeight;
        }

//        private TouchListener(long delay) {
//            this.delay = delay;
//        }

        private boolean haveDelay() {
            return delay > 0;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = (int)event.getRawX();
                    lastY = (int)event.getRawY();
                    isMove = false;
                    downTime = System.currentTimeMillis();
                    if (haveDelay()) {
                        canDrag = false;
                    } else {
                        canDrag = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    int moveX = (int)(event.getRawX() - lastX);
                    int moveY = (int)(event.getRawY() - lastY);

                    int top = v.getTop();
                    int left = v.getLeft();
                    int right = v.getRight();
                    int bottom = v.getBottom();
                    //高度范围
                    if(top<0){
                        top = 0;
                        bottom = v.getHeight();
                    }

                    if(bottom>bottomBorder){
                        top = bottomBorder - v.getHeight();
                        bottom = bottomBorder;
                    }

                    //宽度范围
                    if(left<-hideSize){
                        left = -hideSize;
                        right = v.getWidth()-hideSize;
                    }
                    if(right>rightBorder+hideSize){
                        left = rightBorder - v.getWidth()+hideSize;
                        right = rightBorder+hideSize;
                    }
                    v.layout(left+moveX,top+moveY, right+moveX, bottom+moveY);

                    lastX = (int)event.getRawX();
                    lastY = (int)event.getRawY();

                    isMove = true;
                    break;
                default:
                    break;
            }
            return isMove;
        }

    }
}
