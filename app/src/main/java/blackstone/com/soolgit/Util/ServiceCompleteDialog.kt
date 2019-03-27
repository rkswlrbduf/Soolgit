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


class ServiceCompleteDialog(val activity: Activity) : Dialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_service_complete)

        window.setBackgroundDrawableResource(android.R.color.transparent)
        val displayMetrics = DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels * 271 / 360
        var height = width * 1 / 3
        window.setLayout(width, height)

    }

}