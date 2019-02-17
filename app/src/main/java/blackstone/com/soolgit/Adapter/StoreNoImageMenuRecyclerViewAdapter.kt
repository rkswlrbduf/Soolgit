package blackstone.com.soolgit.Adapter

import android.content.Context
import blackstone.com.soolgit.DataClass.StoreNoImageMenuData
import blackstone.com.soolgit.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class StoreNoImageMenuRecyclerViewAdapter(val context: Context, val list: ArrayList<StoreNoImageMenuData>?) : BaseQuickAdapter<StoreNoImageMenuData, BaseViewHolder>(R.layout.store_menu_recyclerview_row, list) {

    override fun convert(helper: BaseViewHolder, item: StoreNoImageMenuData) {
        helper.setText(R.id.store_menu_row_title_textview, item.MENU_NM)
        helper.setText(R.id.store_menu_row_cost_textview, item.MENU_COST)
    }

    open fun updateList(list: ArrayList<StoreNoImageMenuData>) {
        this.list?.clear()
        this.list?.addAll(list)
        notifyDataSetChanged()
    }

}