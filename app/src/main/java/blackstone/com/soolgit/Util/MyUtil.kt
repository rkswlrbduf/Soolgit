package blackstone.com.soolgit.Util

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.util.Log
import android.widget.TextView
import blackstone.com.soolgit.DataClass.CategoryData
import blackstone.com.soolgit.DataClass.ThemeData
import blackstone.com.soolgit.Fragment.BaseFragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MyUtil(mContext: Context?) : ActivityCompat.OnRequestPermissionsResultCallback {

    companion object {
        private var mLocationPermissionGranted = false
        private var mSMSPermissionGranted = false
        private var mReadPhoneStatePermissionGranted = false
        private lateinit var currentAddress: Address
        private lateinit var currentLocation: Location
        private lateinit var mGeoCoder: Geocoder
    }

    private val gson = Gson()
    private val PREFS_FILENAME = "prefs"
    private val PREF_KEY_ID = "ID"
    private val PREF_KEY_NM = "NM"
    private val PREF_KEY_IMG = "IMG"
    private val PREF_KEY_TYPE = "TYPE"
    private var hashMapString: String = ""
    private lateinit var map: GoogleMap

    private val prefs: SharedPreferences = mContext!!.getSharedPreferences(PREFS_FILENAME, 0)

    var ID: String
        get() = prefs.getString(PREF_KEY_ID, null) ?: ""
        set(value) = prefs.edit().putString(PREF_KEY_ID, value).apply()
    var NM: String
        get() = prefs.getString(PREF_KEY_NM, null) ?: ""
        set(value) = prefs.edit().putString(PREF_KEY_NM, value).apply()
    var IMG: String
        get() = prefs.getString(PREF_KEY_IMG, null) ?: ""
        set(value) = prefs.edit().putString(PREF_KEY_IMG, value).apply()
    var TYPE: String
        get() = prefs.getString(PREF_KEY_TYPE, null) ?: ""
        set(value) = prefs.edit().putString(PREF_KEY_TYPE, value).apply()
    var ZZIM: HashMap<String, Boolean>
        get() {
            val zzimHashString = prefs.getString("ZZIM", null) ?: return HashMap()
            val type = object : TypeToken<HashMap<String, Boolean>>() {}.type
            return gson.fromJson<HashMap<String, Boolean>>(zzimHashString, type)
        }
        set(value) {
            val zzimHashString = gson.toJson(value)
            prefs.edit().putString("ZZIM", zzimHashString).apply()
        }
    var FILTERCATEGORY: ArrayList<CategoryData>
        get() {
            val categoryArrayList = prefs.getString("FILTERCATEGORY", null) ?: return ArrayList()
            val type = object : TypeToken<ArrayList<CategoryData>>() {}.type
            return gson.fromJson<ArrayList<CategoryData>>(categoryArrayList, type)
        }
        set(value) {
            val categoryArrayList = gson.toJson(value)
            prefs.edit().putString("FILTERCATEGORY", categoryArrayList).apply()
        }
    var FILTERTHEME: ArrayList<ThemeData>
        get() {
            val themeArrayList = prefs.getString("FILTERTHEME", null) ?: return ArrayList()
            val type = object : TypeToken<ArrayList<ThemeData>>() {}.type
            return gson.fromJson<ArrayList<ThemeData>>(themeArrayList, type)
        }
        set(value) {
            val themeArrayList = gson.toJson(value)
            prefs.edit().putString("FILTERTHEME", themeArrayList).apply()
        }
    private var PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002
    private var mContext: Context? = null
    private var mLocationRequest = LocationRequest()
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationManager: LocationManager

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

        try {
            mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            currentAddress = mGeoCoder.getFromLocation(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude, 1)[0]
            currentLocation = mLastKnownLocation!!
//            if (mLocationPermissionGranted) {
//                locationTextView?.text = String.format("%s %s %s", currentLocation.locality, currentLocation.subLocality, currentLocation.thoroughfare)
//            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            currentAddress = mGeoCoder.getFromLocation(mDefaultLocation.latitude, mDefaultLocation.longitude, 1)[0]
            val location = Location("")
            location.longitude = mDefaultLocation.longitude
            location.latitude = mDefaultLocation.latitude
            currentLocation = location
        }

        mCurrentLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                try {
                    currentAddress = mGeoCoder.getFromLocation(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude, 1)[0]
                    currentLocation = locationResult.lastLocation
                    BusProvider().getInstance().post(currentAddress)

                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
                mFusedLocationProviderClient?.removeLocationUpdates(mCurrentLocationCallback)
            }
        }
    }

    fun getCurrentAddress(): Address = currentAddress
    fun getCurrentLocation(): Location = currentLocation
    fun getGson(): Gson = gson

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

    @TargetApi(Build.VERSION_CODES.M)
    fun callPermission(o: Any, permissions: Array<String>, permissionsId: Int) {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(mContext!!, it) == PackageManager.PERMISSION_GRANTED) {
                when (it) {
                    android.Manifest.permission.ACCESS_FINE_LOCATION -> mLocationPermissionGranted = true
                }
            } else {
                if (o is BaseFragment) {
                    o.requestPermissions(permissions, permissionsId)
                } else if (o is BaseActivity) {
                    ActivityCompat.requestPermissions(o, permissions, permissionsId)
                }
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

    fun getLocationPermissionGranted(): Boolean {
        return mLocationPermissionGranted
    }

    fun getSMSPermissionGranted(): Boolean {
        return mSMSPermissionGranted && mReadPhoneStatePermissionGranted
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun setMap(map: GoogleMap) {
        this.map = map
    }

    fun getMap(): GoogleMap = map

    fun updateCurrentLocationNAddress() {
        currentAddress = mGeoCoder.getFromLocation(map.cameraPosition.target.latitude, map.cameraPosition.target.longitude, 1)[0]
        val location = Location("")
        location.latitude = map.cameraPosition.target.latitude
        location.longitude = map.cameraPosition.target.longitude
        currentLocation = location
    }

    fun updateLocationUI() {
        try {
            if (mLocationPermissionGranted) {
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
            } else {
                map.isMyLocationEnabled = false
                map.uiSettings.isMyLocationButtonEnabled = false
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
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude), DEFAULT_ZOOM))
                    } else {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM))
                        map.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }


}