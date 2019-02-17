package blackstone.com.soolgit.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import blackstone.com.soolgit.DataClass.StoreData
import blackstone.com.soolgit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.main_area_content_store_recyclerview_row.view.*

class AreaStoreRecyclerViewAdapter(val context: Context, val list: List<StoreData>) : BaseQuickAdapter<StoreData, BaseViewHolder>(R.layout.main_area_content_store_recyclerview_row, list) {

//    inner class AreaStoreViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
//        var areaImage = itemview.main_area_content_dong_row_store_imageview
//        var areaName = itemview.main_area_content_dong_row_store_name_textview
//        var areaLocation = itemview.main_area_content_dong_row_store_location_textview
//        var areaDistance = itemview.main_area_content_dong_row_store_distance_textview
//        var areaTheme = itemview.main_area_content_dong_row_store_theme_textview
//        var areaHeart = itemview.main_area_content_dong_row_store_heart_textview
//        var areaService = itemview.main_area_content_dong_row_store_free_service_textview
//    }

    override fun convert(helper: BaseViewHolder, item: StoreData) {
        helper.setText(R.id.main_area_content_dong_row_store_name_textview, item.StoreName)
        helper.setText(R.id.main_area_content_dong_row_store_location_textview, item.StoreMLocation)
        helper.setText(R.id.main_area_content_dong_row_store_distance_textview, "500m")
        helper.setText(R.id.main_area_content_dong_row_store_theme_textview, item.StoreTheme?.joinToString(" "))
        Glide.with(context)
                .load(item.StoreImage)
                .apply(RequestOptions.placeholderOf(R.color.grey))
                .apply(RequestOptions().centerCrop())
                .apply(RequestOptions.overrideOf(helper.getView<ImageView>(R.id.main_area_content_dong_row_store_imageview).width, helper.getView<ImageView>(R.id.main_area_content_dong_row_store_imageview).height))
                .into(helper.getView(R.id.main_area_content_dong_row_store_imageview))
    }

    override fun getItemCount(): Int = list.size

}