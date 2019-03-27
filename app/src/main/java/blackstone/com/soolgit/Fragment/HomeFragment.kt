package blackstone.com.soolgit.Fragment

import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import blackstone.com.soolgit.Adapter.HomeHotPlaceRecyclerViewAdapter
import blackstone.com.soolgit.Adapter.HomeConceptRecyclerViewAdapter
import blackstone.com.soolgit.Adapter.HomeThemeRecyclerViewAdapter
import blackstone.com.soolgit.Adapter.MainHomeImageSliderAdapter
import blackstone.com.soolgit.DataClass.ConceptData
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
    private var conceptStoreList: ArrayList<ConceptData>? = ArrayList()
    private var mView: View? = null
    private var hotplaceRecyclerView: RecyclerView? = null
    private var themeRecyclerView: RecyclerView? = null
    private var conceptRecyclerView: RecyclerView? = null
    private var searchImageView: ImageView? = null
    private var locationTextView: TextView? = null
    private var imageViewPager: ViewPager? = null
    private var dotsIndicator: WormDotsIndicator? = null
    private var hotplaceTextView: TextView? = null

    private lateinit var hotplaceStoreAdapter: HomeHotPlaceRecyclerViewAdapter
    private lateinit var themeStoreAdapter: HomeThemeRecyclerViewAdapter
    private lateinit var conceptStoreAdapter: HomeConceptRecyclerViewAdapter
    private lateinit var imageSliderViewPagerAdapter: MainHomeImageSliderAdapter
    private lateinit var locationDialog: LocationDialog
    private lateinit var intent: Intent
    private lateinit var mUtil: MyUtil

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
            mUtil.requestCurrentLocationCallback()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun initSetting(view: View?) {
        locationDialog = LocationDialog(activity!!)
        mUtil = MyUtil(context)
        hotplaceRecyclerView = view?.main_home_content_hotplace_recyclerview
        themeRecyclerView = view?.main_home_content_theme_recyclerview
        conceptRecyclerView = view?.main_home_content_location_recyclerview
        searchImageView = view?.main_home_search_imageview
        locationTextView = view?.main_home_location_textview
        dotsIndicator = view?.main_home_dotsindicator
        imageViewPager = view?.main_home_image_viewpager
        hotplaceTextView = view?.main_home_content_hotplace_textview
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

    @Subscribe
    open fun getPost(address: Address) {
        locationTextView?.text = String.format("%s %s %s", address.locality, address.subLocality, address.thoroughfare)
        hotplaceTextView?.text = String.format("%s 근처 핫플레이스", address.thoroughfare)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LocationDialog.LOCATION_REQUESTCODE) {
            val address = data!!.extras["RESULT"] as Address
            locationTextView?.text = String.format("%s %s %s", address.locality, address.subLocality, address.thoroughfare)
            hotplaceTextView?.text = String.format("%s 근처 핫플레이스", address.thoroughfare)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_home, container, false)

        initSetting(mView)

        mUtil.callPermission(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        locationTextView?.text = String.format("%s %s %s", mUtil.getCurrentAddress().locality, mUtil.getCurrentAddress().subLocality, mUtil.getCurrentAddress().thoroughfare)
        hotplaceTextView?.text = String.format("%s 근처 핫플레이스", mUtil.getCurrentAddress().thoroughfare)

        initViewPager()
        initHotPlaceRecyclerView(hotplaceRecyclerView, GridLayoutManager(context, 2), ySpacesItemDecoration(2, mUtil.convertDpToPixel(12f, context!!).toInt(), false))
        initThemeRecyclerView(themeRecyclerView, LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false), xSpacesItemDecoration(1, mUtil.convertDpToPixel(10f, context!!).toInt(), true))
        initConceptRecyclerView(conceptRecyclerView, LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false), ySpacesItemDecoration(1, 30, false))
        serverViewPager()
        serverHotPlaceRecyclerView()
        serverRecommendRecyclerView()
        serverConceptRecyclerView()

        searchImageView?.setOnClickListener(this)
        locationTextView?.setOnClickListener(this)
        locationDialog.setOnCurrentChanged(this)

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

    private fun initConceptRecyclerView(recyclerView: RecyclerView?, layoutManager: LinearLayoutManager, itemDecoration: ySpacesItemDecoration) {
        recyclerView?.layoutManager = layoutManager
        recyclerView?.addItemDecoration(itemDecoration)
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.setHasFixedSize(true)
        conceptStoreAdapter = HomeConceptRecyclerViewAdapter(context!!, conceptStoreList)
        conceptStoreAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        recyclerView?.adapter = conceptStoreAdapter
    }

    private fun serverViewPager() {
        BaseActivity.baseServer?.hotplacestore()?.enqueue(object : Callback<ArrayList<HotPlaceStoreData>> {
            override fun onResponse(call: Call<ArrayList<HotPlaceStoreData>>, response: Response<ArrayList<HotPlaceStoreData>>) {
                imageSliderViewPagerAdapter.updateList(response.body()!!)
                dotsIndicator?.setViewPager(imageViewPager)
            }

            override fun onFailure(call: Call<ArrayList<HotPlaceStoreData>>, t: Throwable) {
                showNetworkDialog(context!!)
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

    private fun serverRecommendRecyclerView() {
        BaseActivity.baseServer?.recommend()?.enqueue(object : Callback<ArrayList<HotPlaceStoreData>> {
            override fun onResponse(call: Call<ArrayList<HotPlaceStoreData>>, response: Response<ArrayList<HotPlaceStoreData>>) {
                themeStoreAdapter.updateList(response.body()!!)
            }

            override fun onFailure(call: Call<ArrayList<HotPlaceStoreData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun serverConceptRecyclerView() {
        BaseActivity.baseServer?.concept()?.enqueue(object : Callback<ArrayList<ConceptData>> {
            override fun onResponse(call: Call<ArrayList<ConceptData>>, response: Response<ArrayList<ConceptData>>) {
                conceptStoreAdapter.updateList(response.body()!!)
            }

            override fun onFailure(call: Call<ArrayList<ConceptData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

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

}