package com.txt.video.ui.weight.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.View;
//import android.webkit.ConsoleMessage;
//import android.webkit.GeolocationPermissions;
//import android.webkit.JsResult;
//import android.webkit.PermissionRequest;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebView;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import android.webkit.PermissionRequest;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.tencent.imsdk.utils.FileUtil;
import com.txt.video.base.constants.VideoCode;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.ui.video.VideoActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 创建者: wwd
 * 创建日期:17/10/19
 * 类的功能描述:可以重写chromeclient里的方法，控制进度条等状态
 */

public class UIWebViewChromeClient extends WebChromeClient {
    private VideoActivity activity;
    private PermissionRequest request;
    private ValueCallback<Uri> uploadMsg;
    private String acceptType;
    private ValueCallback<Uri[]> filePathCallback;
    private FileChooserParams fileChooserParams;
    private WebView webView;

    public UIWebViewChromeClient(VideoActivity mActivity) {
        this.activity = mActivity;
    }
    @Override
    public View getVideoLoadingProgressView() {
        FrameLayout frameLayout = new FrameLayout(activity);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        return frameLayout;
    }

    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
//        activity.showCustomView(view, callback);
    }

    @Override
    public void onHideCustomView() {
//        activity.hideCustomView();
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        result.confirm();
        return true;
    }

//    @Override
//    public void onPermissionRequest(PermissionRequest request) {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            String[] resources = request.getResources();
//            request.grant(resources);
//        }
//    }
//
//    @Override
//    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
//        callback.invoke(origin, true, true);
//        super.onGeolocationPermissionsShowPrompt(origin, callback);
//    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage msg) {
        switch (msg.messageLevel()) {
            case ERROR:
                Log.e("Console", msg.sourceId() + ":" + msg.lineNumber() + "\n" + msg.message());
                break;
            default:
                Log.i("Console", msg.message());
        }

        return super.onConsoleMessage(msg);
    }

    //      // For 3.0+ Devices (Start)
//      // onActivityResult attached before constructor
    protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
        activity.setMUploadMessage(uploadMsg);
        take();
    }

    // For Lollipop 5.0+ Devices
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean onShowFileChooser(WebView mWebView,
                                     ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        activity.setUploadMessage(filePathCallback);

        activity.setCapture( fileChooserParams.isCaptureEnabled());
        take();
        return true;
    }

    //For Android 4.1 only
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        activity.setMUploadMessage(uploadMsg);
        activity.setCapture (!"0".equals(capture));
        take();
    }

    protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
        activity.setMUploadMessage(uploadMsg);
        take();
    }

    @SuppressLint("CheckResult")
    private void take() {
        // Create the storage directory if it does not exist
        // 用户已经同意该权限
        String path = null;
        try {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getCanonicalPath()
                    + "/" +"IMG_"
                    + System.currentTimeMillis() + ".jpg";
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(path);
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            activity.setImageUri(Uri.fromFile(file));
        } else {
            /**
             * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
             */
            TxLogUtils.i("activity.getPackageName()-----"+activity.getPackageName());
            activity.setImageUri(
                    FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileProvider",
                            file));
            //加入uri权限 要不三星手机不能拍照
            List<ResolveInfo> resInfoList = activity.getPackageManager()
                    .queryIntentActivities(captureIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                activity.grantUriPermission(packageName, activity.getImageUri(),
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        if (activity.isCapture()) {
            activity.setPhotoUri(activity.getImageUri());
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, activity.getImageUri());
            activity.startActivityForResult(captureIntent, VideoCode.FILECHOOSER_RESULTCODE);
        } else {
            final List<Intent> cameraIntents = new ArrayList<Intent>();
            final PackageManager packageManager = activity.getPackageManager();
            final List<ResolveInfo> listCam =
                    packageManager.queryIntentActivities(captureIntent, 0);
            for (ResolveInfo res : listCam) {
                final String packageName = res.activityInfo.packageName;
                final Intent i = new Intent(captureIntent);
                i.setComponent(
                        new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                i.setPackage(packageName);
                i.putExtra(MediaStore.EXTRA_OUTPUT, activity.getImageUri());
                cameraIntents.add(i);
            }
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    cameraIntents.toArray(new Parcelable[]{}));
            activity.setPhotoUri(activity.getImageUri());
            activity.startActivityForResult(chooserIntent, VideoCode.FILECHOOSER_RESULTCODE);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }




}
