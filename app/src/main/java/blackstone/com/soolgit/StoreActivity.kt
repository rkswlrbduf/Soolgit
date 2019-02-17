package blackstone.com.soolgit

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import blackstone.com.soolgit.Adapter.StoreImageSliderAdapter
import blackstone.com.soolgit.Adapter.StoreNoImageMenuRecyclerViewAdapter
import blackstone.com.soolgit.Adapter.StorePlaceRecyclerViewAdapter
import blackstone.com.soolgit.Adapter.StoreServiceRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.android.synthetic.main.activity_store.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.support.design.widget.BottomSheetDialog
import android.support.v4.widget.NestedScrollView
import android.view.MotionEvent
import blackstone.com.soolgit.Util.*


class StoreActivity : BaseActivity() {

    private var storeNoImageMenuList: ArrayList<StoreNoImageMenuData>? = ArrayList()
    private var storeImageList: ArrayList<StoreImageData>? = ArrayList()
    private var storePlaceList: ArrayList<StorePlaceData>? = ArrayList()
//    private var storeServiceList: ArrayList<StoreServiceData>? = ArrayList()
    private var dotsIndicator: WormDotsIndicator? = null

    private lateinit var storeViewPager: ViewPager
    private lateinit var storeViewPagerAdapter: StoreImageSliderAdapter
    private lateinit var storeImageMenuLeftImageView: ImageView
    private lateinit var storeImageMenuRightImageView: ImageView
    private lateinit var storeImageMenuLeftTitleTextView: TextView
    private lateinit var storeImageMenuRightTitleTextView: TextView
    private lateinit var storeImageMenuLeftCostTextView: TextView
    private lateinit var storeImageMenuRightCostTextView: TextView
    private lateinit var storeNoImageMenuRecyclerViewAdapter: StoreNoImageMenuRecyclerViewAdapter
    private lateinit var storeNoImageMenurecyclerView: RecyclerView
    private lateinit var storePlaceRecyclerView: RecyclerView
    private lateinit var storePlaceRecyclerViewAdapter: StorePlaceRecyclerViewAdapter
    private lateinit var storeContainer: NestedScrollView

//    private lateinit var storeServiceRecyclerView: RecyclerView
//    private lateinit var storeServiceRecyclerViewAdapter: StoreServiceRecyclerViewAdapter
//    private lateinit var behavior: BottomSheetBehavior<View>
//    private lateinit var serviceDialog: ServiceDialog

    private fun init() {
        storeImageMenuLeftImageView = store_menu_left_imageview
        storeImageMenuRightImageView = store_menu_right_imageview
        storeImageMenuLeftTitleTextView = store_menu_left_title_textview
        storeImageMenuRightTitleTextView = store_menu_right_title_textview
        storeImageMenuLeftCostTextView = store_menu_left_cost_textview
        storeImageMenuRightCostTextView = store_menu_right_cost_textview

        storeViewPager = store_image_viewpager
        storeViewPagerAdapter = StoreImageSliderAdapter(this, storeImageList)
        storeViewPager.adapter = storeViewPagerAdapter

        storeNoImageMenurecyclerView = store_menu_recyclerview
        storeNoImageMenurecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        storeNoImageMenurecyclerView.addItemDecoration(ySpacesItemDecoration(1, convertDpToPixel(6f, this).toInt(), false))
        storeNoImageMenurecyclerView.isNestedScrollingEnabled = false
        storeNoImageMenurecyclerView.setHasFixedSize(true)
        storeNoImageMenuRecyclerViewAdapter = StoreNoImageMenuRecyclerViewAdapter(this, storeNoImageMenuList)
        storeNoImageMenurecyclerView.adapter = storeNoImageMenuRecyclerViewAdapter

        storePlaceRecyclerView = store_place_recyclerview
        storePlaceRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        storePlaceRecyclerView.addItemDecoration(xSpacesItemDecoration(1, convertDpToPixel(10f, this).toInt(), true))
        storePlaceRecyclerView.setHasFixedSize(true)
        storePlaceRecyclerView.isNestedScrollingEnabled = false
        storePlaceRecyclerViewAdapter = StorePlaceRecyclerViewAdapter(this, storePlaceList)
        storePlaceRecyclerView.adapter = storePlaceRecyclerViewAdapter


//        storeContainer = store_container
//        storeContainer.setOnTouchListener(object: View.OnTouchListener{
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                return true
//            }
//        })

//        storeServiceRecyclerView = store_service_recyclerview
//        storeServiceRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        storeServiceRecyclerView.addItemDecoration(ySpacesItemDecoration(1, convertDpToPixel(24f, this).toInt(), false))
//        storeServiceRecyclerView.setHasFixedSize(true)
//        storeServiceRecyclerView.isNestedScrollingEnabled = true
//        storeServiceRecyclerViewAdapter = StoreServiceRecyclerViewAdapter(this, storeServiceList)
//        storeServiceRecyclerViewAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
//            //val item = adapter.getItem(position) as StoreServiceData
//            log("OK")
//            serviceDialog = ServiceDialog(this)
//            serviceDialog.show()
//
//        }
//        storeServiceRecyclerView.adapter = storeServiceRecyclerViewAdapter
//
//        behavior = BottomSheetBehavior.from(store_service_container)
//        behavior.state = BottomSheetBehavior.STATE_HIDDEN


        dotsIndicator = store_dotsindicator
        dotsIndicator?.setViewPager(storeViewPager)

        store_confirm_textview.setOnClickListener {
//            if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) behavior.state = BottomSheetBehavior.STATE_HIDDEN
//            else {
//                behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                storeServiceRecyclerView.focus
//            }
            val bottomSheetDialog = mBottomSheetDialogFragment()
            bottomSheetDialog.show(supportFragmentManager, "bottomSheet")

        }
//
//        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                if (newState == BottomSheetBehavior.STATE_HIDDEN)
//                    dim_bg.visibility = View.GONE
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                dim_bg.visibility = View.VISIBLE
//                dim_bg.alpha = slideOffset
//            }
//        })


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        init()
        noImageMenuServer(intent.getStringExtra("ID"))
        headerImageServer(intent.getStringExtra("ID"))
        imageMenuServer(intent.getStringExtra("ID"))
        placeServer(intent.getStringExtra("ID"))
//        serviceServer(intent.getStringExtra("ID"))
    }

    private fun headerImageServer(storeID: String) {
        baseServer?.storeimage(storeID)?.enqueue(object : Callback<java.util.ArrayList<StoreImageData>> {
            override fun onResponse(call: Call<java.util.ArrayList<StoreImageData>>, response: Response<java.util.ArrayList<StoreImageData>>) {
                log(response.body()!!.toString())
                storeViewPagerAdapter.updateList(response.body()!!)
            }

            override fun onFailure(call: Call<java.util.ArrayList<StoreImageData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun imageMenuServer(storeID: String) {
        baseServer?.storeimagemenu(storeID)?.enqueue(object : Callback<ArrayList<StoreImageMenuData>> {
            override fun onResponse(call: Call<ArrayList<StoreImageMenuData>>, response: Response<ArrayList<StoreImageMenuData>>) {
                val res = response.body()
                Glide.with(this@StoreActivity)
                        .load(res!![0].MENU_IMG_PATH)
                        .apply(RequestOptions().centerCrop())
                        .apply(RequestOptions.overrideOf(storeImageMenuLeftImageView.width, storeImageMenuLeftImageView.height))
                        .apply(RequestOptions.placeholderOf(R.drawable.garyplaceholder))
                        .into(storeImageMenuLeftImageView)
                Glide.with(this@StoreActivity)
                        .load(res!![1].MENU_IMG_PATH)
                        .apply(RequestOptions().centerCrop())
                        .apply(RequestOptions.overrideOf(storeImageMenuRightImageView.width, storeImageMenuRightImageView.height))
                        .apply(RequestOptions.placeholderOf(R.drawable.garyplaceholder))
                        .into(storeImageMenuRightImageView)
                storeImageMenuLeftTitleTextView.text = res!![0].MENU_NM
                storeImageMenuRightTitleTextView.text = res!![1].MENU_NM
                storeImageMenuLeftCostTextView.text = res!![0].MENU_COST
                storeImageMenuLeftCostTextView.text = res!![1].MENU_COST
            }

            override fun onFailure(call: Call<ArrayList<StoreImageMenuData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun noImageMenuServer(storeID: String) {
        baseServer?.storenoimagemenu(storeID)?.enqueue(object : Callback<ArrayList<StoreNoImageMenuData>> {
            override fun onResponse(call: Call<ArrayList<StoreNoImageMenuData>>, response: Response<ArrayList<StoreNoImageMenuData>>) {
                storeNoImageMenuRecyclerViewAdapter.updateList(response.body()!!)
            }

            override fun onFailure(call: Call<ArrayList<StoreNoImageMenuData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun placeServer(storeID: String) {
        baseServer?.storeplace("1")?.enqueue(object : Callback<ArrayList<StorePlaceData>> {
            override fun onResponse(call: Call<ArrayList<StorePlaceData>>, response: Response<ArrayList<StorePlaceData>>) {
                storePlaceRecyclerViewAdapter.updateList(response.body()!!)
            }

            override fun onFailure(call: Call<ArrayList<StorePlaceData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

//    private fun serviceServer(storeID: String) {
//        baseServer?.storeservice("1")?.enqueue(object : Callback<ArrayList<StoreServiceData>> {
//            override fun onResponse(call: Call<ArrayList<StoreServiceData>>, response: Response<ArrayList<StoreServiceData>>) {
//                Log.d("TAG", response.body()!!.size.toString())
//                storeServiceRecyclerViewAdapter.updateList(response.body()!!)
//            }
//
//            override fun onFailure(call: Call<ArrayList<StoreServiceData>>, t: Throwable) {
//                Log.e("HPRVAdapter Retro Err", t.toString())
//            }
//        })
//    }

}