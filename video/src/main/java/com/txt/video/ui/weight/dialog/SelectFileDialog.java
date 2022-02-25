package com.txt.video.ui.weight.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.google.gson.Gson;
import com.txt.video.R;
import com.txt.video.common.toast.ToastUtils;
import com.txt.video.net.bean.FileBean;
import com.txt.video.net.bean.LevelItem1;
import com.txt.video.net.http.HttpRequestClient;
import com.txt.video.net.http.SystemHttpRequest;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.trtc.videolayout.Utils;
import com.txt.video.ui.weight.adapter.ExpandableItemAdapter;
import com.txt.video.common.adapter.base.TxBaseQuickAdapter;
import com.txt.video.common.adapter.base.entity.MultiItemEntity;
import com.txt.video.common.callback.onDialogListenerCallBack;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by justin on 2017/8/25.
 */
public class SelectFileDialog extends Dialog {
    private onDialogListenerCallBack mListener;
    private Context mContext;

    private ExpandableItemAdapter baseQuickAdapter;

    public SelectFileDialog(Context context) {
        super(context, R.style.tx_MyDialog);
        mContext = context;
        list = new ArrayList<>();

    }

    public void setOnConfirmlickListener(onDialogListenerCallBack listener) {
        mListener = listener;
    }

    ArrayList<MultiItemEntity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_dialog_changefile);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = Utils.getWindowHeight(mContext) / 2;
        attributes.width = Utils.getWindowWidth(mContext);
        window.setGravity(Gravity.BOTTOM);

        setCanceledOnTouchOutside(true);
        initView();
    }

    private boolean isNeedStartTimer = false;
    private int noNeedStartTimerCount = 0;

    public void invalidateAdapater(List<FileBean.ListBean> mDatas) {
        list.clear();
        TxLogUtils.i("invalidateAdapater");

        ArrayList<MultiItemEntity> isPubliclist = new ArrayList<>();

        ArrayList<MultiItemEntity> isNoPubliclist = new ArrayList<>();
        ArrayList<MultiItemEntity> isIdpathlist = new ArrayList<>();
        LevelItem1 isNoPublicItem = new LevelItem1(isNoPubliclist);
        isNoPublicItem.setExpanded(false);

        LevelItem1 isPublicItem = new LevelItem1(isPubliclist);
        LevelItem1 isIdpathItem = new LevelItem1(isIdpathlist);
        isPublicItem.setExpanded(false);
        isIdpathItem.setExpanded(false);
        noNeedStartTimerCount = 0;
        for (FileBean.ListBean bean : mDatas) {
            List<String> images = bean.getImages();
            if (null == images || images.size() == 0) {
                isNeedStartTimer = true;
                bean.setUploading(true);
            } else {
                noNeedStartTimerCount++;
                bean.setUploading(false);
            }
            if (bean.getIdpathName() != null) {
                //不为空 就是机构素材库
                isIdpathItem.addSubItem(bean);
            } else {
                if (bean.isIsPublic()) {
                    isPublicItem.addSubItem(bean);
                } else {
                    isNoPublicItem.addSubItem(bean);
                }
            }


        }

        if (noNeedStartTimerCount ==mDatas.size()){
            isNeedStartTimer = false;
        }

        if (null == isNoPublicItem.getSubItems() || isNoPublicItem.getSubItems().size() == 0) {
            isNoPublicItem.setTitle("个人素材库 (0)");
        } else {
            isNoPublicItem.setTitle("个人素材库 (" + isNoPublicItem.getSubItems().size() + ")");
        }

        if (null == isPublicItem.getSubItems() || isPublicItem.getSubItems().size() == 0) {
            isPublicItem.setTitle("公共素材库 (0)");
        } else {
            isPublicItem.setTitle("公共素材库 (" + isPublicItem.getSubItems().size() + ")");
        }
        if (null == isIdpathItem.getSubItems() || isIdpathItem.getSubItems().size() == 0) {
            isIdpathItem.setTitle("职场素材库 (0)");
        } else {
            isIdpathItem.setTitle("职场素材库 (" + isIdpathItem.getSubItems().size() + ")");
        }

        list.add(isNoPublicItem);
        list.add(isPublicItem);
        list.add(isIdpathItem);

        baseQuickAdapter.setNewData(list);
        baseQuickAdapter.expand(0);
        if (!isNeedStartTimer) {
            stopTimer();
        }else{
            startTimer();
        }

    }

    private String id;

    public void setRequestID(String requestID) {
        this.id = requestID;
    }

    private void initView() {
        View tv_uploadpic = findViewById(R.id.tv_uploadpic);
        View tv_uploadfile = findViewById(R.id.tv_uploadfile);
        RecyclerView tx_rv = (RecyclerView) findViewById(R.id.tx_rv);
        tx_rv.setLayoutManager(new LinearLayoutManager(mContext));
        baseQuickAdapter = new ExpandableItemAdapter(list);
        tx_rv.setAdapter(baseQuickAdapter);
        baseQuickAdapter.setOnItemLongClickListener(new TxBaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(TxBaseQuickAdapter adapter, View view, int position) {
                List<MultiItemEntity> data = baseQuickAdapter.getData();
                MultiItemEntity listBean = data.get(position);
                if (listBean.getItemType() == 1) {
                    FileBean.ListBean listItemBean = (FileBean.ListBean) listBean;
                    if (!listItemBean.isIsPublic()) {
                        mListener.onItemLongClick(listItemBean.get_id());
                        return true;
                    } else {
                        return false;
                    }


                } else {
                    return false;
                }

            }
        });

        baseQuickAdapter.setOnItemClickListener(new TxBaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TxBaseQuickAdapter adapter, View view, int position) {
                List<MultiItemEntity> data = baseQuickAdapter.getData();
                MultiItemEntity listBean = data.get(position);
                if (listBean.getItemType() == 1) {
                    FileBean.ListBean listItemBean = (FileBean.ListBean) listBean;
                    TxLogUtils.i("postion" + position + "--------listBean" + listItemBean.getName());
                    mListener.onItemClick(listItemBean.getUrl(), listItemBean.getImages());
                } else {
                }
            }
        });

        tv_uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onConfirm();
            }
        });

        tv_uploadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFile();
            }
        });
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        request();
    }

    private CountDownTimer timer = null;

    public void startTimer() {
        TxLogUtils.i("isNeedStartTimer", "" + noNeedStartTimerCount);
        TxLogUtils.i("isNeedStartTimer", baseQuickAdapter.getData().size() - 1 + "");
        if (baseQuickAdapter.getData().size() - 1 == noNeedStartTimerCount) {
            TxLogUtils.i("isNeedStartTimer", "timer.stop()");
            stopTimer();
        } else {
            if (isNeedStartTimer) {

                if (timer == null) {
                    TxLogUtils.i("isNeedStartTimer", "timer.start()");
                    timer = new CountDownTimer(60000, 2000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            request();
                        }

                        @Override
                        public void onFinish() {
                            if (timer != null) {
                                timer.start();
                            }
                        }
                    };
                    timer.start();
                } else {

                }


            } else {

                stopTimer();
            }
        }


    }

    public void stopTimer() {
        if (timer != null) {
            isNeedStartTimer = false;
            noNeedStartTimerCount = 0;
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        stopTimer();
        super.dismiss();
    }

    public void request() {
        SystemHttpRequest.getInstance()
                .getFileLists(id, new HttpRequestClient.RequestHttpCallBack() {
                    @Override
                    public void onSuccess(final String json) {
                        if (isShowing()) {
                            Activity mContext = (Activity) SelectFileDialog.this.mContext;
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Gson gson = new Gson();
                                    FileBean fileBean = gson.fromJson(json, FileBean.class);

                                    invalidateAdapater(fileBean.getList());
                                }
                            });
                        }

                    }

                    @Override
                    public void onFail(final String err, int code) {
                        Activity mContext = (Activity) SelectFileDialog.this.mContext;
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(err);
                            }
                        });
                    }
                });
    }


}
