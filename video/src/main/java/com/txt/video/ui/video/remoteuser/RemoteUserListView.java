package com.txt.video.ui.video.remoteuser;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.txt.video.R;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.trtc.videolayout.Utils;
import com.txt.video.trtc.videolayout.list.MemberEntity;
import com.txt.video.common.adapter.decoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 成员搜索
 */
public class RemoteUserListView extends ConstraintLayout {
    private final Context mContext;

    private TextView mMuteAudioAllBtn;
    private TextView mMuteVideoAllBtn;
    private TextView mMuteAudioAllOffBtn;
    private TextView mIvClose;
    private TextView ivNoRemoteuser;
    private ImageView iv_delete;
    private RecyclerView mUserListRv;
    private EditText et_search;
    private TextView tv_search;
    private List<MemberEntity> mMemberEntityList;
    private RemoteUserListAdapter  mRemoteUserListAdapter;
    private RemoteUserListCallback mRemoteUserListCallback;

    public RemoteUserListView(Context context) {
        this(context, null);
    }

    public RemoteUserListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflate(context, R.layout.tx_view_meeting_remote_user_list, this);
        post(new Runnable() {
            @Override
            public void run() {
//                ViewGroup.LayoutParams layoutParams = getLayoutParams();
//                layoutParams.height = Utils.getWindowHeight(getContext())/2;
//                setLayoutParams(layoutParams);
            }
        });

        initView(this);


    }

    /**
     * 防止界面点击被透传
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }
    ArrayList<MemberEntity> memberEntities = new ArrayList<>();
    private void initView(View itemView) {
        mMuteAudioAllBtn = (TextView) itemView.findViewById(R.id.btn_mute_audio_all);
        mMuteVideoAllBtn = (TextView) itemView.findViewById(R.id.btn_mute_video_all);
        mMuteAudioAllOffBtn = (TextView) itemView.findViewById(R.id.btn_mute_audio_all_off);
        mUserListRv = (RecyclerView) itemView.findViewById(R.id.rv_user_list);
//        mIvClose = (TextView) itemView.findViewById(R.id.iv_close);
        ivNoRemoteuser = (TextView) itemView.findViewById(R.id.iv_noremoteuser);
        et_search = (EditText) itemView.findViewById(R.id.et_search);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //进行查询
                TxLogUtils.i("当前搜索的"+s);
                memberEntities.clear();
                if (et_search.getText().toString().isEmpty()) {
//                    tv_search.setVisibility(GONE);
                    iv_delete.setVisibility(GONE);
                }else{
                    tv_search.setVisibility(VISIBLE);
                    iv_delete.setVisibility(VISIBLE);
                }

                for (int i = 0; i < mMemberEntityList.size(); i++) {
                    MemberEntity memberEntity = mMemberEntityList.get(i);
                    String userName = memberEntity.getUserName().toLowerCase().trim();
                    boolean contains = userName.toLowerCase().trim().contains(s);
                    TxLogUtils.i("当前是否包含"+contains);
                    if (contains) { //如果输入的文字包含在list中列表中的名字中
                        memberEntities.add(memberEntity);
                    }else{

                    }
                }
                if (memberEntities.size()==0) {
                    ivNoRemoteuser.setVisibility(View.VISIBLE);
                    mUserListRv.setVisibility(View.GONE);
                }else{
                    ivNoRemoteuser.setVisibility(View.GONE);
                    mUserListRv.setVisibility(View.VISIBLE);
                }
                mRemoteUserListAdapter.setMemberList(memberEntities);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tv_search = (TextView) itemView.findViewById(R.id.tv_search);
        iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.getText().clear();
                ivNoRemoteuser.setVisibility(View.GONE);
                mUserListRv.setVisibility(View.VISIBLE);
            }
        });

        tv_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击取消
                tv_search.setVisibility(GONE);
                iv_delete.setVisibility(GONE);
                et_search.getText().clear();
                et_search.clearFocus();
                mUserListRv.setFocusable(true);
                ivNoRemoteuser.setVisibility(View.GONE);
                mUserListRv.setVisibility(View.VISIBLE);
            }
        });


//        mIvClose.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mRemoteUserListCallback != null) {
//                    mRemoteUserListCallback.onFinishClick();
//                }
//            }
//        });
        mUserListRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
//        mUserListRv.addItemDecoration(new DividerItemDecoration(mContext));
        mRemoteUserListAdapter = new RemoteUserListAdapter(mContext, new RemoteUserListAdapter.OnItemClickListener() {
            @Override
            public void onMuteAudioClick(MemberEntity memberEntity) {
                if (mRemoteUserListCallback != null) {
                    mRemoteUserListCallback.onMuteAudioClick(memberEntity);
                }
            }

            @Override
            public void onMuteVideoClick(MemberEntity memberEntity) {
                if (mRemoteUserListCallback != null) {
                    mRemoteUserListCallback.onMuteVideoClick(memberEntity);
                }
            }
        });

//        mRemoteUserListAdapter.setMemberList(mMemberEntityList);


        mUserListRv.setAdapter(mRemoteUserListAdapter);
//        mUserListRv.setHasFixedSize(true);

        mMuteAudioAllBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRemoteUserListCallback != null) {
                    mRemoteUserListCallback.onMuteAllAudioClick();
                }
            }
        });

        mMuteAudioAllOffBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRemoteUserListCallback != null) {
                    mRemoteUserListCallback.onMuteAllAudioOffClick();
                }
            }
        });

        mMuteVideoAllBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRemoteUserListCallback != null) {
                    mRemoteUserListCallback.onMuteAllVideoClick();
                }
            }
        });
    }

    /**
     * 设置成员数据
     * @param memberEntityList
     */

    public void setRemoteUser(List<MemberEntity> memberEntityList) {
        mMemberEntityList = memberEntityList;
        if (mRemoteUserListAdapter != null) {
            if (mMemberEntityList.size()==0) {
                ivNoRemoteuser.setVisibility(View.VISIBLE);
                mUserListRv.setVisibility(View.GONE);
            }else{
                ivNoRemoteuser.setVisibility(View.GONE);
                mUserListRv.setVisibility(View.VISIBLE);
            }
            mRemoteUserListAdapter.setMemberList(mMemberEntityList);
        }
    }

    public void notifyDataSetChanged() {
        if (mRemoteUserListAdapter != null) {
            if (mMemberEntityList.size()==0) {
                ivNoRemoteuser.setVisibility(View.VISIBLE);
                mUserListRv.setVisibility(View.GONE);
            }else{
                ivNoRemoteuser.setVisibility(View.GONE);
                mUserListRv.setVisibility(View.VISIBLE);
            }
            mRemoteUserListAdapter.notifyDataSetChanged();
        }
    }

    public void setRemoteUserListCallback(RemoteUserListCallback remoteUserListCallback) {
        mRemoteUserListCallback = remoteUserListCallback;
    }

    public void selectAudioBtn(boolean isSelect){
        if (null!= mMuteAudioAllBtn) {
            mMuteAudioAllBtn.setSelected(isSelect);
            if (isSelect){
                mMuteAudioAllBtn.setTextColor(ContextCompat.getColor(mContext,R.color.tx_color_e6b980));
            }else{
                mMuteAudioAllBtn.setTextColor(ContextCompat.getColor(mContext,R.color.tx_color_333333));
            }
        }
    }



    public interface RemoteUserListCallback {
        void onFinishClick();

        void onMuteAllAudioClick();

        void onMuteAllAudioOffClick();

        void onMuteAllVideoClick();

        void onMuteAudioClick(MemberEntity memberEntity);

        void onMuteVideoClick(MemberEntity memberEntity);
    }
}
