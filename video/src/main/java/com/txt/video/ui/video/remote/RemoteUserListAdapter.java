package com.txt.video.ui.video.remote;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.txt.video.R;
import com.txt.video.trtc.videolayout.list.MemberEntity;

import java.util.List;


public class RemoteUserListAdapter extends
        RecyclerView.Adapter<RemoteUserListAdapter.ViewHolder> {

    private static final String TAG = RemoteUserListAdapter.class.getSimpleName();

    private Context context;
    private List<MemberEntity> list;
    private OnItemClickListener onItemClickListener;

    public RemoteUserListAdapter(Context context,
                                 OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public void setMemberList(List<MemberEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mUserNameTv;
        private AppCompatImageButton mAudioImg;
        private AppCompatImageButton mVideoImg;

        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(final View itemView) {
            mUserNameTv = (TextView) itemView.findViewById(R.id.tv_user_name);
            mAudioImg = (AppCompatImageButton) itemView.findViewById(R.id.img_audio);
            mVideoImg = (AppCompatImageButton) itemView.findViewById(R.id.img_video);
        }

        public void bind(final MemberEntity model,
                         final boolean isSelf,
                         final OnItemClickListener listener) {

//            if (isSelf) {
//                mUserNameTv.setText(model.getUserName() + "(自己)");
//                mAudioImg.setVisibility(View.GONE);
//                mVideoImg.setVisibility(View.GONE);
//            } else {
//
//            }
            mUserNameTv.setText(model.getUserName());
            mAudioImg.setSelected(model.isMuteAudio());
            mVideoImg.setSelected(model.isMuteVideo());
            mAudioImg.setVisibility(View.VISIBLE);
            mVideoImg.setVisibility(View.VISIBLE);
            mAudioImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMuteAudioClick(getLayoutPosition());
                }
            });
            mVideoImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMuteVideoClick(getLayoutPosition());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context  = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.tx_item_meeting_remote_user_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MemberEntity item = list.get(position);
        holder.bind(item, false, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onMuteAudioClick(int position);

        void onMuteVideoClick(int position);
    }

}