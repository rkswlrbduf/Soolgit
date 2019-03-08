package blackstone.com.soolgit.Adapter

import android.content.Context
import blackstone.com.soolgit.DataClass.HistoryData
import blackstone.com.soolgit.DataClass.NoticeData
import blackstone.com.soolgit.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class HistoryRecyclerViewAdapter(val context: Context, val list: ArrayList<HistoryData>?): BaseQuickAdapter<HistoryData, BaseViewHolder>(R.layout.history_recyclerview_row, list) {

    override fun convert(helper: BaseViewHolder, item: HistoryData) {

    }

    open fun updateList(list: ArrayList<HistoryData>) {
        this.list?.clear()
        this.list?.addAll(list)
        notifyDataSetChanged()
    }

}