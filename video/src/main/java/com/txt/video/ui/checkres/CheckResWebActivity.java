package com.txt.video.ui.checkres;

import static com.tencent.liteav.videobase.f.b.i;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.txt.video.R;
import com.txt.video.TXSdk;
import com.txt.video.base.BaseActivity;
import com.txt.video.common.callback.onDialogListenerCallBack;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.trtc.remoteuser.RemoteUserConfig;
import com.txt.video.trtc.remoteuser.RemoteUserConfigHelper;
import com.txt.video.trtc.videolayout.list.MemberEntity;
import com.txt.video.ui.weight.dialog.SelectPersonDialog;

import java.util.ArrayList;
import java.util.List;

public class CheckResWebActivity extends BaseActivity<CheckResWebContract.ICollectView, CheckResWebPresenter>
        implements CheckResWebContract.ICollectView {

    @Override
    protected int getContentViewId() {
        return R.layout.tx_activity_check_resweb;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        hideStatusBar();
        initWebView();
        TextView tv_person = findViewById(R.id.tv_person);
        tv_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击弹出推送的人员
                showPersonDialog();
            }
        });

    }

    private SelectPersonDialog selectPersonDialog;

    private void showPersonDialog() {
        List<RemoteUserConfig> memberEntityList = RemoteUserConfigHelper.getInstance().getRemoteUserConfigList();
        TxLogUtils.i("memberEntityList" + memberEntityList.size());
        if (memberEntityList.size() == 0) {
//            showMessage("共享产品失败，会议内暂无其他人员");
            return;
        }
        if (memberEntityList.size() == 1) {
        } else {
            if (selectPersonDialog == null) {
                selectPersonDialog =
                        new SelectPersonDialog(this);

            } else {

            }
            selectPersonDialog.setRequestID(TXSdk.getInstance().getAgent());
            selectPersonDialog.setOnConfirmlickListener(
                    new onDialogListenerCallBack() {


                        @Override
                        public void onItemLongClick(@Nullable String id) {

                        }

                        @Override
                        public void onItemClick(@Nullable String url, @NonNull List<String> images) {
                        }

                        @Override
                        public void onItemClick(@Nullable String id, @NonNull String url, @NonNull String name) {
                        }

                        @Override
                        public void onItemClick(@Nullable String id, @NonNull String url) {
                        }

                        @Override
                        public void onFile() {

                        }

                        @Override
                        public void onConfirm() {

                        }
                    });

            selectPersonDialog.show();
            ArrayList<MemberEntity> memberEntities = new ArrayList<>();
            for (int i = 0; i < memberEntityList.size(); i++) {
                RemoteUserConfig remoteUserConfig = memberEntityList.get(i);
                MemberEntity memberEntity = new MemberEntity();
                memberEntity.setUserId(remoteUserConfig.getmUserId());
                memberEntity.setUserName(remoteUserConfig.getUserName());
                memberEntities.add(memberEntity);
            }
            selectPersonDialog.invalidateAdapater(memberEntities);
        }
    }

    private void initWebView() {
        WebView webView = findViewById(R.id.webView);
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
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //加载完毕
                }

            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //
        webView.loadUrl("https://precisemkttest.sinosig.com/resourceNginx/H5Project/qnbProjectV3/index.html#/rayVisitFile");
    }

    @Nullable
    @Override
    protected CheckResWebPresenter createPresenter() {
        return new CheckResWebPresenter(this, this);
    }
}