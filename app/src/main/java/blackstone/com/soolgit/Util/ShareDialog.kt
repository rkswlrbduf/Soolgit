package blackstone.com.soolgit.Util

import android.app.Dialog
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.FragmentActivity
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import blackstone.com.soolgit.R
import kotlinx.android.synthetic.main.dialog_share.*

class ShareDialog(val activity: FragmentActivity) : Dialog(activity), View.OnClickListener {

    private lateinit var dialogShareKakaoContainer: ConstraintLayout
    private lateinit var dialogShareSmsContainer: ConstraintLayout
    private lateinit var dialogShareCancelTextView: TextView
    private lateinit var callback: sharedCallBack

    interface sharedCallBack {
        fun onKakaoClick()
        fun onSmsClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_share)
        dialogShareKakaoContainer = dialog_share_kakao_container
        dialogShareSmsContainer = dialog_share_sms_container
        dialogShareCancelTextView = dialog_share_cancel_textview
        val displayMetrics = DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels * 255 / 360
        var height = width * 210 / 255
        window.setLayout(width, height)
        window.setDimAmount(0.67f)
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setBackgroundDrawableResource(R.drawable.round_rectangle_3dp)
        dialogShareKakaoContainer.setOnClickListener(this)
        dialogShareSmsContainer.setOnClickListener(this)
        dialogShareCancelTextView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.dialog_share_kakao_container -> {
                callback.onKakaoClick()
                dismiss()
            }
            R.id.dialog_share_sms_container -> {
                callback.onSmsClick()
                dismiss()
            }
            R.id.dialog_share_cancel_textview -> {
                dismiss()
            }
        }
    }

    fun setOnShareDialogClickListener(callback: sharedCallBack) {
        this.callback = callback
    }

}