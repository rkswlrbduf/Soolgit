package blackstone.com.soolgit.Util

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import blackstone.com.soolgit.R
import kotlinx.android.synthetic.main.dialog_service.*

class ServiceDialog(val activity: Activity) : Dialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_service)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        val displayMetrics = DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels * 271 / 360
        var height = width
        window.setLayout(width, height)

        service_dialog_pinview.setPinViewEventListener {pinview: Pinview, b: Boolean ->
            Toast.makeText(activity, pinview.value, Toast.LENGTH_SHORT).show()
        }

    }

}