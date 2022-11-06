package com.txt.video.ui.video.barrage.view.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.txt.video.R;

import java.util.List;

public class TUIBarrageMsgListAdapter extends RecyclerView.Adapter<TUIBarrageMsgListAdapter.ViewHolder> {
    private static final String TAG = "TUIBarrageMsgListAdapter";

    private List<TUIBarrageMsgEntity> mMsgEntityList;
    private OnItemClickListener mOnItemClickListener;

    public TUIBarrageMsgListAdapter(Context context, List<TUIBarrageMsgEntity> msgEntityList,
                                    OnItemClickListener onItemClickListener) {
        this.mMsgEntityList = msgEntityList;
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tx_tuibarrage_item_msg, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TUIBarrageMsgEntity item = mMsgEntityList.get(position);
        holder.bind(item, mOnItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mMsgEntityList.size();
    }

    public interface OnItemClickListener {
        void onAgreeClick(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvMsgContent;

        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            mTvMsgContent = (TextView) itemView.findViewById(R.id.tv_msg_content);
        }

        public void bind(final TUIBarrageMsgEntity model,
                         final OnItemClickListener listener) {
            String userName = TextUtils.isEmpty(model.userName) ? model.userId : model.userName;

//            boolean contains = model.content.contains("U+");
            String result = "";
//            if (contains) {
//                int hex = Integer.parseInt(model.content.replace("U+", ""), 16);
//                //将当前 16 进制数转换成字符数组
//                char[] chars = Character.toChars(hex);
//                //将当前字符数组转换成 TextView 可加载的 String 字符串
//                String mEmojiString =new  String(chars);
//                result = userName + ": " + mEmojiString;
//            } else {
//
//            }
            result = userName + ": " + model.content;
            SpannableStringBuilder builder = new SpannableStringBuilder(result);
            ForegroundColorSpan redSpan = new ForegroundColorSpan(model.color);
            if (TextUtils.isEmpty(userName)) {
                userName = "";
            }
            builder.setSpan(redSpan, 0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            mTvMsgContent.setText(builder);

        }
    }
}