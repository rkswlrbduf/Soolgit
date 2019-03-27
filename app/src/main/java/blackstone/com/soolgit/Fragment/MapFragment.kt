package blackstone.com.soolgit.Fragment

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import blackstone.com.soolgit.Adapter.MapStoreRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.MapData
import blackstone.com.soolgit.DataClass.StoreData
import blackstone.com.soolgit.R
import blackstone.com.soolgit.Util.BaseActivity
import blackstone.com.soolgit.Util.MyUtil
import blackstone.com.soolgit.Util.xSpacesItemDecoration
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : BaseFragment(), GoogleMap.OnMarkerClickListener, View.OnClickListener, OnMapReadyCallback, GoogleMap.OnCameraMoveListener {

    private lateinit var map: GoogleMap
    private lateinit var mRecyclerview: RecyclerView
    private lateinit var mRecyclerViewAdapter: MapStoreRecyclerViewAdapter
    private lateinit var refreshContainer: ConstraintLayout
    private lateinit var mView: View
    private lateinit var mUtil: MyUtil
    private lateinit var mapView: MapView
    private lateinit var bounds: LatLngBounds
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002
    private val KEY_CAMERA_POSITION = "camera_position"
    private val KEY_LOCATION = "location"
    private val snapHelper = PagerSnapHelper()

    private var mLocationPermissionGranted = false
    private var mLastKnownLocation: Location? = null
    private var mCameraPosition: CameraPosition? = null
    private var layoutManager: LinearLayoutManager? = null
    private var markerArray = ArrayList<Marker>()
    private var pinUnselected: BitmapDescriptor? = null
    private var pinSelected: BitmapDescriptor? = null
    private var onStarted: Boolean = false
    private var onTouched: Boolean = false

    private var mapList: ArrayList<StoreData> = ArrayList()
    private var lx: Double? = null
    private var rx: Double? = null
    private var ty: Double? = null
    private var dy: Double? = null


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            onResume()
            if (!onStarted) {
                mUtil.callPermission(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
                mapView.getMapAsync(this)
                onStarted = !onStarted
                //mUtil.updateLocationUI()
            }
        }
//        } else {
//            onPause()
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_map, container, false)
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        mUtil = MyUtil(context)
        mapView = mView.map_view

        mRecyclerview = mView.main_map_content_store_recyclerview!!
        refreshContainer = mView.map_refresh_container

        refreshContainer.setOnClickListener(this)
        initMapRecyclerView(mRecyclerview)

        mapView.onCreate(savedInstanceState)

        pinUnselected = BitmapDescriptorFactory.fromResource(R.drawable.pin_unselected)
        pinSelected = BitmapDescriptorFactory.fromResource(R.drawable.pin_selected)
        mRecyclerViewAdapter = MapStoreRecyclerViewAdapter(context!!, mapList)
        mRecyclerview.adapter = mRecyclerViewAdapter
        mRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val current = layoutManager?.getPosition(snapHelper.findSnapView(layoutManager)!!)
                markerArray.forEach {
                    if (it.tag?.equals(current)!!) {
                        it.setIcon(pinSelected)
                        map.setOnCameraMoveListener(null)
                        map.animateCamera(CameraUpdateFactory.newLatLng(it.position))
                        map.setOnCameraIdleListener {
                            map.setOnCameraMoveListener(this@MapFragment)
                        }
                    } else {
                        it.setIcon(pinUnselected)
                    }
                }
            }
        })
        return mView
    }

    private fun serverMapRecyclerView(dy: Double?, lx: Double?, ty: Double?, rx: Double?) {
        BaseActivity.baseServer?.map(dy, lx, ty, rx)?.enqueue(object : Callback<ArrayList<StoreData>> {
            override fun onResponse(call: Call<ArrayList<StoreData>>, response: Response<ArrayList<StoreData>>) {

                mapList = response.body()!!
                mRecyclerview.visibility = if(mapList.isEmpty()) View.GONE else View.VISIBLE
                mRecyclerViewAdapter.updateList(mapList)

                markerArray.clear()
                map.clear()

                mapList.forEachIndexed { index, mapData ->
                    val markerOption = MarkerOptions()
                    markerOption.position(LatLng(mapData.StorePointY!!, mapData.StorePointX!!))
                    markerOption.title(mapData.StoreName)
                    if (index == 0) markerOption.icon(pinSelected)
                    else markerOption.icon(pinUnselected)
                    val marker = map.addMarker(markerOption)
                    marker?.tag = index
                    markerArray.add(marker!!)
                }
            }

            override fun onFailure(call: Call<ArrayList<StoreData>>, t: Throwable) {
                showNetworkDialog(context!!)
            }
        })
    }

    private fun initMapRecyclerView(recyclerView: RecyclerView) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(xSpacesItemDecoration(1, convertDpToPixel(10f, context!!).toInt(), true))
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
//        outState.putParcelable(KEY_LOCATION, mLastKnownLocation)
//        super.onSaveInstanceState(outState)
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
        }
        mUtil.updateLocationUI()
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        try {
            if (mUtil.getLocationPermissionGranted()) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mUtil.getCurrentLocation().latitude, mUtil.getCurrentLocation().longitude), 15f))
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
        map.setOnCameraIdleListener {
            bounds = map.projection.visibleRegion.latLngBounds
            dy = bounds.southwest.latitude
            lx = bounds.southwest.longitude
            ty = bounds.northeast.latitude
            rx = bounds.northeast.longitude
            serverMapRecyclerView(dy, lx, ty, rx)
            map.setOnCameraMoveListener(this)
            map.setOnCameraIdleListener(null)
        }
        mUtil.setMap(map)
        map.setOnMarkerClickListener(this)

    }

    override fun onCameraMove() {
        refreshContainer.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.map_refresh_container -> {
                val bounds = map.projection.visibleRegion.latLngBounds
                dy = bounds.southwest.latitude
                lx = bounds.southwest.longitude
                ty = bounds.northeast.latitude
                rx = bounds.northeast.longitude
                serverMapRecyclerView(dy, lx, ty, rx)
                refreshContainer.visibility = View.INVISIBLE
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        mRecyclerview.scrollToPosition(marker?.tag as Int)
        map.animateCamera(CameraUpdateFactory.newLatLng(marker.position))
        return true
    }

    override fun onResume() {
        super.onResume()
        log("OnResume")
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        log("onPause")
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}