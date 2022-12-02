package com.txt.video.ui.video.word.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.txt.video.R;
import com.txt.video.TXSdk;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.ui.video.word.presenter.ITxWordPresenter;
import com.txt.video.ui.video.word.presenter.TXWordPresenter;
import com.txt.video.ui.weight.dialog.WordSizePopup;
import com.txt.video.ui.weight.easyfloat.utils.DisplayUtils;

import java.util.ArrayList;

/**
 * 提词器显示界面
 */
public class TxWordDisplayView extends FrameLayout implements ITxWordDisplayView {
    private static final String TAG = "TUIBarrageDisplayView";

    private Context mContext;
    private TextView tv_text; //显示文字
    private TextView tv_size; //字体选择
    private ImageView tv_title1; //
    private TextView tv_title; //
    private ScrollView scrollview; //
    private ImageButton tx_switch_word; //显示提词器
    private ITxWordPresenter mPresenter;
    private String mGroupId;
    private LinearLayout ll_word;

    public TxWordDisplayView(Context context) {
        super(context);
    }

    public TxWordDisplayView(Context context, String groupId) {
        this(context);
        this.mContext = context;
        this.mGroupId = groupId;
        initView(context);
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new TXWordPresenter(mContext, "" + 123, mGroupId);
        mPresenter.initDisplayView(this);
    }

    private void initView(Context context) {
        View baseView = LayoutInflater.from(context).inflate(R.layout.tx_word_view_display, this);
        post(new Runnable() {
            @Override
            public void run() {
//                ViewGroup.LayoutParams layoutParams = getLayoutParams();
//                layoutParams.height = DisplayUtils.INSTANCE.getScreenHeight(getContext())/2 +50;
//                layoutParams.width =DisplayUtils.INSTANCE.getScreenHeight(getContext())/2  ;
//                setLayoutParams(layoutParams);
            }
        });
        tv_text = findViewById(R.id.tv_text);
        tv_size = findViewById(R.id.tv_size);
        tv_title1 = findViewById(R.id.tv_title1);
        tv_title = findViewById(R.id.tv_title);
        scrollview = findViewById(R.id.scrollview);
        ll_word = findViewById(R.id.ll_word);
        tv_size.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示字体大小
                WordSizePopup wordSizePopup = new WordSizePopup(tv_size, R.layout.tx_layout_popup_wordsize);
                wordSizePopup.setOnCheckDialogListener(new WordSizePopup.onWordSizePopupListener() {
                    @Override
                    public void onBigSize() {
                        tv_text.setTextSize(16f);
                    }

                    @Override
                    public void onMediumSize() {
                        tv_text.setTextSize(14f);
                    }

                    @Override
                    public void onSmallSize() {
                        tv_text.setTextSize(12f);
                    }
                });
                wordSizePopup.show();
            }
        });
        tx_switch_word = (ImageButton) findViewById(R.id.tx_switch_word);
        tv_title1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_text.getText().toString().isEmpty()){
                    return;
                }
                hideWordView(false);
                if (null != monOpenListener) {
                    monOpenListener.open(true);
                }

            }
        });
        tx_switch_word.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideWordView(true);
                if (null != monOpenListener) {
                    monOpenListener.open(false);
                }
            }
        });
    }
    private void hideWordView(boolean isHide){
        try {
            if (isHide) {
                //显示或者隐藏
                ll_word.setVisibility(GONE);
                tv_title1.setVisibility(VISIBLE);
                post(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup.LayoutParams layoutParams = getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        setLayoutParams(layoutParams);
                    }
                });
                isSelect = false;
            }else{
                ll_word.setVisibility(VISIBLE);
                tv_title1.setVisibility(GONE);

                changView();
                isSelect = true; //打开
            }
        }catch (Exception e){

        }
    }

    public void changView(){
        if (isLand) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = 500;
            layoutParams.width =DisplayUtils.INSTANCE.getScreenHeight(getContext())/2  ;
            setLayoutParams(layoutParams);
        }else{
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = DisplayUtils.INSTANCE.getScreenHeight(getContext())/4 ;
            layoutParams.width =  DisplayUtils.INSTANCE.getScreenWidth(getContext()) ;
            setLayoutParams(layoutParams);
        }
    }
    @Override
    protected void onDetachedFromWindow() {
        if (mPresenter != null) {
//            mPresenter.destroyPresenter();
        }
        super.onDetachedFromWindow();
    }

    @Override
    public void setContent(String content) {
        if (null != tv_text) {
            tv_text.setText(content);
            if (content.isEmpty()){
                tv_title1.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.tx_noword));
                hideWordView(true);
            }else{
                tv_title1.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.tx_hasword));
                hideWordView(false);
            }
        }
    }
   public String  getContent(){
        if (null != tv_text) {
          return   tv_text.getText().toString();
        }else {
            return  "";
        }
    }

    boolean isSelect = false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    boolean isLand = true;

    public void setLand(boolean isLand){
        this.isLand = isLand;
    }


    public interface onOpenListener{
        void open(boolean isOpen);
    }
    public onOpenListener monOpenListener;

    public void setOnPenListener(onOpenListener mOnOpenListener){
        this.monOpenListener = mOnOpenListener;
    }
}
