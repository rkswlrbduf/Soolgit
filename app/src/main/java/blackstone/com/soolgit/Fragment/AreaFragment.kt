package blackstone.com.soolgit.Fragment

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.location.Address
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import blackstone.com.soolgit.Adapter.AreaStoreRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.StoreData
import blackstone.com.soolgit.FilterActivity
import blackstone.com.soolgit.R
import blackstone.com.soolgit.StoreActivity
import blackstone.com.soolgit.Util.*
import blackstone.com.soolgit.Util.BaseActivity.Companion.baseServer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.otto.Subscribe
import com.victor.loading.rotate.RotateLoading
import kotlinx.android.synthetic.main.filter_theme_recyclerview_row.view.*
import kotlinx.android.synthetic.main.fragment_area.view.*
import kotlinx.android.synthetic.main.fragment_area_content.view.*
import kotlinx.android.synthetic.main.fragment_area_coordinatorlayout_header.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AreaFragment : BaseFragment(), View.OnClickListener, LocationDialog.callback {

    companion object {
        const val FILTER_REQUESTCODE = 2002
    }

    private val gson = Gson()

    private var onStarted: Boolean = false

    private lateinit var mView: View
    private lateinit var storeAdapter: AreaStoreRecyclerViewAdapter
    private lateinit var mUtil: MyUtil
    private lateinit var locationTextView: TextView
    private lateinit var filterImageView: ImageView
    private lateinit var locationIconImageView: ImageView
    private lateinit var searchIconImageView: ImageView
    private lateinit var collapsibleToolbar: CollapsibleToolbar
    private lateinit var locationDialog: LocationDialog
    private lateinit var mComparator: mComparator
    private lateinit var loader: RotateLoading
    private lateinit var storeRecyclerViewContainer: NestedScrollView
    private lateinit var storeActivityIntent: Intent
    private lateinit var storeRecyclerView: RecyclerView
    private lateinit var headerThemeTextView: TextView
    private lateinit var headerThemeBackGround: View

    private var storeList: ArrayList<StoreData> = ArrayList()
    private var themeHashMap: HashMap<Int, String> = HashMap()

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            onResume()
            if (!onStarted) {
                onStarted = !onStarted
                locationTextView.text = String.format("%s %s", mUtil.getCurrentAddress().locality, mUtil.getCurrentAddress().thoroughfare)
                collapsibleToolbar.setTransition(R.id.start, R.id.end)
                initStoreRecyclerView(mView.main_area_content_store_recyclerview, LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false), ySpacesItemDecoration(1, 40, false))
                initStoreServerData(mView.main_area_content_store_recyclerview, mUtil.getCurrentLocation().longitude, mUtil.getCurrentLocation().latitude)
            }
        } else {
            onPause()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_area, container, false)
        initSetting(mView)
        return mView
    }

    private fun initSetting(view: View) {
        mUtil = MyUtil(context)
        mComparator = mComparator(mUtil)
        collapsibleToolbar = (view.fragment_area_coordinatorlayout_header as CollapsibleToolbar)
        locationTextView = (view.fragment_area_coordinatorlayout_header as View).main_area_header_location_textview
        locationIconImageView = (view.fragment_area_coordinatorlayout_header as View).main_area_content_header_location_imageview
        filterImageView = (view.fragment_area_coordinatorlayout_header as View).main_area_header_filter_imageview
        searchIconImageView = (view.fragment_area_coordinatorlayout_header as View).main_area_header_search_imageview
        loader = view.loader
        storeRecyclerViewContainer = view.main_area_content_store
        storeRecyclerView = view.main_area_content_store_recyclerview
        headerThemeTextView = (view.fragment_area_coordinatorlayout_header as View).main_area_header_theme_textview
        headerThemeBackGround = (view.fragment_area_coordinatorlayout_header as View).main_area_header_theme_background

        locationDialog = LocationDialog(activity!!)
        locationTextView.pivotX = 0f
        locationTextView.pivotY = locationTextView.height.toFloat()

        locationDialog.setOnCurrentChanged(this)

        locationTextView.setOnClickListener(this)
        locationIconImageView.setOnClickListener(this)
        filterImageView.setOnClickListener(this)
        searchIconImageView.setOnClickListener(this)

        storeAdapter = AreaStoreRecyclerViewAdapter(context!!, storeList, mUtil.getCurrentLocation())
        storeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        storeAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            storeActivityIntent = Intent(context, StoreActivity::class.java)
            storeActivityIntent.putExtra("ID", (adapter.getItem(position) as StoreData).StoreID.toString())
            startActivity(storeActivityIntent)
        }

        storeRecyclerView.adapter = storeAdapter

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.main_area_header_location_textview, R.id.main_area_content_header_location_imageview -> {
                locationDialog.show()
            }
            R.id.main_area_header_filter_imageview -> {
                startActivityForResult(Intent(activity, FilterActivity::class.java), FILTER_REQUESTCODE)
            }
            R.id.main_area_header_search_imageview -> {

            }
        }
    }

    private fun initStoreRecyclerView(recyclerView: RecyclerView?, layoutManager: RecyclerView.LayoutManager, itemDecoration: ySpacesItemDecoration) {
        recyclerView?.layoutManager = layoutManager
        recyclerView?.addItemDecoration(itemDecoration)
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.setHasFixedSize(true)
    }

    /**
     * (BUS)현재 위치 기반 데이터 업데이트
     */
    @Subscribe
    open fun getPost(address: Address) {
        locationTextView.text = String.format("%s %s", address.locality, address.thoroughfare)
        collapsibleToolbar.setTransition(R.id.start, R.id.end)
        stopLoader()
    }

    override fun onCurrentChanged() {
        try {
            startLoader()
            mUtil.requestCurrentLocationCallback()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BusProvider().getInstance().register(this)
    }

    override fun onDestroy() {
        BusProvider().getInstance().unregister(this)
        super.onDestroy()
    }

    @Subscribe
    open fun onActivityResult(activityResultEvent: ActivityResultEvent) {
        onActivityResult(activityResultEvent.getRequestCode(), activityResultEvent.getResultCode(), activityResultEvent.getData())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LocationDialog.LOCATION_REQUESTCODE -> {
                locationTextView.text = String.format("%s %s", mUtil.getCurrentAddress().locality, mUtil.getCurrentAddress().thoroughfare)
                //locationTextView.text = (data!!.extras["RESULT"] as Address).locality
                collapsibleToolbar.setTransition(R.id.start, R.id.end)
            }
            FILTER_REQUESTCODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val type = object : TypeToken<HashMap<Int, String>>() {}.type
                    themeHashMap = gson.fromJson<HashMap<Int, String>>(data?.getStringExtra("THEME"), type)
                    if(themeHashMap.size != 0) {
                        headerThemeTextView.text = String.format("%s 등 %d개 테마", themeHashMap.entries.iterator().next().value, themeHashMap.size)
                        headerThemeTextView.setTextColor(ContextCompat.getColor(context!!, R.color.marigold))
                        headerThemeBackGround.background.setColorFilter(ContextCompat.getColor(context!!, R.color.marigold), PorterDuff.Mode.SRC_IN)
                    } else {
                        headerThemeTextView.text = "테마 전체"
                        headerThemeTextView.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                        headerThemeBackGround.background.clearColorFilter()
                    }
                    collapsibleToolbar.setTransition(R.id.start, R.id.end)
                }
            }
        }
    }

    private fun initStoreServerData(recyclerView: RecyclerView?, PointX: Double, PointY: Double) {
        startLoader()
        baseServer?.storedong(PointX, PointY)?.enqueue(object : Callback<ArrayList<StoreData>> {
            override fun onResponse(call: Call<ArrayList<StoreData>>, response: Response<ArrayList<StoreData>>) {
                storeList = response.body()!!
                Collections.sort(storeList, mComparator.getDistanceComparator())
                storeAdapter.updateList(storeList)
                stopLoader()
            }

            override fun onFailure(call: Call<ArrayList<StoreData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun startLoader() {
        storeRecyclerViewContainer.visibility = View.INVISIBLE
        loader.visibility = View.VISIBLE
        loader.start()
    }

    private fun stopLoader() {
        storeRecyclerViewContainer.visibility = View.VISIBLE
        loader.visibility = View.INVISIBLE
        loader.stop()
    }

}