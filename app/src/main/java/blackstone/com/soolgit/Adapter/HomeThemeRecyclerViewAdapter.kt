package blackstone.com.soolgit.Adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import blackstone.com.soolgit.DataClass.HotPlaceStoreData
import blackstone.com.soolgit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class HomeThemeRecyclerViewAdapter(val context: Context, var list: ArrayList<HotPlaceStoreData>?) : BaseQuickAdapter<HotPlaceStoreData, BaseViewHolder>(R.layout.main_home_content_theme_recyclerview_row, list) {

    override fun convert(helper: BaseViewHolder, item: HotPlaceStoreData) {
        helper.setText(R.id.main_home_content_theme_row_store_name_textview, item.StoreName)
        Glide.with(context)
                .load(item.StoreImg)
                .apply(RequestOptions.placeholderOf(ColorDrawable(ContextCompat.getColor(context, R.color.very_light_pink))))
                .apply(RequestOptions().centerCrop())
                .apply(RequestOptions.overrideOf(helper.getView<ImageView>(R.id.main_home_content_theme_row_store_image_imageview).width, helper.getView<ImageView>(R.id.main_home_content_theme_row_store_image_imageview).height))
                .into(helper.getView(R.id.main_home_content_theme_row_store_image_imageview))
    }

    override fun getItemCount(): Int = list!!.size

    open fun updateList(list: ArrayList<HotPlaceStoreData>) {
        this.list?.clear()
        this.list?.addAll(list)
        notifyDataSetChanged()
    }

}