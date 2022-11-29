package com.txt.video.ui.video.remoteuser;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;
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
        private TextView tv_user_remarks;
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
            tv_user_remarks = (TextView) itemView.findViewById(R.id.tv_user_remarks);
        }

        public void bind(final MemberEntity model,
                         final boolean isSelf,
                         final OnItemClickListener listener) {
            //todo 高亮展示
            if (isSelf) {
                tv_user_remarks.setText(model.getUserName() + " (主持人、我)");
            } else {
                tv_user_remarks.setText(model.getUserName());
            }
            String userName = model.getUserName();
            if (userName.length()>2) {
                userName = userName.substring(userName.length()-2, userName.length());
            }
            mUserNameTv.setText(userName);
            mAudioImg.setSelected(model.isMuteAudio());
            mVideoImg.setSelected(model.isMuteVideo());
            mAudioImg.setVisibility(View.VISIBLE);
            mVideoImg.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMuteAudioClick(model);
                }
            });
            mAudioImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMuteAudioClick(model);
                }
            });
            mVideoImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMuteVideoClick(model);
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
        holder.bind(item, item.isHost(), onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onMuteAudioClick(MemberEntity mMemberEntity);

        void onMuteVideoClick(MemberEntity mMemberEntity);
    }

}