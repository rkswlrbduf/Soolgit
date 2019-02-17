package blackstone.com.soolgit.Adapter

import android.content.Context
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

class MapStoreRecyclerViewAdapter(val context: Context, val list: List<MapData>) : BaseQuickAdapter<MapData, BaseViewHolder>(R.layout.main_map_content_store_recyclerview_row, list) {

    override fun convert(helper: BaseViewHolder, item: MapData) {
        helper.setText(R.id.main_map_content_row_store_name_textview, item.StoreName)
        helper.setText(R.id.main_map_content_row_store_call_textview, item.StoreCall)
        helper.setText(R.id.main_map_content_row_store_location_textview, item.StoreBLocation)
        Log.d("MapStoreRVA", "X: " + item.StorePointX + ", Y: " + item.StorePointY)
        Glide.with(context)
                .load("https://s3.ap-northeast-2.amazonaws.com/soolgitbucket01/background.png")
                .apply(RequestOptions.placeholderOf(R.color.grey))
                .apply(RequestOptions().centerCrop())
                .apply(RequestOptions.overrideOf(helper.getView<ImageView>(R.id.main_map_content_row_store_image_imageview).width, helper.getView<ImageView>(R.id.main_map_content_row_store_image_imageview).height))
                .into(helper.getView(R.id.main_map_content_row_store_image_imageview))
    }

    override fun getItemCount(): Int = list.size

}