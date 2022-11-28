package com.txt.video.trtc.videolayout.list;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.txt.video.R;
import com.txt.video.TXSdk;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.ui.video.VideoMode;

import java.util.List;


public class MemberListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = MemberListAdapter.class.getSimpleName();

    private static final int TYPE_SELF = 0;
    private static final int TYPE_OTHER = 1;
    public static final String VOLUME_SHOW = "volume_show";
    public static final String VOLUME_HIDE = "volume_hide";
    public static final String VOLUME = "volume";
    public static final String ISHOST = "isHost";
    public static final String QUALITY = "quality";
    public static final String VIDEO_CLOSE = "video_close";
    public static final String VIDEO_SCREEN_CLOSE = "video_screen_close";
    public static final String VIDEOVIEW_CHANGE = "videoview_change";
    public static final String NAME_CHANGE = "name_change";
    public static final String NAMEANDUSERHEAD_CHANGE = "nameanduserhead_change";

    private Context context;
    private List<MemberEntity> list;
    private ListCallback mListCallback;
//
//    public MemberListAdapter(Context context,
//                             TRTCMeeting meeting,
//                             List<MemberEntity> list,
//                             ListCallback listCallback) {
//        this.mTRTCMeeting = meeting;
//        this.context = context;
//        this.list = list;
//        this.mListCallback = listCallback;
//    }

    public MemberListAdapter(Context context,
                             List<MemberEntity> list,
                             ListCallback listCallback) {
        this.context = context;
        this.list = list;
        this.mListCallback = listCallback;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_SELF) {
            View view = inflater.inflate(R.layout.tx_layout_trtc_func, parent, false);
            if (VideoMode.getVIDEOMODE_HORIZONTAL() == TXSdk.getInstance().getRoomControlConfig().getVideoMode()){
                int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
                view.setLayoutParams(new ViewGroup.LayoutParams(widthPixels/4, widthPixels/3));
            }

            return new SelfViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.tx_layout_trtc_func, parent, false);
            if (VideoMode.getVIDEOMODE_HORIZONTAL() == TXSdk.getInstance().getRoomControlConfig().getVideoMode()){
                int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
                view.setLayoutParams(new ViewGroup.LayoutParams(widthPixels/4, widthPixels/3));
            }

            return new OtherViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TxLogUtils.i(TAG, "onBindViewHolder: " + position);
        if (holder instanceof OtherViewHolder) {
            MemberEntity item = list.get(position);
            ((OtherViewHolder) holder).bind(item, mListCallback);
        } else if (holder instanceof SelfViewHolder) {
            MemberEntity item = list.get(position);
            ((SelfViewHolder) holder).bind(item, mListCallback);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.size() == 0) {
            onBindViewHolder(holder, position);
        } else {
            if (QUALITY.equals(payloads.get(0))) {
                if (holder instanceof OtherViewHolder) {
                    MemberEntity item = list.get(position);
                    ((OtherViewHolder) holder).setQuality(item.getQuality());
                } else if (holder instanceof SelfViewHolder) {
                    MemberEntity item = list.get(position);
                    ((SelfViewHolder) holder).setQuality(item.getQuality());
                }
            } else if (VOLUME.equals(payloads.get(0))) {
                if (holder instanceof OtherViewHolder) {
                    MemberEntity item = list.get(position);
                    ((OtherViewHolder) holder).setVolume(item.getAudioVolume());
                } else if (holder instanceof SelfViewHolder) {
                    MemberEntity item = list.get(position);
                    ((SelfViewHolder) holder).setVolume(item.getAudioVolume());
                }
            } else if (VOLUME_SHOW.equals(payloads.get(0))) {
                if (holder instanceof OtherViewHolder) {
                    MemberEntity item = list.get(position);
                    ((OtherViewHolder) holder).showVolume(item.isShowAudioEvaluation());
                } else if (holder instanceof SelfViewHolder) {
                    MemberEntity item = list.get(position);
                    ((SelfViewHolder) holder).showVolume(item.isShowAudioEvaluation());
                }
            }else if (VOLUME_HIDE.equals(payloads.get(0))) {
                if (holder instanceof OtherViewHolder) {
                    MemberEntity item = list.get(position);
                    ((OtherViewHolder) holder).showVolume(item.isShowAudioEvaluation());
                } else if (holder instanceof SelfViewHolder) {
                    MemberEntity item = list.get(position);
                    ((SelfViewHolder) holder).showVolume(item.isShowAudioEvaluation());
                }
            }else if (VIDEO_CLOSE.equals(payloads.get(0))) {
                if (holder instanceof OtherViewHolder) {
                    MemberEntity item = list.get(position);
                    ((OtherViewHolder) holder).showNoVideo(item.isMuteVideo(),true);
                } else if (holder instanceof SelfViewHolder) {
                    MemberEntity item = list.get(position);
                    ((SelfViewHolder) holder).showNoVideo(item.isMuteVideo(),true);
                }
            }else if (VIDEO_SCREEN_CLOSE.equals(payloads.get(0))) {
                if (holder instanceof OtherViewHolder) {
                    MemberEntity item = list.get(position);
                    ((OtherViewHolder) holder).showNoVideo(item.isScreen(),false);
                } else if (holder instanceof SelfViewHolder) {
                    MemberEntity item = list.get(position);
                    ((SelfViewHolder) holder).showNoVideo(item.isScreen(),false);
                }
            }else if (VIDEOVIEW_CHANGE.equals(payloads.get(0))) {
                if (holder instanceof OtherViewHolder) {
                    MemberEntity item = list.get(position);
                    ((OtherViewHolder) holder).bind(item);
                } else if (holder instanceof SelfViewHolder) {
                    MemberEntity item = list.get(position);
                    ((SelfViewHolder) holder).bind(item);
                }
            }else if (NAME_CHANGE.equals(payloads.get(0))) {
                if (holder instanceof OtherViewHolder) {
                    MemberEntity item = list.get(position);
                    ((OtherViewHolder) holder).changeUserName(item);
                } else if (holder instanceof SelfViewHolder) {
                    MemberEntity item = list.get(position);
                    ((SelfViewHolder) holder).changeUserName(item);
                }
            }else if (ISHOST.equals(payloads.get(0))){
                if (holder instanceof OtherViewHolder) {
                    MemberEntity item = list.get(position);
                    ((OtherViewHolder) holder).showHost(item.isHost());
                } else if (holder instanceof SelfViewHolder) {
                    MemberEntity item = list.get(position);
                    ((SelfViewHolder) holder).showHost(item.isHost());
                }
            }else if (NAMEANDUSERHEAD_CHANGE.equals(payloads.get(0))){
                if (holder instanceof OtherViewHolder) {
                    MemberEntity item = list.get(position);
                    ((OtherViewHolder) holder).changeUserName(item);
                    ((OtherViewHolder) holder).showNoVideo(item.isMuteVideo(),true);
                } else if (holder instanceof SelfViewHolder) {
                    MemberEntity item = list.get(position);
                    ((SelfViewHolder) holder).changeUserName(item);
                    ((SelfViewHolder) holder).showNoVideo(item.isMuteVideo(),true);
                }
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_SELF : TYPE_OTHER;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface ListCallback {
        void onItemClick(int position);

        void onItemDoubleClick(int position);

        void onInfoClick(int position);
    }
}