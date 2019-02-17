package blackstone.com.soolgit.Adapter

import android.content.Context
import android.widget.ImageView
import blackstone.com.soolgit.DataClass.StoreData
import blackstone.com.soolgit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SearchContentRecyclerViewAdapter(val context: Context, var list: ArrayList<StoreData>?) : BaseQuickAdapter<StoreData, BaseViewHolder>(R.layout.search_content_recyclerview_row, list) {

    override fun convert(helper: BaseViewHolder, item: StoreData) {
        helper.setText(R.id.search_content_dong_row_store_name_textview, item.StoreName)
        helper.setText(R.id.search_content_dong_row_store_location_textview, item.StoreMLocation)
        helper.setText(R.id.search_content_dong_row_store_distance_textview, "500m")
        helper.setText(R.id.search_content_dong_row_store_theme_textview, item.StoreTheme?.joinToString(" "))
        Glide.with(context)
                .load(item.StoreImage)
                .apply(RequestOptions.placeholderOf(R.color.grey))
                .apply(RequestOptions().centerCrop())
                .apply(RequestOptions.overrideOf(helper.getView<ImageView>(R.id.search_content_dong_row_store_imageview).width, helper.getView<ImageView>(R.id.search_content_dong_row_store_imageview).height))
                .into(helper.getView(R.id.search_content_dong_row_store_imageview))
    }

    open fun updateList(list: ArrayList<StoreData>) {
        this.list?.clear()
        this.list?.addAll(list)
        notifyDataSetChanged()
    }

}