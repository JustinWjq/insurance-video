package com.txt.video.trtc.ticimpl.utils;

//import com.tencent.rtmp.TXLog;
import com.tencent.teduboard.TEduBoardController;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.ui.video.VideoActivity;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by JustinWjq
 *
 * @date 2020/8/26.
 * description：
 */
public class MyBoardCallback implements TEduBoardController.TEduBoardCallback {
    private final static String TAG = "TICClassMainActivity";
    WeakReference<VideoActivity> mActivityRef;

    public MyBoardCallback(VideoActivity activityEx) {
        mActivityRef = new WeakReference<>(activityEx);
    }

    @Override
    public void onTEBError(int code, String msg) {
        TxLogUtils.i(TAG, "onTEBError:" + code + "|" + msg);
    }

    @Override
    public void onTEBWarning(int code, String msg) {
        TxLogUtils.i("onTEBWarning:" + code + "|" + msg);
    }

    @Override
    public void onTEBInit() {
        TxLogUtils.i("onTEBInit:");
        VideoActivity activity = mActivityRef.get();
        if (activity != null) {
            activity.addBoardView();
        }
    }

    @Override
    public void onTEBHistroyDataSyncCompleted() {
        VideoActivity activityEx = mActivityRef.get();
        if (activityEx != null) {
//            activityEx.onTEBHistroyDataSyncCompleted();
        }
    }

    @Override
    public void onTEBSyncData(String data) {

    }


    @Override
    public void onTEBAddBoard(List<String> boardId, final String fileId) {
        TxLogUtils.i("onTEBAddBoard:" + fileId);
        VideoActivity activityEx = mActivityRef.get();
        if (activityEx != null) {
            activityEx.onTEBAddBoard(boardId, fileId);
        }
    }

    @Override
    public void onTEBDeleteBoard(List<String> boardId, final String fileId) {
        TxLogUtils.i(TAG, "onTEBDeleteBoard:" + fileId + "|" + boardId);
    }

    @Override
    public void onTEBGotoBoard(String boardId, final String fileId) {
        TxLogUtils.i(TAG, "onTEBGotoBoard:" + fileId + "|" + boardId);
    }

    @Override
    public void onTEBGotoStep(int currentStep, int total) {
        TxLogUtils.i(TAG, "onTEBGotoStep:" + currentStep + "|" + total);
    }

    @Override
    public void onTEBRectSelected() {
        TxLogUtils.i(TAG, "onTEBRectSelected:");
    }

    @Override
    public void onTEBRefresh() {
        TxLogUtils.i(TAG, "onTEBRefresh:");
    }

//    @Override
//    public void onTEBOfflineWarning(int count) {
//
//    }

    @Override
    public void onTEBDeleteFile(String fileId) {
    }

    @Override
    public void onTEBSwitchFile(String fileId) {
    }

    @Override
    public void onTEBAddTranscodeFile(String s) {
        TxLogUtils.i(TAG, "onTEBAddTranscodeFile:" + s);
    }

    @Override
    public void onTEBUndoStatusChanged(boolean canUndo) {
        VideoActivity activityEx = mActivityRef.get();
        if (activityEx != null) {
//            activityEx.mCanUndo = canUndo;
        }
    }

    @Override
    public void onTEBRedoStatusChanged(boolean canredo) {
        VideoActivity activityEx = mActivityRef.get();
        if (activityEx != null) {
//            activityEx.mCanRedo = canredo;
        }
    }

    @Override
    public void onTEBFileUploadProgress(final String path, int currentBytes, int totalBytes, int uploadSpeed, float percent) {
        TxLogUtils.i(TAG, "onTEBFileUploadProgress:" + path + " percent:" + percent);
    }

    @Override
    public void onTEBFileUploadStatus(final String path, int status, int code, String statusMsg) {
        TxLogUtils.i(TAG, "onTEBFileUploadStatus:" + path + " status:" + status);
    }

    @Override
    public void onTEBFileTranscodeProgress(String s, String s1, String s2, TEduBoardController.TEduBoardTranscodeFileResult tEduBoardTranscodeFileResult) {
        TxLogUtils.i(TAG, "onTEBFileTranscodeProgress:" + s + " status:" + s1 + "---" + s2);
    }

    @Override
    public void onTEBH5FileStatusChanged(String fileId, int status) {

    }

    @Override
    public void onTEBAddImagesFile(String fileId) {
        TxLogUtils.i(TAG, "onTEBAddImagesFile:" + fileId);
//        VideoActivity activityEx = mActivityRef.get();
//        TEduBoardController.TEduBoardFileInfo fileInfo = activityEx.mBoard.getFileInfo(fileId);
    }

    @Override
    public void onTEBVideoStatusChanged(String fileId, int status, float progress, float duration) {
        TxLogUtils.i(TAG, "onTEBVideoStatusChanged:" + fileId + " | " + status + "|" + progress);
    }

//    @Override
//    public void onTEBAudioStatusChanged(String elementId, int status, float progress, float duration) {
//
//    }

    @Override
    public void onTEBSnapshot(String path, int code, String msg) {
        TxLogUtils.i(TAG, "onTEBSnapshot:" + path + " | " + code + "|" + msg);
    }


    public void onTEBPPtStatusChanged(int statusCode, String fid, String describeMsg) {

    }

    @Override
    public void onTEBH5PPTStatusChanged(int i, String s, String s1) {

    }
    //    @Override
//    public void onTEBTextElementStatusChange(String status, String id, String value, int left, int top) {
//
//    }
//
//    @Override
//    public void onTEBClassGroupStatusChanged(boolean enable, String classGroupId, int operationType, String message) {
//
//    }

    @Override
    public void onTEBImageStatusChanged(String boardId, String url, int status) {
        TxLogUtils.i(TAG, "onTEBImageStatusChanged:" + boardId + "|" + url + "|" + status);
    }

    @Override
    public void onTEBSetBackgroundImage(final String url) {
        TxLogUtils.i(TAG, "onTEBSetBackgroundImage:" + url);
    }

    @Override
    public void onTEBAddImageElement(final String url) {
        TxLogUtils.i(TAG, "onTEBAddImageElement:" + url);
    }
//
//    @Override
//    public void onTEBAddElement(String id, int type, String url) {
//
//    }

//    @Override
//    public void onTEBDeleteElement(List<String> id) {
//
//    }
//
//    @Override
//    public void onTEBSelectElement(List<TEduBoardController.ElementItem> elementItemList) {
//
//    }
//
//    @Override
//    public void onTEBMathGraphEvent(int code, String boardId, String graphId, String message) {
//
//    }
//
//    @Override
//    public void onTEBZoomDragStatus(String fid, int scale, int xOffset, int yOffset) {
//
//    }

    @Override
    public void onTEBBackgroundH5StatusChanged(String boardId, String url, int status) {
        TxLogUtils.i(TAG, "onTEBBackgroundH5StatusChanged:" + boardId + " url:" + boardId + " status:" + status);
    }

//    @Override
//    public void onTEBTextElementWarning(String code, String message) {
//
//    }
//
//    @Override
//    public void onTEBImageElementStatusChanged(int status, String currentBoardId, String imgUrl, String currentImgUrl) {
//
//    }


}
