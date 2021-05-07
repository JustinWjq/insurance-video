package com.txt.video.ui.video.remote;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.txt.video.R;
import com.txt.video.trtc.videolayout.Utils;
import com.txt.video.trtc.videolayout.list.MemberEntity;
import com.txt.video.common.adapter.decoration.DividerItemDecoration;

import java.util.List;

public class RemoteUserListView extends ConstraintLayout {
    private final Context mContext;

    private TextView mMuteAudioAllBtn;
    private TextView mMuteVideoAllBtn;
    private TextView mMuteAudioAllOffBtn;
    private TextView mIvClose;
    private ImageView ivNoRemoteuser;
    private RecyclerView mUserListRv;
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
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = Utils.getWindowHeight(getContext())/2;
                setLayoutParams(layoutParams);
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

    private void initView(View itemView) {
        mMuteAudioAllBtn = (TextView) itemView.findViewById(R.id.btn_mute_audio_all);
        mMuteVideoAllBtn = (TextView) itemView.findViewById(R.id.btn_mute_video_all);
        mMuteAudioAllOffBtn = (TextView) itemView.findViewById(R.id.btn_mute_audio_all_off);
        mUserListRv = (RecyclerView) itemView.findViewById(R.id.rv_user_list);
        mIvClose = (TextView) itemView.findViewById(R.id.iv_close);
        ivNoRemoteuser = (ImageView) itemView.findViewById(R.id.iv_noremoteuser);

        mIvClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRemoteUserListCallback != null) {
                    mRemoteUserListCallback.onFinishClick();
                }
            }
        });
        mUserListRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mUserListRv.addItemDecoration(new DividerItemDecoration(mContext));
        mRemoteUserListAdapter = new RemoteUserListAdapter(mContext, new RemoteUserListAdapter.OnItemClickListener() {
            @Override
            public void onMuteAudioClick(int position) {
                if (mRemoteUserListCallback != null) {
                    mRemoteUserListCallback.onMuteAudioClick(position);
                }
            }

            @Override
            public void onMuteVideoClick(int position) {
                if (mRemoteUserListCallback != null) {
                    mRemoteUserListCallback.onMuteVideoClick(position);
                }
            }
        });

//        mRemoteUserListAdapter.setMemberList(mMemberEntityList);


        mUserListRv.setAdapter(mRemoteUserListAdapter);
        mUserListRv.setHasFixedSize(true);

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
        if (mRemoteUserListAdapter != null&&isShown()) {
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
                mMuteAudioAllBtn.setTextColor(ContextCompat.getColor(mContext,R.color.tx_white));
            }else{
                mMuteAudioAllBtn.setTextColor(ContextCompat.getColor(mContext,R.color.tx_color_006DFF));
            }
        }
    }

    public interface RemoteUserListCallback {
        void onFinishClick();

        void onMuteAllAudioClick();

        void onMuteAllAudioOffClick();

        void onMuteAllVideoClick();

        void onMuteAudioClick(int position);

        void onMuteVideoClick(int position);
    }
}
