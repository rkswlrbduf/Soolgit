package blackstone.com.soolgit.Util

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import blackstone.com.soolgit.Interface.KakaoServerInterface
import blackstone.com.soolgit.Interface.NaverServerInterface
import blackstone.com.soolgit.Interface.ServerInterface
import blackstone.com.soolgit.Interface.UserServerInterface
import blackstone.com.soolgit.R
import com.amazonaws.mobile.client.AWSMobileClient
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.GsonBuilder
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


open class BaseActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002

    companion object {
        var mLocationPermissionGranted = false
        var baseServer: ServerInterface? = null
        var naverServer: NaverServerInterface? = null
        var kakaoServer: KakaoServerInterface? = null
        var userServer: UserServerInterface? = null
    }

    protected var progressBar: ProgressBar? = null
    var firebaseAnalytics: FirebaseAnalytics? = null
    val BASE_URL = "http://54.180.48.148:9000"
    val NAVER_BASE_URL = "https://openapi.naver.com"
    val KAKAO_BASE_URL = "https://kapi.kakao.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarColor()

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        AWSMobileClient.getInstance().initialize(this) {
            Log.d("TAG", "AWSMobileClient is initialized")
        }.execute()

        val gson = GsonBuilder().setLenient().create()

        val baseInterceptor = HttpLoggingInterceptor()

        baseInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val baseClient = OkHttpClient.Builder().addInterceptor(baseInterceptor).build()

        val baseRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(baseClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val naverRetrofit = Retrofit.Builder()
                .baseUrl(NAVER_BASE_URL)
                .client(baseClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val kakaoRetrofit = Retrofit.Builder()
                .baseUrl(KAKAO_BASE_URL)
                .client(baseClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val userRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(baseClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        baseServer = baseRetrofit.create(ServerInterface::class.java)
        naverServer = naverRetrofit.create(NaverServerInterface::class.java)
        kakaoServer = kakaoRetrofit.create(KakaoServerInterface::class.java)
        userServer = userRetrofit.create(UserServerInterface::class.java)

    }

    /**
     * TypeKit Init
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    /**
     * 프로그레스를 보여준다.
     */
    open fun showProgressDialog(context: Context) {
        /*if (mProgressDialog == null) {
            if (Build.VERSION_CODES.KITKAT < Build.VERSION.SDK_INT) {
                mProgressDialog = ProgressDialog(context)
            } else {
                mProgressDialog = ProgressDialog(context)
            }
            mProgressDialog?.setMessage("Google Login")
            mProgressDialog?.isIndeterminate = true
            mProgressDialog?.setCancelable(false)
        }
        mProgressDialog?.show()*/
        progressBar = ProgressBar(context)
        val doubleBounce = DoubleBounce()
        progressBar?.indeterminateDrawable = doubleBounce
        progressBar?.visibility = View.VISIBLE
    }

    /**
     * 프로그래스를 숨긴다.
     */
    open fun hideProgressDialog() {
        if (progressBar != null && progressBar!!.visibility == View.VISIBLE) {
            progressBar?.visibility = View.GONE
        }
    }

    /**
     * statusBar Color 설정
     */
    open fun setStatusBarColor() {
        if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
            val window = getWindow()
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor))
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    open fun setLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
        }
        //initCurrentLocation()
    }

//    open fun getDeviceLocation() {
//        try {
//            if (mLocationPermissionGranted) {
//                val locationResult = mFusedLocationProviderClient?.lastLocation
//                locationResult?.addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        mLastKnownLocation = it.result
//                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude), DEFAULT_ZOOM))
//                    } else {
//                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM))
//                        map?.uiSettings?.isMyLocationButtonEnabled = false
//                    }
//                }
//            }
//        } catch (e: SecurityException) {
//            Log.e("Exception: %s", e.message)
//        }
//    }

    open fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    open fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    open fun log(tag: String) {
        Log.d("TAG", tag)
    }


}