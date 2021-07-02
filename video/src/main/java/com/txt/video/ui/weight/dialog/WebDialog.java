package  com.txt.video.ui.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.txt.video.R;
import com.txt.video.common.callback.onShareWhiteBroadDialogListener;
import com.txt.video.trtc.ConfigHelper;
import com.txt.video.trtc.feature.AudioConfig;
import com.txt.video.trtc.videolayout.Utils;


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

    public WebDialog(Context context) {
        super(context, R.style.tx_MyDialog);
        mContext = context;


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
//        attributes.height = Utils.getWindowHeight(mContext);
        attributes.height = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.width = Utils.getWindowWidth(mContext);
        window.setAttributes(attributes);
        window.setGravity(Gravity.BOTTOM);

        setCanceledOnTouchOutside(true);
        initView();
    }
    WebView webView;
    ImageButton tx_audio;
    TextView iv_close;
    TextView tv_endshare;
    TextView tv_title;
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
        webView.loadUrl(url);
    }

    public void checkAudio(boolean isSelected){
        tx_audio.setSelected(isSelected);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (mListener != null) {
            if (id == R.id.tv_endshare) {
                mListener.onCheckFileWhiteBroad();
            } else if (id == R.id.tx_audio) {
                mListener.onCheckBroad();
            } else if (id == R.id.iv_close) {
                mListener.onShareWhiteBroadEnd();
            }else if (id == R.id.atv_exit) {
                dismiss();
            }
        }

    }
}
