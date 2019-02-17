package blackstone.com.soolgit.Adapter

import android.content.Context
import blackstone.com.soolgit.DataClass.NoticeData
import blackstone.com.soolgit.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class NoticeRecyclerViewAdapter(val context: Context, val list: ArrayList<NoticeData>?): BaseQuickAdapter<NoticeData, BaseViewHolder>(R.layout.notice_recyclerview_row, list) {

    override fun convert(helper: BaseViewHolder, item: NoticeData) {
        helper.setText(R.id.notice_row_title_textview, item.NTC_NM)
        helper.setText(R.id.notice_row_time_textview, item.NTC_DT)
    }

    open fun updateList(list: ArrayList<NoticeData>) {
        this.list?.clear()
        this.list?.addAll(list)
        notifyDataSetChanged()
    }

}