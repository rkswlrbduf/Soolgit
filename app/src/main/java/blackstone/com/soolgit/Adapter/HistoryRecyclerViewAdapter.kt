package blackstone.com.soolgit.Adapter

import android.content.Context
import blackstone.com.soolgit.DataClass.HistoryData
import blackstone.com.soolgit.DataClass.NoticeData
import blackstone.com.soolgit.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class HistoryRecyclerViewAdapter(val context: Context, val list: ArrayList<HistoryData>?): BaseQuickAdapter<HistoryData, BaseViewHolder>(R.layout.history_recyclerview_row, list) {

    override fun convert(helper: BaseViewHolder, item: HistoryData) {
        helper.setText(R.id.history_row_title_textview, item.STORE_M_LCN)
        helper.setText(R.id.history_row_location_textview, item.STORE_NM)
        helper.setText(R.id.history_row_menu_textview, item.SERVICE_NM)
        helper.setText(R.id.history_row_time_textview, item.ORDER_DT)
        helper.setText(R.id.history_row_cost_textview, item.SERVICE_COST)
    }

    open fun updateList(list: ArrayList<HistoryData>) {
        this.list?.clear()
        this.list?.addAll(list)
        notifyDataSetChanged()
    }

}