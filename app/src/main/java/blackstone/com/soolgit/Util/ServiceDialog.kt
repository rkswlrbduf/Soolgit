package blackstone.com.soolgit.Util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import blackstone.com.soolgit.DataClass.ServiceCompleteData
import blackstone.com.soolgit.DataClass.InactiveBus
import blackstone.com.soolgit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.dialog_service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ServiceDialog(val activity: Activity, val item: ServiceCompleteData) : Dialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_service)

        service_dialog_menu_textview.text = item.servicenm
        Glide.with(context)
                .load(item.serviceimg)
                .apply(RequestOptions.placeholderOf(ColorDrawable(ContextCompat.getColor(context, R.color.very_light_pink))))
                .apply(RequestOptions.overrideOf(service_dialog_menu_imageview.width, service_dialog_menu_imageview.height))
                .into(service_dialog_menu_imageview)
        service_dialog_location_textview.text = item.storenm
        service_dialog_close_imagebutton.setOnClickListener { dismiss() }

        window.setBackgroundDrawableResource(android.R.color.transparent)
        val displayMetrics = DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels * 271 / 360
        var height = width
        window.setLayout(width, height)

        service_dialog_pinview.setPinViewEventListener {pinview: Pinview, b: Boolean ->
            if(item.storecode == pinview.value) {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(pinview.windowToken, 0)
                BaseActivity.baseServer?.servicecomplete(item)?.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        dismiss()
                        ServiceCompleteDialog(activity).show()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {

                    }
                })
                BusProvider().getInstance().post(InactiveBus(false))
            }
        }

    }

}