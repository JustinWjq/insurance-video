package com.txt.video.ui.weight.dialog;

import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * author ：Justin
 * time ：2023/2/2.
 * des ：
 */
public class TxWebChromeClient extends WebChromeClient {

    @Override
    public void onPermissionRequest(PermissionRequest request) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            String[] resources = request.getResources();
            request.grant(resources);
        }
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, true);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

}
