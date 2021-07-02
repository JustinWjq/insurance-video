package com.txt.video.ui.weight.adapter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.widget.RelativeLayout
import com.txt.video.R
import com.txt.video.common.adapter.base.TxBaseQuickAdapter
import com.txt.video.common.adapter.base.TxBaseViewHolder
import com.txt.video.common.utils.DatetimeUtil
import com.txt.video.net.bean.WebUrlBean
import java.text.ParseException

/**
 * Created by JustinWjq
 * @date 2021/2/4.
 * description：
 */
public class WebUrlListAdapter : TxBaseQuickAdapter<WebUrlBean.ListBean, TxBaseViewHolder>(R.layout.tx_adapter_file_list_item) {
    override fun convert(helper: TxBaseViewHolder, item: WebUrlBean.ListBean?) {
        helper.setImageResource(R.id.tx_iv_file_pic, R.drawable.tx_icon_h5)

        helper.setText(R.id.tx_tv_file_name, item!!.name)

        try {
            helper.setText(
                R.id.tx_tv_file_time,
                DatetimeUtil.UTCToCST(item.ctime)
            )
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

}