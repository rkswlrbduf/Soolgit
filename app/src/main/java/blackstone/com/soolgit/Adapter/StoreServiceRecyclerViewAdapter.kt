package blackstone.com.soolgit.Adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import blackstone.com.soolgit.DataClass.StoreServiceData
import blackstone.com.soolgit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.store_service_recyclerview_row.view.*

class StoreServiceRecyclerViewAdapter(val context: Context, val list: ArrayList<StoreServiceData>?) : RecyclerView.Adapter<StoreServiceRecyclerViewAdapter.ViewHolder>() {

    interface ClickListener {
        fun onClick()
    }

    private lateinit var clickListener: ClickListener

    open fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title = view.store_service_row_textview
        val image = view.store_service_row_imageview

        init {
            view.setOnClickListener {
//                Toast.makeText(context, list!![adapterPosition].SERVICE_ID.toString(), Toast.LENGTH_SHORT).show()
                clickListener.onClick()
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.store_service_recyclerview_row, p0, false))
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.title.text = list!![p1].SERVICE_NM
        Glide.with(context)
                .load(list[p1].SERVICE_IMG)
                .apply(RequestOptions.placeholderOf(ColorDrawable(ContextCompat.getColor(context, R.color.very_light_pink))))
                .apply(RequestOptions.overrideOf(p0.image.width, p0.image.height))
                .into(p0.image)
    }

    override fun getItemCount(): Int = list?.size!!

    open fun updateList(list: ArrayList<StoreServiceData>) {
        this.list?.clear()
        this.list?.addAll(list)
        notifyDataSetChanged()
    }

}

//class StoreServiceRecyclerViewAdapter(val context: Context, val list: ArrayList<StoreServiceData>?): BaseQuickAdapter<StoreServiceData, BaseViewHolder>(R.layout.store_service_recyclerview_row, list) {
//
//    override fun convert(helper: BaseViewHolder, item: StoreServiceData) {
//        helper.setText(R.id.store_service_row_textview, item?.SERVICE_NM)
//        Glide.with(context)
//                .load(item.SERVICE_IMG)
//                .apply(RequestOptions.placeholderOf(ColorDrawable(ContextCompat.getColor(context, R.color.very_light_pink))))
//                .apply(RequestOptions.overrideOf(helper.getView<ImageView>(R.id.store_service_row_imageview).width, helper.getView<ImageView>(R.id.store_service_row_imageview).height))
//                .into(helper.getView(R.id.store_service_row_imageview))
//        item.TAG_NM?.forEach {
//            val textview: View = LayoutInflater.from(context).inflate(R.layout.store_service_textview, null, false)
//            textview.store_service_tag_textview.text = it
//            val lm = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            lm.setMargins(0,0,(context as StoreActivity).convertDpToPixel(10f, context).toInt(),0)
//            textview.layoutParams = lm
//            helper.getView<LinearLayout>(R.id.store_service_row_tag_container)?.addView(textview)
//        }
//        helper.addOnClickListener(R.id.store_service_row_container).addOnClickListener(R.id.store_service_row_imageview)
//
//    }
//
//    open fun updateList(list: ArrayList<StoreServiceData>) {
//        this.list?.clear()
//        this.list?.addAll(list)
//        notifyDataSetChanged()
//    }
//
//}