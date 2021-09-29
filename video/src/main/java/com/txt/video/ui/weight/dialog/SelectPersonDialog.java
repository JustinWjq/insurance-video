package com.txt.video.ui.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.txt.video.R;
import com.txt.video.common.adapter.base.TxBaseQuickAdapter;
import com.txt.video.common.callback.onDialogListenerCallBack;
import com.txt.video.net.bean.PersonBean;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.trtc.videolayout.Utils;
import com.txt.video.trtc.videolayout.list.MemberEntity;
import com.txt.video.ui.weight.adapter.PersonNameAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by justin on 2017/8/25.
 */
public class SelectPersonDialog extends Dialog {
    private onDialogListenerCallBack mListener;
    private Context mContext;

    private PersonNameAdapter baseQuickAdapter;

    public SelectPersonDialog(Context context) {
        super(context, R.style.tx_MyDialog);
        mContext = context;
        list = new ArrayList<>();

    }

    public void setOnConfirmlickListener(onDialogListenerCallBack listener) {
        mListener = listener;
    }

    ArrayList<PersonBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_dialog_personlist);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = Utils.getWindowHeight(mContext) / 3;
        attributes.width = Utils.getWindowWidth(mContext);
        window.setGravity(Gravity.BOTTOM);

        setCanceledOnTouchOutside(true);
        initView();
    }


    public void invalidateAdapater(ArrayList<MemberEntity> mDatas) {
        list.clear();
        TxLogUtils.i("invalidateAdapater");
        list=new ArrayList();
        for (int i = 0; i < mDatas.size(); i++) {
            PersonBean personBean = new PersonBean();
            MemberEntity jsonObject = mDatas.get(i);
            String userId = jsonObject.getUserId();
            if (userId.equals(id)) {
                //本人不添加
            } else{
                personBean.setName(jsonObject.getUserName());
                personBean.setUserId(jsonObject.getUserId());
                list.add(personBean);
            }

        }


        baseQuickAdapter.setNewData(list);
    }

    private String id;

    public void setRequestID(String requestID) {
        this.id = requestID;
    }

    private void initView() {
        RecyclerView tx_rv = (RecyclerView) findViewById(R.id.tx_rv);
        tx_rv.setLayoutManager(new LinearLayoutManager(mContext));
        baseQuickAdapter = new PersonNameAdapter();
        tx_rv.setAdapter(baseQuickAdapter);
        baseQuickAdapter.setOnItemLongClickListener(new TxBaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(TxBaseQuickAdapter adapter, View view, int position) {

                return false;
            }
        });

        baseQuickAdapter.setOnItemClickListener(new TxBaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TxBaseQuickAdapter adapter, View view, int position) {
                List<PersonBean> data = baseQuickAdapter.getData();
                PersonBean personBean = data.get(position);
                mListener.onItemClick(personBean.getUserId(),personBean.getName());
                dismiss();
            }
        });


        findViewById(R.id.atv_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }



    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


}
