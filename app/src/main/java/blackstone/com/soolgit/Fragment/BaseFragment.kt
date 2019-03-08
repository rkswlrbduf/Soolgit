package blackstone.com.soolgit.Fragment

import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import android.support.v4.app.Fragment
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import blackstone.com.soolgit.R.id.progressBar
import com.github.ybq.android.spinkit.style.DoubleBounce

open class BaseFragment : Fragment() {

    private var mProgressDialog: ProgressDialog? = null

    open fun log(tag: String) {
        Log.d("TAG", tag)
    }

    open fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    open fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    open fun showProgressDialog(context: Context?) {
        if (mProgressDialog == null) {
            if (Build.VERSION_CODES.KITKAT < Build.VERSION.SDK_INT) {
                mProgressDialog = ProgressDialog(context)
            } else {
                mProgressDialog = ProgressDialog(context)
            }
            mProgressDialog?.setMessage("Google Login")
            mProgressDialog?.isIndeterminate = true
            mProgressDialog?.setCancelable(false)
        }
        mProgressDialog?.show()
        //progressBar.visibility = View.VISIBLE
    }

    open fun hideProgressDialog() {
//        if (progressBar.visibility == View.VISIBLE) {
//            progressBar.visibility = View.GONE
//        }
        mProgressDialog?.dismiss()
    }

}