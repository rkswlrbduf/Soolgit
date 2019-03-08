package blackstone.com.soolgit.Adapter

import android.content.Context
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView
import blackstone.com.soolgit.DataClass.StoreData
import blackstone.com.soolgit.DataClass.ThemeData
import blackstone.com.soolgit.R
import blackstone.com.soolgit.Util.MyUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class FilterThemeRecyclerViewAdapter(val context: Context, val list: ArrayList<ThemeData>): BaseQuickAdapter<ThemeData, BaseViewHolder>(R.layout.filter_theme_recyclerview_row, list) {

    private var mUtil: MyUtil = MyUtil(context)

    override fun convert(helper: BaseViewHolder, item: ThemeData) {
        Glide.with(context)
                .load(item.THEME_IMG)
                .apply(RequestOptions.placeholderOf(R.color.white))
                .apply(RequestOptions().centerCrop())
                .apply(RequestOptions.overrideOf(helper.getView<ImageView>(R.id.filter_theme_row_icon_imageView).width, helper.getView<ImageView>(R.id.filter_theme_row_icon_imageView).height))
                .into(helper.getView(R.id.filter_theme_row_icon_imageView))
        helper.setText(R.id.filter_theme_row_textView, item.THEME_NM)
        helper.addOnClickListener(R.id.filter_theme_row_container)

        if(mUtil.FILTERTHEME!![item.THEME_ID] != null) {
            helper.getView<ImageView>(R.id.filter_theme_row_icon_imageView).setColorFilter(ContextCompat.getColor(context, R.color.marigold), PorterDuff.Mode.SRC_IN)
            helper.getView<TextView>(R.id.filter_theme_row_textView).setTextColor(ContextCompat.getColor(context, R.color.marigold))
        }

    }

    open fun updateList(list: ArrayList<ThemeData>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

}