package blackstone.com.soolgit.Util

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.DisplayMetrics
import android.view.WindowManager
import blackstone.com.soolgit.CurrentLocationActivity
import blackstone.com.soolgit.R
import kotlinx.android.synthetic.main.dialog_location.*

class LocationDialog(val activity: FragmentActivity) : Dialog(activity) {

    private lateinit var mCallback: callback

    interface callback {
        fun onCurrentChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_location)
        val displayMetrics = DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels * 2 / 3
        var height = width * 174 / 240
        window.setLayout(width, height)
        window.setDimAmount(0.67f)
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setBackgroundDrawableResource(R.drawable.dialog_round_10dp)
        home_dialog_current_location_imagebutton.setOnClickListener {
            mCallback.onCurrentChanged()
            dismiss()
        }
        home_dialog_map_location_imagebutton.setOnClickListener {
            activity.startActivityForResult(Intent(activity, CurrentLocationActivity::class.java), 1001)
            dismiss()
        }
    }

    fun setOnCurrentChanged(mCallback: callback) {
        this.mCallback = mCallback
    }

}