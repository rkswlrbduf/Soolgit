package blackstone.com.soolgit.Adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import blackstone.com.soolgit.DataClass.StoreNoImageMenuData
import blackstone.com.soolgit.DataClass.StorePlaceData
import blackstone.com.soolgit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class StorePlaceRecyclerViewAdapter(val context: Context, val list: ArrayList<StorePlaceData>?) : BaseQuickAdapter<StorePlaceData, BaseViewHolder>(R.layout.store_place_recyclerview_row, list) {

    override fun convert(helper: BaseViewHolder, item: StorePlaceData) {
        helper.setText(R.id.store_place_row_title_textview, item.STORE_NM)
        Glide.with(context)
                .load(item.IMG_PATH)
                .apply(RequestOptions.placeholderOf(ColorDrawable(ContextCompat.getColor(context, R.color.very_light_pink))))
                .apply(RequestOptions().centerCrop())
                .apply(RequestOptions.overrideOf(helper.getView<ImageView>(R.id.store_place_row_image_imageview).width, helper.getView<ImageView>(R.id.store_place_row_image_imageview).height))
                .into(helper.getView(R.id.store_place_row_image_imageview))
    }

    open fun updateList(list: ArrayList<StorePlaceData>) {
        this.list?.clear()
        this.list?.addAll(list)
        notifyDataSetChanged()
    }

}