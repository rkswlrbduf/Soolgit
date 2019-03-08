package blackstone.com.soolgit.Adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import blackstone.com.soolgit.DataClass.StoreDetailData
import blackstone.com.soolgit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class StoreImageSliderAdapter(private val context: Context, private var list: ArrayList<String>?) : PagerAdapter() {

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int = list!!.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = ImageView(context)
        Glide.with(context)
                .load(list!![position])
                .apply(RequestOptions().centerCrop())
                .apply(RequestOptions.overrideOf(view.width, view.height))
                .apply(RequestOptions.placeholderOf(ColorDrawable(ContextCompat.getColor(context, R.color.very_light_pink))))
                .into(view)
        container.addView(view)
        return view
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? = null

    fun updateList(list: ArrayList<String>?) {
        this.list = list
        notifyDataSetChanged()
    }

}