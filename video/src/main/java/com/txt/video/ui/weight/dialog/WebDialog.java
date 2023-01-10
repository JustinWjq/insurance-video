package  com.txt.video.ui.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
//import android.webkit.CookieManager;
//import android.webkit.CookieSyncManager;
//import android.webkit.WebChromeClient;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.txt.video.R;
import com.txt.video.common.callback.onShareWhiteBroadDialogListener;
import com.txt.video.trtc.ConfigHelper;
import com.txt.video.trtc.feature.AudioConfig;
import com.txt.video.trtc.videolayout.Utils;
import com.txt.video.ui.weight.easyfloat.utils.DisplayUtils;


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


    private String mUrl;
    private String mCookie;
    public WebDialog(Context context,String url,String cookie) {
        super(context, R.style.tx_MyDialog);
        mContext = context;
        this.mUrl = url;
        this.mCookie = cookie;


    }

    public void setOnShareWhiteBroadDialogListener(onShareWhiteBroadDialogListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Window win = this.getWindow();
//
//        WindowManager.LayoutParams lp = win.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.gravity = Gravity.CENTER;
//        //兼容刘海屏
//        if (Build.VERSION.SDK_INT >= 28) {
//            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//        }
//        win.setAttributes(lp);
//
//        win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        int options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//        win.getDecorView().setSystemUiVisibility(options);
//        setContentView(R.layout.tx_dialog_web);
//        Window window = getWindow();
//        window.setGravity(Gravity.CENTER);
//
//        setCanceledOnTouchOutside(false);
//        injectCookie();
//        initView();
        setContentView(R.layout.tx_dialog_web);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);

        setCanceledOnTouchOutside(true);
        injectCookie();
        initView();
    }

    public void changeUi(int width,int heigh){
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = heigh;
        attributes.width = width;
        window.setAttributes(attributes);

    }
    WebView webView;
    ImageButton tx_audio;
    TextView iv_close;
    TextView tv_endshare;
    TextView tv_title;
    private void injectCookie(){
        try {
            if (null != mCookie &&!mCookie.isEmpty()) {
            CookieManager mCookieManager = CookieManager.getInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mCookieManager.removeSessionCookies(null);
                mCookieManager.flush();
            }else{
                mCookieManager.removeAllCookie();
                CookieSyncManager.getInstance().sync();
            }
            mCookieManager.setAcceptCookie(true);

                mCookieManager.setCookie(mUrl,mCookie);
            }
        }catch (Exception e){

        }

    }
    private void initView(){

        webView = findViewById(R.id.webView);
        tv_endshare = findViewById(R.id.tv_endshare);
        iv_close = findViewById(R.id.iv_close);
        tx_audio = findViewById(R.id.tx_audio);
        tv_title = findViewById(R.id.tv_title);
        tv_endshare.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        tx_audio.setOnClickListener(this);

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
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress ==100) {
                    //加载完毕
                    mListener.onEnd();
                }

            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    public void request(String url,boolean isAgent,String title){
        if (!isAgent) {
            iv_close.setVisibility(View.VISIBLE);
            tv_endshare.setVisibility(View.GONE);
        }else{
            iv_close.setVisibility(View.GONE);
            tv_endshare.setVisibility(View.VISIBLE);
        }
        tv_title.setText(title);
//        webView.loadUrl("https://sync-web-test.cloud-ins.cn/mirror.html?syncid=132-cvsstest1050020635123-3&synctoken=006880b027964924e6ca254b77531c2eaf3IAATJZVg7_uayN3siDhG1FCAnNGNyKufRj2ZShXkNESlzgGZEB8AAAAAEADJLSwHXoqiYwEA6ANeiqJj");
        webView.loadUrl(url);
    }

    public void checkAudio(boolean isSelected){
        tx_audio.setSelected(isSelected);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (mListener != null) {
            if (id == R.id.tv_endshare||id == R.id.iv_close) {
                mListener.onCheckFileWhiteBroad();
            } else if (id == R.id.tx_audio) {
                mListener.onCheckBroad();
            } else if (id == R.id.atv_exit) {
                dismiss();
            }
        }

    }
}
