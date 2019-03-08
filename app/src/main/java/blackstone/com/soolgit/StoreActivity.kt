package blackstone.com.soolgit

import android.content.ActivityNotFoundException
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import blackstone.com.soolgit.Adapter.StoreImageSliderAdapter
import blackstone.com.soolgit.Adapter.StoreNoImageMenuRecyclerViewAdapter
import blackstone.com.soolgit.Adapter.StorePlaceRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.StoreDetailData
import blackstone.com.soolgit.DataClass.StoreImageMenuData
import blackstone.com.soolgit.DataClass.StoreNoImageMenuData
import blackstone.com.soolgit.DataClass.StorePlaceData
import blackstone.com.soolgit.Util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.android.synthetic.main.activity_store.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.net.Uri


class StoreActivity : BaseActivity(), View.OnClickListener {

    private var storeImageList: ArrayList<String>? = ArrayList()
    private var storeNoImageMenuList: ArrayList<StoreNoImageMenuData>? = ArrayList()
    private var storePlaceList: ArrayList<StorePlaceData>? = ArrayList()
    private var dotsIndicator: WormDotsIndicator? = null
    private var bundle: Bundle? = Bundle()
    private var storeDetailData: StoreDetailData = StoreDetailData()

    private lateinit var mUtil: MyUtil

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

    private lateinit var storeZzimImageView: ImageView
    private lateinit var storeShareImageView: ImageView
    private lateinit var storeConfirmTextView: TextView

    private lateinit var storeIntroTitleTextView: TextView
    private lateinit var storeIntroThemeTextView: TextView
    private lateinit var storeInformMapTextView: TextView
    private lateinit var storeInformTimeTextView: TextView
    private lateinit var storeInformCallTextView: TextView

    private lateinit var storeContainer: NestedScrollView
    private lateinit var storeID: String

    private fun init() {

        mUtil = MyUtil(this)

        storeImageMenuLeftImageView = store_menu_left_imageview
        storeImageMenuRightImageView = store_menu_right_imageview
        storeImageMenuLeftTitleTextView = store_menu_left_title_textview
        storeImageMenuRightTitleTextView = store_menu_right_title_textview
        storeImageMenuLeftCostTextView = store_menu_left_cost_textview
        storeImageMenuRightCostTextView = store_menu_right_cost_textview
        storeZzimImageView = store_zzim_imageview
        storeShareImageView = store_share_imageview
        storeViewPager = store_image_viewpager
        storeConfirmTextView = store_confirm_textview
        storeViewPagerAdapter = StoreImageSliderAdapter(this, storeImageList)
        storeViewPager.adapter = storeViewPagerAdapter
        storeIntroTitleTextView = store_intro_title_textview
        storeIntroThemeTextView = store_intro_theme_textview
        storeInformMapTextView = store_inform_map_textview
        storeInformTimeTextView = store_inform_time_textview
        storeInformCallTextView = store_inform_call_textview

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

        dotsIndicator = store_dotsindicator
        dotsIndicator?.setViewPager(storeViewPager)

        storeID = intent.getStringExtra("ID")

        storeZzimImageView.setOnClickListener(this)
        storeShareImageView.setOnClickListener(this)
        storeConfirmTextView.setOnClickListener(this)
        if (mUtil.ZZIM[storeID] != null) {
            storeZzimImageView.setImageResource(R.drawable.like_button_active)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        init()
        storeServer(storeID)
        imageMenuServer(storeID)
        noImageMenuServer(storeID)
        placeServer(storeID)
    }

    private fun storeServer(id: String) {
        baseServer?.storedetail(id)?.enqueue(object : Callback<StoreDetailData> {
            override fun onResponse(call: Call<StoreDetailData>, response: Response<StoreDetailData>) {
                storeDetailData = response.body()!!

                storeViewPagerAdapter.updateList(storeDetailData.IMG_PATH)
                storeIntroTitleTextView.text = storeDetailData.STORE_NM
                storeIntroThemeTextView.text = storeDetailData.THEME_NM?.joinToString(" ")
                storeInformMapTextView.text = storeDetailData.STORE_B_LCN
                storeInformTimeTextView.text = storeDetailData.STORE_TIME
                storeInformCallTextView.text = storeDetailData.STORE_CALL
            }

            override fun onFailure(call: Call<StoreDetailData>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun imageMenuServer(id: String) {
        baseServer?.storeimagemenu(id)?.enqueue(object : Callback<ArrayList<StoreImageMenuData>> {
            override fun onResponse(call: Call<ArrayList<StoreImageMenuData>>, response: Response<ArrayList<StoreImageMenuData>>) {
                val res = response.body()
                Glide.with(this@StoreActivity)
                        .load(res!![0].MENU_IMG_PATH)
                        .apply(RequestOptions().centerCrop())
                        .apply(RequestOptions.overrideOf(storeImageMenuLeftImageView.width, storeImageMenuLeftImageView.height))
                        .apply(RequestOptions.placeholderOf(ColorDrawable(ContextCompat.getColor(this@StoreActivity, R.color.very_light_pink))))
                        .into(storeImageMenuLeftImageView)
                Glide.with(this@StoreActivity)
                        .load(res[1].MENU_IMG_PATH)
                        .apply(RequestOptions().centerCrop())
                        .apply(RequestOptions.overrideOf(storeImageMenuRightImageView.width, storeImageMenuRightImageView.height))
                        .apply(RequestOptions.placeholderOf(ColorDrawable(ContextCompat.getColor(this@StoreActivity, R.color.very_light_pink))))
                        .into(storeImageMenuRightImageView)
                storeImageMenuLeftTitleTextView.text = res[0].MENU_NM
                storeImageMenuRightTitleTextView.text = res[1].MENU_NM
                storeImageMenuLeftCostTextView.text = res[0].MENU_COST
                storeImageMenuLeftCostTextView.text = res[1].MENU_COST
            }

            override fun onFailure(call: Call<ArrayList<StoreImageMenuData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun noImageMenuServer(id: String) {
        baseServer?.storenoimagemenu(id)?.enqueue(object : Callback<ArrayList<StoreNoImageMenuData>> {
            override fun onResponse(call: Call<ArrayList<StoreNoImageMenuData>>, response: Response<ArrayList<StoreNoImageMenuData>>) {
                storeNoImageMenuRecyclerViewAdapter.updateList(response.body()!!)
            }

            override fun onFailure(call: Call<ArrayList<StoreNoImageMenuData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun placeServer(id: String) {
        baseServer?.storeplace(id)?.enqueue(object : Callback<ArrayList<StorePlaceData>> {
            override fun onResponse(call: Call<ArrayList<StorePlaceData>>, response: Response<ArrayList<StorePlaceData>>) {
                storePlaceRecyclerViewAdapter.updateList(response.body()!!)
            }

            override fun onFailure(call: Call<ArrayList<StorePlaceData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.store_zzim_imageview -> {
                if (mUtil.ZZIM[storeID] == null) {
                    val hashMap = mUtil.ZZIM
                    hashMap.put(storeID, true)
                    mUtil.ZZIM = hashMap
                    storeZzimImageView.setImageResource(R.drawable.like_button_active)
                } else {
                    val hashMap = mUtil.ZZIM
                    hashMap.remove(storeID)
                    mUtil.ZZIM = hashMap
                    storeZzimImageView.setImageResource(R.drawable.like_button_inactvie)
                }
            }
            R.id.store_share_imageview -> {
                val message = "[술깃한제안]\n" +
                        mUtil.NM +
                        "님이 추천하는 술집!!\n\n" +
                        storeIntroTitleTextView.text + "\n" +
                        storeInformMapTextView.text + "\n" +
                        storeInformCallTextView.text + "\n" +
                        "지금 바로 술깃한제안 앱에서 바로 확인 해 보세요!"
                val shareDialog = ShareDialog(this)
                shareDialog.setOnShareDialogClickListener(object : ShareDialog.sharedCallBack {
                    override fun onKakaoClick() {
                        try {
                            val intent = Intent(Intent.ACTION_SEND)
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_TEXT, message)
                            intent.setPackage("com.kakao.talk")
                            startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse("market://details?id=" + "com.kakao.talk")
                            startActivity(intent)
                        }
                    }

                    override fun onSmsClick() {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.putExtra("sms_body", message)
                        intent.type = "vnd.android-dir/mms-sms"
                        startActivity(intent)
                    }
                })
                shareDialog.show()
            }
            R.id.store_confirm_textview -> {
                val bottomSheetDialog = mBottomSheetDialogFragment()
                bundle?.putParcelable("item", storeDetailData)
                bottomSheetDialog.arguments = bundle
                bottomSheetDialog.show(supportFragmentManager, "bottomSheet")
            }
        }
    }
}