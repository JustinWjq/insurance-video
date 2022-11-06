package com.txt.video.ui.video.word.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

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
    private TextView tv_title1; //
    private TextView tv_title; //
    private ScrollView scrollview; //
    private ImageButton tx_switch_word; //显示提词器
    private ITxWordPresenter mPresenter;
    private String mGroupId;

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
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParams.width =DisplayUtils.INSTANCE.getScreenHeight(getContext())/2  ;
                setLayoutParams(layoutParams);
            }
        });
        tv_text = findViewById(R.id.tv_text);
        tv_size = findViewById(R.id.tv_size);
        tv_title1 = findViewById(R.id.tv_title1);
        tv_title = findViewById(R.id.tv_title);
        scrollview = findViewById(R.id.scrollview);
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
        tx_switch_word.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示或者隐藏
                boolean selected = tx_switch_word.isSelected();
                if (selected) { //如果是选中状态，就是关闭提词器
                    tv_title.setVisibility(VISIBLE);
                    tv_size.setVisibility(VISIBLE);
                    tv_title1.setVisibility(GONE);
                    scrollview.setVisibility(VISIBLE);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            ViewGroup.LayoutParams layoutParams = getLayoutParams();
                            layoutParams.height =DisplayUtils.INSTANCE.getScreenHeight(getContext())/2  ;
                            layoutParams.width =DisplayUtils.INSTANCE.getScreenHeight(getContext())/2  ;
                            setLayoutParams(layoutParams);
                        }
                    });
                } else {
                    //默认是开启状态
                    tv_title.setVisibility(GONE);
                    tv_size.setVisibility(GONE);
                    tv_title1.setVisibility(VISIBLE);
                    scrollview.setVisibility(GONE);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            ViewGroup.LayoutParams layoutParams = getLayoutParams();
                            layoutParams.height = tx_switch_word.getHeight()+20;
                            layoutParams.width = layoutParams.width/2 ;
                            setLayoutParams(layoutParams);
                        }
                    });

                }
                tx_switch_word.setSelected(!selected);
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mPresenter != null) {
            mPresenter.destroyPresenter();
        }
        super.onDetachedFromWindow();
    }

    @Override
    public void setContent(String content) {
        if (null != tv_text) {
            tv_text.setText(content);
        }
    }
}
