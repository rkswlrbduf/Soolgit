package blackstone.com.soolgit.Fragment

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import blackstone.com.soolgit.Adapter.MapStoreRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.MapData
import blackstone.com.soolgit.R
import blackstone.com.soolgit.Util.BaseActivity
import blackstone.com.soolgit.Util.MyUtil
import blackstone.com.soolgit.Util.xSpacesItemDecoration
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : BaseFragment(), GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener {

    lateinit var mRecyclerview: RecyclerView
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002
    private val KEY_CAMERA_POSITION = "camera_position"
    private val KEY_LOCATION = "location"
    private val snapHelper = LinearSnapHelper()

    private var mLocationPermissionGranted = false
    private var mLastKnownLocation: Location? = null
    private var mCameraPosition: CameraPosition? = null
    private var mapView: MapView? = null
    private var map: GoogleMap? = null
    private var layoutManager: LinearLayoutManager? = null
    private var markerArray = ArrayList<Marker>()
    private var pinUnselected: BitmapDescriptor? = null
    private var pinSelected: BitmapDescriptor? = null
    private var onStarted: Boolean = false

    private var lx: Double? = null
    private var rx: Double? = null
    private var ty: Double? = null
    private var dy: Double? = null
    private var mView: View? = null
    private var mUtil: MyUtil? = null

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser && mView != null) {
            onResume()
            if(!onStarted) {
                onStarted = !onStarted
                mUtil?.callPermission(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
                mUtil?.getDeviceLocation()
                mUtil?.updateLocationUI()
                map?.setOnCameraIdleListener(this)
                map?.setOnMarkerClickListener(this)
            }
        } else {
            onPause()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_map, container, false)
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        mUtil = MyUtil(context)
        mapView = mView?.map_view as MapView
        mRecyclerview = mView?.main_map_content_store_recyclerview!!

        initMapRecyclerView(mRecyclerview)

        mapView?.onCreate(savedInstanceState)
        mapView?.onResume()
        mapView?.getMapAsync {
            map = it
            mUtil?.setMap(map)
        }
        pinUnselected = BitmapDescriptorFactory.fromResource(R.drawable.pin_unselected)
        pinSelected = BitmapDescriptorFactory.fromResource(R.drawable.pin_selected)
        mRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var current = layoutManager?.getPosition(snapHelper.findSnapView(layoutManager)!!)
                markerArray?.forEach {
                    if (it.tag?.equals(current)!!) {
                        it.setIcon(pinSelected)
                    } else {
                        it.setIcon(pinUnselected)
                    }
                }
            }
        })
        return mView
    }

    private fun serverMapRecyclerView(recyclerView: RecyclerView, dy: Double?, lx: Double?, ty: Double?, rx: Double?) {
        BaseActivity.baseServer?.map(dy, lx, ty, rx)?.enqueue(object : Callback<List<MapData>> {
            override fun onResponse(call: Call<List<MapData>>, response: Response<List<MapData>>) {
                var mapList: List<MapData> = response.body()!!

                recyclerView.adapter = MapStoreRecyclerViewAdapter(context!!, mapList)
                markerArray.clear()
                map?.clear()

                mapList.forEachIndexed { index, mapData ->
                    var markerOption = MarkerOptions()
                    markerOption.position(LatLng(mapData.StorePointY!!, mapData.StorePointX!!))
                    markerOption.title(mapData.StoreName)
                    if (index == 0) markerOption.icon(pinSelected)
                    else markerOption.icon(pinUnselected)
                    var marker = map?.addMarker(markerOption)
                    marker?.tag = index
                    markerArray.add(marker!!)
                }
            }

            override fun onFailure(call: Call<List<MapData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun initMapRecyclerView(recyclerView: RecyclerView) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(xSpacesItemDecoration(1, convertDpToPixel(10f, context!!).toInt(), false))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map?.cameraPosition)
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation)
            super.onSaveInstanceState(outState)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
        }
        mUtil?.updateLocationUI()
    }

    override fun onCameraIdle() {
        var bounds = map?.projection?.visibleRegion?.latLngBounds
        dy = bounds?.southwest?.latitude
        lx = bounds?.southwest?.longitude
        ty = bounds?.northeast?.latitude
        rx = bounds?.northeast?.longitude
        serverMapRecyclerView(mRecyclerview, dy, lx, ty, rx)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        mRecyclerview.scrollToPosition(p0?.tag as Int)
        return true
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }
}