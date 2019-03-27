package blackstone.com.soolgit

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import blackstone.com.soolgit.Adapter.MapStoreRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.CategoryData
import blackstone.com.soolgit.DataClass.MapData
import blackstone.com.soolgit.DataClass.StoreData
import blackstone.com.soolgit.Util.BaseActivity
import blackstone.com.soolgit.Util.MyUtil
import blackstone.com.soolgit.Util.xSpacesItemDecoration
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_store_location.*

class StoreLocationActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 4002

    private lateinit var map: GoogleMap
    private lateinit var mUtil: MyUtil

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: MapStoreRecyclerViewAdapter
    private val snapHelper = PagerSnapHelper()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var storeList: ArrayList<StoreData>
    private var markerArray = ArrayList<Marker>()
    private lateinit var pinUnselected: BitmapDescriptor
    private lateinit var pinSelected: BitmapDescriptor
    private val type = object : TypeToken<ArrayList<StoreData>>() {}.type



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_location)

        mUtil = MyUtil(this)
        mUtil.callPermission(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)

        recyclerView = main_map_content_store_recyclerview
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mapFragment = supportFragmentManager.findFragmentById(R.id.store_location_map) as SupportMapFragment
        storeList = mUtil.getGson().fromJson<ArrayList<StoreData>>(intent.getStringExtra("storeList"), type)
        pinUnselected = BitmapDescriptorFactory.fromResource(R.drawable.pin_unselected)
        pinSelected = BitmapDescriptorFactory.fromResource(R.drawable.pin_selected)
        recyclerViewAdapter = MapStoreRecyclerViewAdapter(this, storeList)

        mapFragment.getMapAsync(this)

        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(xSpacesItemDecoration(1, convertDpToPixel(10f, this).toInt(), true))
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var current = layoutManager.getPosition(snapHelper.findSnapView(layoutManager)!!)
                markerArray.forEach {
                    if (it.tag?.equals(current)!!) {
                        it.setIcon(pinSelected)
                        map.animateCamera(CameraUpdateFactory.newLatLng(it.position))
                    } else {
                        it.setIcon(pinUnselected)
                    }
                }
            }
        })
        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = recyclerViewAdapter

    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        if (mUtil.getLocationPermissionGranted()) {
            mUtil.setMap(map)
            mUtil.updateLocationUI()
            mUtil.getDeviceLocation()
        }
        storeList.forEachIndexed { index, mapData ->
            var markerOption = MarkerOptions()
            markerOption.position(LatLng(mapData.StorePointY!!, mapData.StorePointX!!))
            markerOption.title(mapData.StoreName)
            if (index == 0) markerOption.icon(pinSelected)
            else markerOption.icon(pinUnselected)
            var marker = map.addMarker(markerOption)
            marker?.tag = index
            markerArray.add(marker!!)
        }
        map.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        recyclerView.scrollToPosition(marker?.tag as Int)
        map.animateCamera(CameraUpdateFactory.newLatLng(marker.position))
        return true
    }

}