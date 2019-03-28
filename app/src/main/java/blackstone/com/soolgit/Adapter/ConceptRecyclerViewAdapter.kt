package blackstone.com.soolgit.Adapter

import android.content.Context
import android.location.Location
import android.widget.ImageView
import blackstone.com.soolgit.DataClass.StoreData
import blackstone.com.soolgit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.gms.maps.model.LatLng

class ConceptRecyclerViewAdapter(val context: Context, val list : ArrayList<StoreData>, val location: Location): BaseQuickAdapter<StoreData, BaseViewHolder>(R.layout.main_home_concept_store_recyclerview_row, list) {

    override fun convert(helper: BaseViewHolder, item: StoreData) {
        val location = Location("")
        val point = LatLng(item.StorePointY!!, item.StorePointX!!)
        location.latitude = point.latitude
        location.longitude = point.longitude
        helper.setText(R.id.main_home_concept_row_store_name_textview, item.StoreName)
        helper.setText(R.id.main_home_concept_row_store_location_textview, item.StoreMLocation)
        helper.setText(R.id.main_home_concept_row_store_distance_textview, String.format("%.0f m", location.distanceTo(this.location)))
        helper.setText(R.id.main_home_concept_row_store_theme_textview, item.StoreTheme?.joinToString(" "))
        Glide.with(context)
                .load(item.StoreImage)
                .apply(RequestOptions.placeholderOf(R.color.grey))
                .apply(RequestOptions().centerCrop())
                .apply(RequestOptions.overrideOf(helper.getView<ImageView>(R.id.main_home_concept_row_store_imageview).width, helper.getView<ImageView>(R.id.main_home_concept_row_store_imageview).height))
                .into(helper.getView(R.id.main_home_concept_row_store_imageview))
        helper.setText(R.id.main_home_concept_row_store_heart_textview, item.StoreZzimCount)
    }

    open fun updateList(list: ArrayList<StoreData>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

}