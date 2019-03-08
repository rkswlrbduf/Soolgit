package blackstone.com.soolgit.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import blackstone.com.soolgit.DataClass.DongData
import blackstone.com.soolgit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.main_area_content_dong_recyclerview_row.view.*

class AreaDongRecyclerViewAdapter(val context: Context, val list: List<DongData>, val hash: HashMap<Int, Boolean>) : BaseQuickAdapter<DongData, BaseViewHolder>(R.layout.main_area_content_dong_recyclerview_row, list) {

    override fun convert(helper: BaseViewHolder, item: DongData) {
        if(hash[helper.position] != null) helper.setVisible(R.id.main_area_content_dong_row_selected_imageview, hash[helper.position]!!)
        else helper.setVisible(R.id.main_area_content_dong_row_selected_imageview, false)
        helper.setText(R.id.main_area_content_dong_row_textview, item.DongName)
        Glide.with(context)
                .load(item.DongImage)
                .apply(RequestOptions.placeholderOf(R.color.grey))
                .apply(RequestOptions().centerCrop())
                .apply(RequestOptions.overrideOf(helper.getView<ImageView>(R.id.main_area_content_dong_row_imageview).width, helper.getView<ImageView>(R.id.main_area_content_dong_row_imageview).height))
                .into(helper.getView(R.id.main_area_content_dong_row_imageview))
    }

    override fun getItemCount(): Int = list.size

}