package com.txt.video.ui.weight.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.txt.video.R;
import com.txt.video.common.adapter.base.TxBaseQuickAdapter;
import com.txt.video.common.adapter.decoration.DividerItemDecoration;
import com.txt.video.common.dialog.common.TxBaseDialog;
import com.txt.video.common.dialog.common.TxCommonDialog;
import com.txt.video.common.utils.AppUtils;
import com.txt.video.common.utils.DatetimeUtil;
import com.txt.video.net.bean.UserInfoBean;
import com.txt.video.net.http.HttpRequestClient;
import com.txt.video.net.http.SystemHttpRequest;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.ui.weight.adapter.UserInfoDialogAdapter;
import com.txt.video.ui.weight.adapter.UserInfoDynamicDialogAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Justin
 * time ：6/25/21.
 * des ：展示用户信息
 */
public final class TxInfoDialog {

    public static final class Builder
            extends TxCommonDialog.Builder<Builder>
            implements TxBaseDialog.OnShowListener,
            TextView.OnEditorActionListener {

        private OnListener mListener;
        private RecyclerView rv_file;
        private UserInfoDynamicDialogAdapter mUserInfoDynamicDialogAdapter;

        public Builder(Context context) {
            super(context);
            setGravity(Gravity.BOTTOM);
            setThemeStyle(R.style.tx_MyDialog);
            setContentView(R.layout.tx_layout_userinfo);
            setAnimStyle(TxBaseDialog.ANIM_DEFAULT);
            setWidth(context.getResources().getDisplayMetrics().widthPixels);
            setHeight(context.getResources().getDisplayMetrics().heightPixels / 2);
            rv_file = findViewById(R.id.rv_file);
            TabLayout detaile_tablayout = findViewById(R.id.detaile_tablayout);
            ImageView iv_close = findViewById(R.id.iv_close);
            iv_close.setOnClickListener(this);
            mUserInfoDynamicDialogAdapter = new UserInfoDynamicDialogAdapter();
            mUserInfoDynamicDialogAdapter.setEmptyView( LayoutInflater.from(getContext()).inflate(R.layout.tx_view_info_empty,null));
            mUserInfoDynamicDialogAdapter.setOnLoadMoreListener(new TxBaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    if (next_paging_id.isEmpty()){
                        mUserInfoDynamicDialogAdapter.loadMoreEnd();
                    }else{
                        getDynamicData();
                    }
                }
            },rv_file);

            detaile_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getText().toString().equals("客户动态")) {
                        //刷新
                        next_paging_id= "";
                        ArrayList zhanyAl = new ArrayList<UserInfoBean>();

                        mUserInfoDynamicDialogAdapter.setNewData(zhanyAl);
                        rv_file.setAdapter(mUserInfoDynamicDialogAdapter);
                        getDynamicData();
                    } else {
                        next_paging_id= "";
                        setUserInfo();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }


        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        private String info;

        public Builder setInfo(String info) {
            this.info = info;
            setUserInfo();
            return this;
        }

        private String userId;

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public String cid;

        private void setUserInfo() {
            ArrayList zhanyAl = new ArrayList<UserInfoBean>();
            try {
                JSONObject jsonObject = new JSONObject(info);
                cid = jsonObject.optString("cid");
                zhanyAl.add(new UserInfoBean("真实姓名", jsonObject.optString("fn").isEmpty() ? "--" : jsonObject.optString("fn")));
                zhanyAl.add(new UserInfoBean("备注名", jsonObject.optString("alias").isEmpty() ? "--" : jsonObject.optString("alias")));
                zhanyAl.add(new UserInfoBean("性别", jsonObject.optString("sex").isEmpty() ? "--" : jsonObject.optString("sex")));
                zhanyAl.add(new UserInfoBean("生日", jsonObject.optString("birthday").isEmpty() ? "--" : jsonObject.optString("birthday")));
                JSONArray tels = jsonObject.optJSONArray("tels");
                StringBuilder stringBuilder = new StringBuilder("");
                for (int i = 0; i < tels.length(); i++) {
                    if (i==tels.length()-1){
                        stringBuilder.append(tels.getString(i));
                    }else{
                        stringBuilder.append(tels.getString(i)+"，");
                    }

                }
                zhanyAl.add(new UserInfoBean("手机", stringBuilder.toString().isEmpty() ? "--" :stringBuilder.toString()));
                zhanyAl.add(new UserInfoBean("邮箱", jsonObject.optString("email").isEmpty() ? "--" : jsonObject.optString("email")));
                zhanyAl.add(new UserInfoBean("详细地址", jsonObject.optString("adr").isEmpty() ? "--" : jsonObject.optString("adr")));
            } catch (Exception e) {
                e.printStackTrace();
            }

            UserInfoDialogAdapter screenShareResourcesAdapter = new UserInfoDialogAdapter();
            screenShareResourcesAdapter.setEmptyView( LayoutInflater.from(getContext()).inflate(R.layout.tx_view_info_empty,null));
            screenShareResourcesAdapter.setNewData(zhanyAl);
            rv_file.setAdapter(screenShareResourcesAdapter);
        }
        String next_paging_id = "";
        private void getDynamicData() {
            SystemHttpRequest.getInstance().customerAction(
                    userId,
                    cid,
                    next_paging_id,
                    new HttpRequestClient.RequestHttpCallBack() {

                        @Override
                        public void onSuccess(String json) {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                next_paging_id = jsonObject.optString("next_paging_id");

                                AppUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        List<UserInfoBean> data = new ArrayList<>();
                                        if (jsonObject.has("list")) {
                                            JSONArray list = jsonObject.optJSONArray("list");
                                            for (int i = 0; i < list.length(); i++) {
                                                try {
                                                    JSONObject jsonObject1 = list.getJSONObject(i);
                                                    long dateLong = jsonObject1.getLong("date");
                                                    JSONObject content = jsonObject1.getJSONObject("content");
                                                    if (null != content) {
                                                        String content1 = content.getString("content");
                                                        if (null != content1) {
                                                            String dateStr = DatetimeUtil.INSTANCE.formatDate(dateLong, DatetimeUtil.INSTANCE.getDATE_PATTERN_MM());
                                                            data.add(new UserInfoBean(dateStr, content1));
                                                        }
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    TxLogUtils.d(e.getMessage());
                                                }


                                            }
                                        }

                                        mUserInfoDynamicDialogAdapter.addData(data);
                                        mUserInfoDynamicDialogAdapter.loadMoreComplete();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                                TxLogUtils.d(e.getMessage());
                            }
                        }

                        @Override
                        public void onFail(String err, int code) {
                            //加载失败
                            AppUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mUserInfoDynamicDialogAdapter.loadMoreFail();
                                }
                            });
                        }
                    }
            );
        }

        /**
         * {@link TxBaseDialog.OnShowListener}
         */
        @Override
        public void onShow(TxBaseDialog dialog) {
            //  postDelayed(() -> showKeyboard(mContext), 500);
        }

        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.tv_ui_confirm) {
                //重置
            } else if (viewId == R.id.tv_ui_cancel) {
                autoDismiss();
                if (mListener != null) {
                    mListener.onCancel(getDialog());
                }
            } else if (viewId == R.id.iv_close) {
                autoDismiss();
            }
        }

        /**
         * {@link TextView.OnEditorActionListener}
         */
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // 模拟点击确认按钮
                onClick(findViewById(R.id.tv_ui_confirm));
                return true;
            }
            return false;
        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(TxBaseDialog dialog, String name, String url);

        /**
         * 点击取消时回调
         */
        default void onCancel(TxBaseDialog dialog) {
        }
    }
}
