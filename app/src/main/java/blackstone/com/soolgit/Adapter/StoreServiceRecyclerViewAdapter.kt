package blackstone.com.soolgit.Adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import blackstone.com.soolgit.DataClass.StoreServiceData
import blackstone.com.soolgit.R
import blackstone.com.soolgit.StoreActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.store_service_recyclerview_row.view.*
import kotlinx.android.synthetic.main.store_service_textview.view.*

class StoreServiceRecyclerViewAdapter(val context: Context, val list: ArrayList<StoreServiceData>?) : RecyclerView.Adapter<StoreServiceRecyclerViewAdapter.ViewHolder>() {

    interface ClickListener {
        fun onClick(position: Int)
    }

    private lateinit var clickListener: ClickListener

    open fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title = view.store_service_row_textview
        val image = view.store_service_row_imageview
        val container = view.store_service_row_tag_container

        init {
            view.setOnClickListener {
                clickListener.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.store_service_recyclerview_row, p0, false))
    }

    override fun onBindViewHolder(view: ViewHolder, position: Int) {
        view.title.text = list!![position].SERVICE_NM
        Glide.with(context)
                .load(list[position].SERVICE_IMG)
                .apply(RequestOptions.placeholderOf(ColorDrawable(ContextCompat.getColor(context, R.color.very_light_pink))))
                .apply(RequestOptions.overrideOf(view.image.width, view.image.height))
                .into(view.image)
        list[position].TAG_NM?.forEach {
            val textView: View = LayoutInflater.from(context).inflate(R.layout.store_service_textview, null, false)
            textView.store_service_tag_textview.text = it
            val lm = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lm.setMargins(0, 0, (context as StoreActivity).convertDpToPixel(10f, context).toInt(), 0)
            textView.layoutParams = lm
            view.container.addView(textView)
        }
    }

    override fun getItemCount(): Int = list?.size!!

    open fun updateList(list: ArrayList<StoreServiceData>) {
        this.list?.clear()
        this.list?.addAll(list)
        notifyDataSetChanged()
    }

}