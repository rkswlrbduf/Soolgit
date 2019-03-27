package blackstone.com.soolgit.Adapter

import android.content.Context
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView
import blackstone.com.soolgit.DataClass.CategoryData
import blackstone.com.soolgit.DataClass.StoreData
import blackstone.com.soolgit.DataClass.ThemeData
import blackstone.com.soolgit.R
import blackstone.com.soolgit.Util.MyUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class FilterCategoryRecyclerViewAdapter(val context: Context, val list: ArrayList<CategoryData>): BaseQuickAdapter<CategoryData, BaseViewHolder>(R.layout.filter_category_recyclerview_row, list) {

    private var mUtil: MyUtil = MyUtil(context)

    override fun convert(helper: BaseViewHolder, item: CategoryData) {
        Glide.with(context)
                .load(item.CATEGORY_IMG)
                .apply(RequestOptions.placeholderOf(R.color.white))
                .apply(RequestOptions().centerCrop())
                .apply(RequestOptions.overrideOf(helper.getView<ImageView>(R.id.filter_category_row_icon_imageView).width, helper.getView<ImageView>(R.id.filter_category_row_icon_imageView).height))
                .into(helper.getView(R.id.filter_category_row_icon_imageView))
        helper.setText(R.id.filter_category_row_textView, item.CATEGORY_NM)
        helper.addOnClickListener(R.id.filter_category_row_container)

        mUtil.FILTERCATEGORY.forEach {
            if(it.CATEGORY_ID == item.CATEGORY_ID) {
                helper.getView<ImageView>(R.id.filter_category_row_icon_imageView).setColorFilter(ContextCompat.getColor(context, R.color.marigold), PorterDuff.Mode.SRC_IN)
                helper.getView<TextView>(R.id.filter_category_row_textView).setTextColor(ContextCompat.getColor(context, R.color.marigold))
                item.CATEGORY_CHECKED = true
                return@forEach
            }
        }
    }

    open fun updateList(list: ArrayList<CategoryData>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

}