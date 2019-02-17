package blackstone.com.soolgit.Fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import blackstone.com.soolgit.Adapter.HomeHotPlaceRecyclerViewAdapter
import blackstone.com.soolgit.Adapter.HomeLocationRecyclerViewAdapter
import blackstone.com.soolgit.Adapter.HomeThemeRecyclerViewAdapter
import blackstone.com.soolgit.Adapter.MainHomeImageSliderAdapter
import blackstone.com.soolgit.DataClass.HotPlaceStoreData
import blackstone.com.soolgit.R
import blackstone.com.soolgit.SearchActivity
import blackstone.com.soolgit.StoreActivity
import blackstone.com.soolgit.Util.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.squareup.otto.Subscribe
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeFragment : BaseFragment(), View.OnClickListener, LocationDialog.callback {

    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002

    private var viewPagerList: ArrayList<HotPlaceStoreData>? = ArrayList()
    private var hotplaceStoreList: ArrayList<HotPlaceStoreData>? = ArrayList()
    private var themeStoreList: ArrayList<HotPlaceStoreData>? = ArrayList()
    private var locationStoreList: ArrayList<HotPlaceStoreData>? = ArrayList()
    private var mView: View? = null
    private var hotplaceRecyclerView: RecyclerView? = null
    private var themeRecyclerView: RecyclerView? = null
    private var locationRecyclerView: RecyclerView? = null
    private var searchImageView: ImageView? = null
    private var locationTextView: TextView? = null
    private var imageViewPager: ViewPager? = null
    private var dotsIndicator: WormDotsIndicator? = null
    private var mUtil: MyUtil? = null

    private lateinit var hotplaceStoreAdapter: HomeHotPlaceRecyclerViewAdapter
    private lateinit var themeStoreAdapter: HomeThemeRecyclerViewAdapter
    private lateinit var locationStoreAdapter: HomeLocationRecyclerViewAdapter
    private lateinit var imageSliderViewPagerAdapter: MainHomeImageSliderAdapter
    private lateinit var locationDialog: LocationDialog
    private lateinit var intent: Intent

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            onResume()
        } else {
            onPause()
        }
    }

    override fun onCurrentChanged() {
        try {
            mUtil?.requestCurrentLocationCallback()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun initSetting(view: View?) {
        locationDialog = LocationDialog(activity!!)
        mUtil = MyUtil(context)
        hotplaceRecyclerView = view?.main_home_content_hotplace_recyclerview
        themeRecyclerView = view?.main_home_content_theme_recyclerview
        locationRecyclerView = view?.main_home_content_location_recyclerview
        searchImageView = view?.main_home_search_imageview
        locationTextView = view?.main_home_location_textview
        dotsIndicator = view?.main_home_dotsindicator
        imageViewPager = view?.main_home_image_viewpager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BusProvider().getInstance().register(this)
    }

    override fun onDestroy() {
        BusProvider().getInstance().unregister(this)
        super.onDestroy()
    }

    @SuppressWarnings("unused")
    @Subscribe
    open fun onActivityResult(activityResultEvent: ActivityResultEvent) {
        onActivityResult(activityResultEvent.getRequestCode(), activityResultEvent.getResultCode(), activityResultEvent.getData())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        locationTextView?.text = data?.getStringExtra("RESULT")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_home, container, false)

        initSetting(mView)

        mUtil?.callPermission(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        mUtil?.setLocationTextView(locationTextView)
        mUtil?.requestCurrentLocationCallback()

        initViewPager()
        initHotPlaceRecyclerView(hotplaceRecyclerView, GridLayoutManager(context, 2), ySpacesItemDecoration(2, mUtil!!.convertDpToPixel(12f, context!!).toInt(), false))
        initThemeRecyclerView(themeRecyclerView, LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false), xSpacesItemDecoration(1, mUtil!!.convertDpToPixel(10f, context!!).toInt(), true))
        initLocationRecyclerView(locationRecyclerView, LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false), ySpacesItemDecoration(1, 30, false))
        serverViewPager()
        serverHotPlaceRecyclerView()
        serverThemeRecyclerView()
        serverLocationRecyclerView()

        searchImageView?.setOnClickListener(this)
        locationTextView?.setOnClickListener(this)
        locationDialog.setOnCurrentChanged(this)

//        if (handler == null) {
//            handler = Handler()
//            runnable = object : Runnable {
//                override fun run() {
//                    if (layoutManager.findFirstVisibleItemPosition() !== 2) {
//                        recyclerView.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() + 1)
//                    } else {
//                        recyclerView.smoothScrollToPosition(0)
//                    }
//                    handler.postDelayed(this, 3000)
//                }
//            }
//        }
//        handler.postDelayed(runnable, 3000)

        return mView
    }

    private fun initViewPager() {
        imageSliderViewPagerAdapter = MainHomeImageSliderAdapter(context!!, viewPagerList)
        imageViewPager?.adapter = imageSliderViewPagerAdapter
    }

    private fun initHotPlaceRecyclerView(recyclerView: RecyclerView?, layoutManager: GridLayoutManager, itemDecoration: ySpacesItemDecoration) {
        recyclerView?.layoutManager = layoutManager
        recyclerView?.addItemDecoration(itemDecoration)
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.setHasFixedSize(true)
        hotplaceStoreAdapter = HomeHotPlaceRecyclerViewAdapter(context!!, hotplaceStoreList)
        hotplaceStoreAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        recyclerView?.adapter = hotplaceStoreAdapter
        hotplaceStoreAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            intent = Intent(context, StoreActivity::class.java)
            intent.putExtra("ID", (adapter.getItem(position) as HotPlaceStoreData).StoreID)
            startActivity(intent)
        }
    }

    private fun initThemeRecyclerView(recyclerView: RecyclerView?, layoutManager: LinearLayoutManager, itemDecoration: xSpacesItemDecoration) {
        recyclerView?.layoutManager = layoutManager
        recyclerView?.addItemDecoration(itemDecoration)
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.setHasFixedSize(true)
        themeStoreAdapter = HomeThemeRecyclerViewAdapter(context!!, themeStoreList)
        themeStoreAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        recyclerView?.adapter = themeStoreAdapter
    }

    private fun initLocationRecyclerView(recyclerView: RecyclerView?, layoutManager: LinearLayoutManager, itemDecoration: ySpacesItemDecoration) {
        recyclerView?.layoutManager = layoutManager
        recyclerView?.addItemDecoration(itemDecoration)
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.setHasFixedSize(true)
        locationStoreAdapter = HomeLocationRecyclerViewAdapter(context!!, locationStoreList)
        locationStoreAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        recyclerView?.adapter = locationStoreAdapter
    }

    private fun serverViewPager() {
        BaseActivity.baseServer?.hotplacestore()?.enqueue(object : Callback<ArrayList<HotPlaceStoreData>> {
            override fun onResponse(call: Call<ArrayList<HotPlaceStoreData>>, response: Response<ArrayList<HotPlaceStoreData>>) {
                imageSliderViewPagerAdapter.updateList(response.body()!!)
                dotsIndicator?.setViewPager(imageViewPager)
            }
            override fun onFailure(call: Call<ArrayList<HotPlaceStoreData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun serverHotPlaceRecyclerView() {
        BaseActivity.baseServer?.hotplacestore()?.enqueue(object : Callback<ArrayList<HotPlaceStoreData>> {
            override fun onResponse(call: Call<ArrayList<HotPlaceStoreData>>, response: Response<ArrayList<HotPlaceStoreData>>) {
                hotplaceStoreAdapter.updateList(response.body()!!)
            }
            override fun onFailure(call: Call<ArrayList<HotPlaceStoreData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun serverThemeRecyclerView() {
        BaseActivity.baseServer?.hotplacestore()?.enqueue(object : Callback<ArrayList<HotPlaceStoreData>> {
            override fun onResponse(call: Call<ArrayList<HotPlaceStoreData>>, response: Response<ArrayList<HotPlaceStoreData>>) {
                themeStoreAdapter.updateList(response.body()!!)
            }
            override fun onFailure(call: Call<ArrayList<HotPlaceStoreData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun serverLocationRecyclerView() {
        BaseActivity.baseServer?.hotplacestore()?.enqueue(object : Callback<ArrayList<HotPlaceStoreData>> {
            override fun onResponse(call: Call<ArrayList<HotPlaceStoreData>>, response: Response<ArrayList<HotPlaceStoreData>>) {
                locationStoreAdapter.updateList(response.body()!!)
            }
            override fun onFailure(call: Call<ArrayList<HotPlaceStoreData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

//    private fun initCurrentLocation() {
//        try {
//            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//        } catch (e: SecurityException) {
//            e.printStackTrace()
//        }
//        val arrayList = geoCoder.getFromLocation(lastKnownLocation.latitude, lastKnownLocation.longitude, 1)
//        if (mLocationPermissionGranted && lastKnownLocation != null && !arrayList.isEmpty()) {
//            locationTextView?.text = String.format("%s %s %s", arrayList[0].locality, arrayList[0].subLocality, arrayList[0].thoroughfare)
//        }
//
//        try {
//                //locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1L, 1f, locationListener)
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0F, object : LocationListener {
//                    override fun onLocationChanged(location: Location) {
//                        try {
//                            val arrayList =
//                                    Log.d("myLocation", arrayList.toString())
//                        } catch (e: IOException) {
//                            e.printStackTrace()
//                            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생")
//                        }
//                    }
//
//                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//                    }
//
//                    override fun onProviderEnabled(provider: String?) {
//                    }
//
//                    override fun onProviderDisabled(provider: String?) {
//                    }
//                })
//            }
//        } catch (e: SecurityException) {
//            Log.e("Exception: %s", e.message)
//        }
//    }

   override fun onClick(v: View?) {
        when (v?.id) {
            R.id.main_home_search_imageview -> {
                startActivity(Intent(context, SearchActivity::class.java))
            }
            R.id.main_home_location_textview -> {
                locationDialog.show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mUtil?.removeCurrentLocationCallback()
    }
}