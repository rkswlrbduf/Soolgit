package blackstone.com.soolgit.Adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import blackstone.com.soolgit.DataClass.MapData
import blackstone.com.soolgit.DataClass.StoreData
import blackstone.com.soolgit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.main_area_content_store_recyclerview_row.view.*

class MapStoreRecyclerViewAdapter(val context: Context, val list: ArrayList<StoreData>) : BaseQuickAdapter<StoreData, BaseViewHolder>(R.layout.main_map_content_store_recyclerview_row, list) {

    override fun convert(helper: BaseViewHolder, item: StoreData) {
        helper.setText(R.id.main_map_content_row_store_name_textview, item.StoreName)
        helper.setText(R.id.main_map_content_row_store_call_textview, item.StoreCall)
        helper.setText(R.id.main_map_content_row_store_location_textview, item.StoreBLocation)
        Glide.with(context)
                .load(item.StoreImage)
                .apply(RequestOptions.placeholderOf(ColorDrawable(ContextCompat.getColor(context, R.color.very_light_pink))))
                .apply(RequestOptions().centerCrop())
                .apply(RequestOptions.overrideOf(helper.getView<ImageView>(R.id.main_map_content_row_store_image_imageview).width, helper.getView<ImageView>(R.id.main_map_content_row_store_image_imageview).height))
                .into(helper.getView(R.id.main_map_content_row_store_image_imageview))
    }

    override fun getItemCount(): Int = list.size

    open fun updateList(list: ArrayList<StoreData>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

}