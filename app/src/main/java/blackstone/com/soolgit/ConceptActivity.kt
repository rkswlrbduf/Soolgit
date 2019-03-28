package blackstone.com.soolgit

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import blackstone.com.soolgit.Adapter.ConceptRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.StoreData
import blackstone.com.soolgit.Util.BaseActivity
import blackstone.com.soolgit.Util.MyUtil
import blackstone.com.soolgit.Util.ySpacesItemDecoration
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_concept.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ConceptActivity : BaseActivity() {

    private lateinit var mUtil: MyUtil

    private var storeList: ArrayList<StoreData> = ArrayList()
    private lateinit var conceptTopImageView: ImageView
    private lateinit var conceptStoreRecyclerView: RecyclerView
    private lateinit var conceptStoreRecyclerViewAdapter: ConceptRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concept)
        mUtil = MyUtil(this)

        conceptTopImageView = concept_top_imageView
        conceptStoreRecyclerView = concept_content_store_recyclerView
        conceptStoreRecyclerViewAdapter = ConceptRecyclerViewAdapter(this, storeList, mUtil.getCurrentLocation())
        Glide.with(this).load("https://s3.ap-northeast-2.amazonaws.com/soolgitbucket01/concept_content/concept1_top.png")
                .into(conceptTopImageView)
        conceptStoreRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        conceptStoreRecyclerView.adapter = conceptStoreRecyclerViewAdapter
        conceptStoreRecyclerView.addItemDecoration(ySpacesItemDecoration(1, 40, false))
        initStoreServerData(mUtil.getCurrentLocation().longitude, mUtil.getCurrentLocation().latitude)
    }

    private fun initStoreServerData(PointX: Double, PointY: Double) {
        val filterCategoryIDIntArray: ArrayList<Int> = ArrayList()
        val filterThemeIDIntArray: ArrayList<Int> = ArrayList()
        mUtil.FILTERCATEGORY.forEach {
            filterCategoryIDIntArray.add(it.CATEGORY_ID)
        }
        mUtil.FILTERTHEME.forEach {
            filterThemeIDIntArray.add(it.THEME_ID)
        }
        baseServer?.storedong(PointX, PointY, mUtil.getGson().toJson(filterCategoryIDIntArray), mUtil.getGson().toJson(filterThemeIDIntArray))?.enqueue(object : Callback<ArrayList<StoreData>> {
            override fun onResponse(call: Call<ArrayList<StoreData>>, response: Response<ArrayList<StoreData>>) {
                storeList = response.body()!!
                conceptStoreRecyclerViewAdapter.updateList(storeList)
            }
            override fun onFailure(call: Call<ArrayList<StoreData>>, t: Throwable) {
                showNetworkDialog(this@ConceptActivity)
            }
        })
    }

}