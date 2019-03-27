package blackstone.com.soolgit

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import blackstone.com.soolgit.Adapter.FilterCategoryRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.CategoryData
import blackstone.com.soolgit.DataClass.ThemeData
import blackstone.com.soolgit.Util.BaseActivity
import blackstone.com.soolgit.Util.MyUtil
import blackstone.com.soolgit.Util.TagGroup
import blackstone.com.soolgit.Util.ySpacesItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.filter_category_recyclerview_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilterActivity : BaseActivity(), View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener, TagGroup.OnTagClickListener {

    private var filterCategoryArrayList: ArrayList<CategoryData> = ArrayList()
    private var filterThemeArrayList: ArrayList<ThemeData> = ArrayList()
    private var returnIntent: Intent = Intent()
    private var preCheckedPosition: Int = 0
    private var gson = Gson()

    private lateinit var mUtil: MyUtil
    private lateinit var filterCategoryRecyclerView: RecyclerView
    private lateinit var filterCategoryRecyclerViewAdapter: FilterCategoryRecyclerViewAdapter
    private lateinit var filterThemeTagGroup: TagGroup
    //private lateinit var filterThemeRecyclerView: RecyclerView
    //private lateinit var filterThemeRecyclerViewAdapter: FilterThemeRecyclerViewAdapter
    private lateinit var filterBackImageView: ImageView
    private lateinit var filterSortRecommendTextView: TextView
    private lateinit var filterSortZZimTextView: TextView
    private lateinit var filterSortDistanceTextView: TextView
    private lateinit var filterApplyTextView: TextView
    private lateinit var preCheckedTextView: TextView
    private lateinit var filterCategoryChoiceTextView: TextView
    private lateinit var filterThemeChoiceTextView: TextView
    private var categoryCheckedData: ArrayList<CategoryData> = ArrayList()
    //private var categoryHashMap: HashMap<Int, String> = HashMap()

    private fun initSetting() {
        mUtil = MyUtil(this)
        filterThemeArrayList = arrayListOf()

        filterCategoryRecyclerView = filter_category_recyclerView
        filterCategoryRecyclerViewAdapter = FilterCategoryRecyclerViewAdapter(this, filterCategoryArrayList)
        //filterThemeRecyclerView = filter_theme_recyclerView
        //filterThemeRecyclerViewAdapter = FilterThemeRecyclerViewAdapter(this, filterThemeArrayList)
        filterBackImageView = filter_back_imageView
        filterSortRecommendTextView = filter_sort_recommend_textView
        preCheckedTextView = filterSortRecommendTextView
        filterSortZZimTextView = filter_sort_zzim_textView
        filterSortDistanceTextView = filter_sort_distance_textView
        filterApplyTextView = filter_apply_textView
        filterCategoryChoiceTextView = filter_category_choice_textView
        filterThemeChoiceTextView = filter_theme_choice_textView
        filterThemeTagGroup = filter_theme_tagGroup
        filterCategoryRecyclerView.layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false)
        //filterThemeRecyclerView.layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        initSetting()

        filterCategoryRecyclerView.addItemDecoration(ySpacesItemDecoration(2, mUtil.convertDpToPixel(12f, this).toInt(), true))
        filterCategoryRecyclerView.layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false)
        filterCategoryChoiceTextView.text = mUtil.FILTERCATEGORY.size.toString()
        filterCategoryRecyclerViewAdapter.onItemChildClickListener = this
        filterCategoryRecyclerView.adapter = filterCategoryRecyclerViewAdapter

        filterBackImageView.setOnClickListener(this)
        filterSortRecommendTextView.setOnClickListener(this)
        filterSortZZimTextView.setOnClickListener(this)
        filterSortDistanceTextView.setOnClickListener(this)
        filterApplyTextView.setOnClickListener(this)
        filterThemeTagGroup.setOnTagClickListener(this)

        serverThemeInform()
        serverCategoryInform()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.filter_back_imageView -> {
                finish()
            }
            R.id.filter_sort_recommend_textView -> {
                setSortToggle(filterSortRecommendTextView)
                preCheckedPosition = 0
            }
            R.id.filter_sort_zzim_textView -> {
                setSortToggle(filterSortZZimTextView)
                preCheckedPosition = 1
            }
            R.id.filter_sort_distance_textView -> {
                setSortToggle(filterSortDistanceTextView)
                preCheckedPosition = 2
            }
            R.id.filter_apply_textView -> {
                returnIntent.putExtra("SORT", preCheckedPosition)
                (filterCategoryRecyclerView.adapter as FilterCategoryRecyclerViewAdapter).data.forEach {
                    if ((it as CategoryData).CATEGORY_CHECKED)
                        categoryCheckedData.add(it)
                }
                log(categoryCheckedData.size.toString())
                mUtil.FILTERCATEGORY = categoryCheckedData
                mUtil.FILTERTHEME = filterThemeTagGroup.checkedTags
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val item = adapter.getItem(position) as CategoryData
        if (item.CATEGORY_CHECKED) {
            view.filter_category_row_icon_imageView.clearColorFilter()
            view.filter_category_row_textView.setTextColor(ContextCompat.getColor(this@FilterActivity, R.color.very_light_pink_two))
            item.CATEGORY_CHECKED = false
            filterCategoryChoiceTextView.text = (filterCategoryChoiceTextView.text.toString().toInt() - 1).toString()
        } else {
            view.filter_category_row_icon_imageView.setColorFilter(ContextCompat.getColor(this@FilterActivity, R.color.marigold), PorterDuff.Mode.SRC_IN)
            view.filter_category_row_textView.setTextColor(ContextCompat.getColor(this@FilterActivity, R.color.marigold))
            item.CATEGORY_CHECKED = true
            filterCategoryChoiceTextView.text = (filterCategoryChoiceTextView.text.toString().toInt() + 1).toString()
        }
        //categoryAfterData = adapter.data as ArrayList<CategoryData>
    }

    override fun onTagClick(checkedCount: Int) {
        filterThemeChoiceTextView.text = checkedCount.toString()
    }

    private fun setSortToggle(item: TextView) {
        preCheckedTextView.background = ContextCompat.getDrawable(this, R.drawable.sort_padding_10_unchecked)
        preCheckedTextView.setTextColor(ContextCompat.getColor(this, R.color.very_light_pink_two))
        item.background = ContextCompat.getDrawable(this, R.drawable.sort_padding_10_checked)
        item.setTextColor(ContextCompat.getColor(this, R.color.black_two))
        preCheckedTextView = item
    }

    private fun serverCategoryInform() {
        baseServer?.category()?.enqueue(object : Callback<java.util.ArrayList<CategoryData>> {
            override fun onResponse(call: Call<java.util.ArrayList<CategoryData>>, response: Response<java.util.ArrayList<CategoryData>>) {
                filterCategoryRecyclerViewAdapter.updateList(response.body()!!)
            }

            override fun onFailure(call: Call<java.util.ArrayList<CategoryData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun serverThemeInform() {
        baseServer?.theme()?.enqueue(object : Callback<java.util.ArrayList<ThemeData>> {
            override fun onResponse(call: Call<java.util.ArrayList<ThemeData>>, response: Response<java.util.ArrayList<ThemeData>>) {
                filterThemeTagGroup.setTags(response.body()!!)
            }

            override fun onFailure(call: Call<java.util.ArrayList<ThemeData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

}