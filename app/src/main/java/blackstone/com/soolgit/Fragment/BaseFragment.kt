package blackstone.com.soolgit.Fragment

import android.content.Context
import android.support.v4.app.Fragment
import android.util.DisplayMetrics

open class BaseFragment : Fragment() {

    open fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    open fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

}