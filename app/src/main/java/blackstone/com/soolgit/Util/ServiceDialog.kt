package blackstone.com.soolgit.Util

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.widget.Toast
import blackstone.com.soolgit.DataClass.HotPlaceStoreData
import blackstone.com.soolgit.DataClass.StoreDetailData
import blackstone.com.soolgit.DataClass.StoreServiceData
import blackstone.com.soolgit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.dialog_service.*

class ServiceDialog(val activity: Activity, val item: StoreServiceData, val storeData: StoreDetailData?) : Dialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_service)

        service_dialog_menu_textview.text = item.SERVICE_NM
        Glide.with(context)
                .load(item.SERVICE_IMG)
                .apply(RequestOptions.placeholderOf(ColorDrawable(ContextCompat.getColor(context, R.color.very_light_pink))))
                .apply(RequestOptions.overrideOf(service_dialog_menu_imageview.width, service_dialog_menu_imageview.height))
                .into(service_dialog_menu_imageview)
        service_dialog_location_textview.text = storeData?.STORE_NM
        service_dialog_close_imagebutton.setOnClickListener { dismiss() }

        window.setBackgroundDrawableResource(android.R.color.transparent)
        val displayMetrics = DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels * 271 / 360
        var height = width
        window.setLayout(width, height)

        service_dialog_pinview.setPinViewEventListener {pinview: Pinview, b: Boolean ->

        }

    }

}