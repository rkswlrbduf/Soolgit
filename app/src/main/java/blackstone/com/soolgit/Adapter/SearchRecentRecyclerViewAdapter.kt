package blackstone.com.soolgit.Adapter

import blackstone.com.soolgit.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SearchRecentRecyclerViewAdapter(var list: ArrayList<String>?) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.search_recent_recyclerview_row, list) {

    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.search_row_recent_textview,item)
                .addOnClickListener(R.id.search_row_delete_imagebutton)
                .addOnClickListener(R.id.search_row_recent_textview)
    }

}