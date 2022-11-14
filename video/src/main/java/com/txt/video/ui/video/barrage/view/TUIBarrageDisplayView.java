package com.txt.video.ui.video.barrage.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.txt.video.R;
import com.txt.video.TXSdk;
import com.txt.video.base.constants.IMkey;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.trtc.ticimpl.TICEventListener;
import com.txt.video.ui.video.barrage.model.TUIBarrageModel;
import com.txt.video.ui.video.barrage.presenter.ITUIBarragePresenter;
import com.txt.video.ui.video.barrage.presenter.TUIBarragePresenter;
import com.txt.video.ui.video.barrage.view.adapter.TUIBarrageMsgEntity;
import com.txt.video.ui.video.barrage.view.adapter.TUIBarrageMsgListAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 弹幕显示界面
 */
public class TUIBarrageDisplayView extends FrameLayout implements ITUIBarrageDisplayView {
    private static final String TAG = "TUIBarrageDisplayView";

    private Context                        mContext;
    private RecyclerView                   mRecyclerMsg;
    private TUIBarrageMsgListAdapter mAdapter;
    private ITUIBarragePresenter mPresenter;
    private ArrayList<TUIBarrageMsgEntity> mMsgList  = new ArrayList<>();
    private String                         mGroupId;

    public ArrayList<TUIBarrageMsgEntity> getmMsgList() {
        return mMsgList;
    }

    public void setmMsgList(ArrayList<TUIBarrageMsgEntity> mMsgList) {
        this.mMsgList = mMsgList;
    }

    public TUIBarrageDisplayView(Context context) {
        super(context);
    }

    public TUIBarrageDisplayView(Context context, String groupId) {
        this(context);
        this.mContext = context;
        this.mGroupId = groupId;
        initView(context);
        initPresenter();
    }

    private ITUIBarrageListener mBarrageListener;
    public void setReceiveBarrageListener(ITUIBarrageListener barrageListener) {
        TUIBarragePresenter.sharedInstance().addObserver(barrageListener);
        mBarrageListener = barrageListener;
    }

    private void initPresenter() {
//        mPresenter = new TUIBarragePresenter(mContext,""+123, mGroupId);
        mPresenter =  TUIBarragePresenter.sharedInstance();
        mPresenter.initDisplayView(this);
    }

    private void initView(Context context) {
        View baseView = LayoutInflater.from(context).inflate(R.layout.tx_tuibarrage_view_display, this);
        mRecyclerMsg = findViewById(R.id.rv_msg);

        //弹幕暂时不需要处理点击事件,因此点击回调设置为空
        mAdapter = new TUIBarrageMsgListAdapter(context, mMsgList, null);
        mRecyclerMsg.setLayoutManager(new LinearLayoutManager(context){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerMsg.setAdapter(mAdapter);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mPresenter != null) {
            mPresenter.destroyPresenter();
        }
        super.onDetachedFromWindow();
    }



    @Override
    public void receiveBarrage(TUIBarrageModel model) {
        if (model == null) {
            TxLogUtils.i( "receiveBarrage model is empty");
            return;
        }

        String message = model.content;
        TxLogUtils.i( "receiveBarrage message = " + message);
        if (message.length() == 0) {
            TxLogUtils.i( "receiveBarrage message is empty");
            return;
        }

        TUIBarrageMsgEntity entity = new TUIBarrageMsgEntity();
        entity.userName = model.userName;
        entity.content = message;
        entity.setSelf(model.userId == TXSdk.getInstance().getAgent());

        //用户名显示随机的颜色值
//        int index = new Random().nextInt(TUIBarrageConstants.MESSAGE_USERNAME_COLOR.length);
        entity.color = mContext.getResources().getColor( R.color.tx_color_e6b980);

        //接收到弹幕后,更新显示界面
        mMsgList.add(entity);
        mAdapter.notifyDataSetChanged();
        mRecyclerMsg.smoothScrollToPosition(mAdapter.getItemCount());
        if (null != mBarrageListener) {
            mBarrageListener.onSuccess(200,"123",model);
        }
        TUIBarragePresenter.sharedInstance().sedMsg(200,"123",model);
    }


}
