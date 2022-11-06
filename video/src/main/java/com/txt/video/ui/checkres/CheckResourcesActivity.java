package com.txt.video.ui.checkres;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.txt.video.R;
import com.txt.video.base.BaseActivity;
import com.txt.video.common.adapter.base.TxBaseQuickAdapter;
import com.txt.video.ui.weight.dialog.TxSearchcriteriaDialog;
import com.txt.video.net.bean.ResourceTypeBean;
import com.txt.video.net.bean.ResourcegsConditionsBean;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.ui.weight.adapter.ScreenShareResLeftAdapter;
import com.txt.video.ui.weight.adapter.ScreenShareResourcesAdapter1;
import com.txt.video.ui.weight.adapter.ScreenShareResourcesResultAdapter;

import java.util.ArrayList;

/**
 * author ：Justin
 * time ：2021/3/17.
 * des ：选择共享页面，暂时不做
 */
public class CheckResourcesActivity extends BaseActivity<CheckResourcesContract.ICollectView, CheckResourcesPresenter>
        implements CheckResourcesContract.ICollectView {

    @Override
    protected int getContentViewId() {
        return R.layout.tx_activity_check_resources;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        hideStatusBar();
        initData();
        initView();
        clickEt(false);
    }

    //点击输入框，x，搜索显示，左边框隐藏，第一个list隐藏
    private void clickEt(boolean showSearch) {
        if (showSearch) {
            ll_close.setVisibility(View.GONE);
            rv_zhany.setVisibility(View.GONE);
            ll_left.setVisibility(View.GONE);
            rv_zhany.setVisibility(View.GONE);
            tv_cancel.setVisibility(View.VISIBLE);
            iv_back.setVisibility(View.VISIBLE);
            //sv_search 设置布局中间，宽度设置为屏幕的一半
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sv_search.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            layoutParams.width = getResources().getDisplayMetrics().widthPixels / 2;
            sv_search.setLayoutParams(layoutParams);
        }else{
            ll_close.setVisibility(View.VISIBLE);
            rv_zhany.setVisibility(View.VISIBLE);
            ll_left.setVisibility(View.VISIBLE);
            rv_zhany.setVisibility(View.VISIBLE);
            tv_cancel.setVisibility(View.GONE);
            iv_back.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sv_search.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT ;
        }

        hideStatusBar();


    }

    LinearLayout ll_close;
    LinearLayout ll_left;
    LinearLayout sv_search;
    RecyclerView rv_zhany;
    RecyclerView rv_zy;
    RecyclerView rv_left;
    TextView tv_cancel;
    ImageView iv_back;

    private void initView() {
        rv_zhany = findViewById(R.id.rv_zhany);
        rv_left = findViewById(R.id.rv_left);
        ll_left = findViewById(R.id.ll_left);
        sv_search = findViewById(R.id.sv_search);

        //弹出的取消按钮
        tv_cancel = findViewById(R.id.tv_cancel);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEt(false);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEt(false);
            }
        });
        EditText et_search = findViewById(R.id.et_search);
        ll_close = findViewById(R.id.ll_close);
        ImageButton tx_business_close = findViewById(R.id.tx_business_close);
        ImageButton tx_search = findViewById(R.id.tx_search);
        rv_zy = findViewById(R.id.rv_zy);
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEt(true);
            }
        });
        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TxLogUtils.i("hasFocus" + "----" + hasFocus);

            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                TxLogUtils.i("beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TxLogUtils.i("onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                TxLogUtils.i("afterTextChanged");
            }
        });

        tx_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TxSearchcriteriaDialog.Builder(CheckResourcesActivity.this).show();
            }
        });
        tx_business_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //右边头部的rv
        ScreenShareResourcesAdapter1 screenShareResourcesAdapter1 = new ScreenShareResourcesAdapter1();
        rv_zhany.setAdapter(screenShareResourcesAdapter1);
        screenShareResourcesAdapter1.setNewData(zhanyAl);
        screenShareResourcesAdapter1.setOnItemClickListener(new TxBaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TxBaseQuickAdapter adapter, View view, int position) {
                for (ResourcegsConditionsBean item : zhanyAl) {
                    if (position ==  zhanyAl.indexOf(item)) {
                        item.setCheck(true);
                    }else{
                        item.setCheck(false);
                    }
                }
                screenShareResourcesAdapter1.notifyDataSetChanged();
            }
        });
        //右边下面的资源rv
        ScreenShareResourcesResultAdapter screenShareResourcesAdapter = new ScreenShareResourcesResultAdapter();
        rv_zy.setLayoutManager(new GridLayoutManager(this, 3));
        rv_zy.setAdapter(screenShareResourcesAdapter);
        screenShareResourcesAdapter.setNewData(zyAl);

        //左边的rv
        ScreenShareResLeftAdapter screenShareResLeftAdapter = new ScreenShareResLeftAdapter();
        rv_left.setAdapter(screenShareResLeftAdapter);
        screenShareResLeftAdapter.setNewData(leftRvAl);
        screenShareResLeftAdapter.setOnItemClickListener(new TxBaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TxBaseQuickAdapter adapter, View view, int position) {
                for (ResourcegsConditionsBean item : leftRvAl) {
                    if (position ==  leftRvAl.indexOf(item)) {
                        item.setCheck(true);
                    }else{
                        item.setCheck(false);
                    }
                }
                screenShareResLeftAdapter.notifyDataSetChanged();
            }
        });
    }

    ArrayList<ResourcegsConditionsBean> zhanyAl;
    ArrayList zyAl;
    ArrayList<ResourcegsConditionsBean> leftRvAl;

    private void initData() {
        zhanyAl = new ArrayList<ResourcegsConditionsBean>();
        zhanyAl.add(new ResourcegsConditionsBean("讲理念", "1",true));
        zhanyAl.add(new ResourcegsConditionsBean("讲方案", "1",false));
        zhanyAl.add(new ResourcegsConditionsBean("讲产品", "1",false));
        zhanyAl.add(new ResourcegsConditionsBean("讲建议书", "1",false));
        zhanyAl.add(new ResourcegsConditionsBean("讲懂你保险", "1",false));
        zhanyAl.add(new ResourcegsConditionsBean("讲保障规划", "1",false));
        zhanyAl.add(new ResourcegsConditionsBean("讲理赔", "1",false));
        zhanyAl.add(new ResourcegsConditionsBean("讲服务", "1",false));

        zyAl = new ArrayList<ResourceTypeBean>();
        zyAl.add(new ResourceTypeBean("讲理念","https://www.baidu.com/img/bd_logo.png"));
        zyAl.add(new ResourceTypeBean("讲方案"));
        zyAl.add(new ResourceTypeBean("讲产品"));
        zyAl.add(new ResourceTypeBean("讲建议书"));
        zyAl.add(new ResourceTypeBean("讲懂你保险"));
        zyAl.add(new ResourceTypeBean("讲保障规划"));
        zyAl.add(new ResourceTypeBean("讲理赔"));
        zyAl.add(new ResourceTypeBean("讲服务"));

        leftRvAl = new ArrayList<ResourcegsConditionsBean>();
        leftRvAl.add(new ResourcegsConditionsBean("展业","",true));
        leftRvAl.add(new ResourcegsConditionsBean("增员","",false));

    }


    @Nullable
    @Override
    protected CheckResourcesPresenter createPresenter() {
        return new CheckResourcesPresenter(this, this);
    }
}