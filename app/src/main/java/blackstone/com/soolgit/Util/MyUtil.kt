package blackstone.com.soolgit.Util

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import blackstone.com.soolgit.Fragment.BaseFragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import java.util.*

class MyUtil(mContext: Context?) : ActivityCompat.OnRequestPermissionsResultCallback {

    companion object {
        private var mLocationPermissionGranted = false
    }

    private val PREFS_FILENAME = "prefs"
    private val PREF_KEY_ID = "ID"
    private val PREF_KEY_NM = "NM"
    private val PREF_KEY_IMG = "IMG"
    private val PREF_KEY_TYPE = "TYPE"

    private val prefs: SharedPreferences = mContext!!.getSharedPreferences(PREFS_FILENAME, 0)

    var ID: String?
        get() = prefs.getString(PREF_KEY_ID, "")
        set(value) = prefs.edit().putString(PREF_KEY_ID, value).apply()
    var NM: String?
        get() = prefs.getString(PREF_KEY_NM, "")
        set(value) = prefs.edit().putString(PREF_KEY_NM, value).apply()
    var IMG: String?
        get() = prefs.getString(PREF_KEY_IMG, "")
        set(value) = prefs.edit().putString(PREF_KEY_IMG, value).apply()
    var TYPE: String?
        get() = prefs.getString(PREF_KEY_TYPE, "")
        set(value) = prefs.edit().putString(PREF_KEY_TYPE, value).apply()

    private var PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002
    private var mContext: Context? = null
    private var mLocationRequest = LocationRequest()
    private var mGeoCoder: Geocoder
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationManager: LocationManager
    private var map: GoogleMap? = null
    private val mDefaultLocation = LatLng(37.490446, 126.723578)
    private val DEFAULT_ZOOM = 15f

    private val LOCATION_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY
    private val LOCATION_INTERVAL = 1000L
    private val LOCATION_FAST_INTERVAL = 200L

    private lateinit var mCurrentLocationCallback: LocationCallback
    private var locationTextView: TextView? = null
    private var mLastKnownLocation: Location? = null



    init {
        this.mContext = mContext
        mLocationManager = mContext?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mGeoCoder = Geocoder(this.mContext, Locale.KOREAN)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.mContext!!)
        mLocationRequest.priority = LOCATION_PRIORITY
        mLocationRequest.interval = LOCATION_INTERVAL
        mLocationRequest.fastestInterval = LOCATION_FAST_INTERVAL
        mCurrentLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                try {
                    mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    val arrayList = mGeoCoder.getFromLocation(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude, 1)
                    if (mLocationPermissionGranted && !arrayList.isEmpty()) {
                        locationTextView?.text = String.format("%s %s %s", arrayList[0].locality, arrayList[0].subLocality, arrayList[0].thoroughfare)
                    }
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
                mFusedLocationProviderClient?.removeLocationUpdates(mCurrentLocationCallback)
            }
        }
    }

    fun setLocationTextView(locationTextView: TextView?) {
        this.locationTextView = locationTextView
    }

    fun requestCurrentLocationCallback() {
        try {
            mFusedLocationProviderClient?.requestLocationUpdates(mLocationRequest, mCurrentLocationCallback, null)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun setPassiveMapListener() {
        map?.setOnCameraIdleListener {
            val arrayList = mGeoCoder.getFromLocation(map!!.cameraPosition.target.latitude, map!!.cameraPosition.target.longitude, 1)
            if (mLocationPermissionGranted && !arrayList.isEmpty()) {
                locationTextView?.text = arrayList[0].getAddressLine(0).removePrefix("대한민국 ")
            }
        }
    }

    fun removeCurrentLocationCallback() {
        if (mFusedLocationProviderClient != null)
            mFusedLocationProviderClient?.removeLocationUpdates(mCurrentLocationCallback)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun callPermission(o: Any, permissions: Array<String>, permissionsId: Int) {
        if (ContextCompat.checkSelfPermission(mContext!!, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            if (o is BaseFragment) {
                o.requestPermissions(permissions, permissionsId)
            } else if (o is BaseActivity) {
                ActivityCompat.requestPermissions(o, permissions, permissionsId)
            }
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
    }

    fun getPermissionGranted(): Boolean {
        return mLocationPermissionGranted
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun setMap(map: GoogleMap?) {
        this.map = map
    }

    fun updateLocationUI() {
        try {
            if (mLocationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                mLastKnownLocation = null
                callPermission(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }

    fun getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                val locationResult = mFusedLocationProviderClient?.lastLocation
                locationResult?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        mLastKnownLocation = it.result
                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude), DEFAULT_ZOOM))
                    } else {
                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }


}