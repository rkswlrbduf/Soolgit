package blackstone.com.soolgit.Fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import blackstone.com.soolgit.Adapter.AreaDongRecyclerViewAdapter
import blackstone.com.soolgit.Adapter.AreaStoreRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.DongData
import blackstone.com.soolgit.DataClass.StoreData
import blackstone.com.soolgit.R
import blackstone.com.soolgit.Util.BaseActivity.Companion.baseServer
import blackstone.com.soolgit.Util.xSpacesItemDecoration
import blackstone.com.soolgit.Util.ySpacesItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_area.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AreaFragment : Fragment() {

    private var dongAdapter: AreaDongRecyclerViewAdapter? = null
    private val gson = Gson()
    private val hashMap = HashMap<Int, Boolean>()
    private var mCurrentCounter = 0
    private lateinit var StoreAdapter: AreaStoreRecyclerViewAdapter

    private var visible: Boolean = false
    private var mView: View? = null
    private var onStarted: Boolean = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(mView != null && isVisibleToUser) {
            onResume()
            if(!onStarted) {
                onStarted = !onStarted
                initDongRecyclerview(mView?.main_area_content_dong_recyclerview, LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false), xSpacesItemDecoration(1, convertDpToPixel(7f, context!!).toInt(), false))
                initStoreRecyclerview(mView?.main_area_content_store_recyclerview, LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false), ySpacesItemDecoration(1, 40, false))
                initDongServerData(mView?.main_area_content_dong_recyclerview, mView?.main_area_content_store_recyclerview)
            }
        } else {
            onPause()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_area, container, false)
        return mView
    }

    private fun initDongRecyclerview(recyclerView: RecyclerView?, layoutManager: RecyclerView.LayoutManager, itemDecoration: xSpacesItemDecoration) {
        recyclerView?.layoutManager = layoutManager
        recyclerView?.addItemDecoration(itemDecoration)
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.setHasFixedSize(true)
    }

    private fun initStoreRecyclerview(recyclerView: RecyclerView?, layoutManager: RecyclerView.LayoutManager, itemDecoration: ySpacesItemDecoration) {
        recyclerView?.layoutManager = layoutManager
        recyclerView?.addItemDecoration(itemDecoration)
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.setHasFixedSize(true)
    }

    private fun initDongServerData(dongRecyclerView: RecyclerView?, storeRecyclerview: RecyclerView?) {
        baseServer?.dong()?.enqueue(object : Callback<List<DongData>> {
            override fun onResponse(call: Call<List<DongData>>, response: Response<List<DongData>>) {
                var dongList: List<DongData> = response.body()!!
                dongAdapter = AreaDongRecyclerViewAdapter(context!!, dongList, hashMap)
                dongAdapter?.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
                hashMap.put(0, true)
                dongRecyclerView?.adapter = dongAdapter
                dongAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                    if (position == 0) {
                        if (hashMap.get(position) != null) {
                            hashMap.put(1, true)
                        } else {
                            hashMap.clear()
                        }
                    } else {
                        if (hashMap.get(0) != null) {
                            hashMap.remove(0)
                        }
                    }
                    if (hashMap.get(position) != null) {
                        hashMap.remove(position)
                    } else {
                        hashMap.put(position, true)
                    }
                    if (hashMap.size == 0) {
                        hashMap.put(0, true)
                    }
                    adapter.notifyDataSetChanged()
                    initStoreServerData(storeRecyclerview, gson.toJson(hashMap).toString())
                }
                initStoreServerData(storeRecyclerview, gson.toJson(hashMap).toString())
            }

            override fun onFailure(call: Call<List<DongData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun initStoreServerData(recyclerView: RecyclerView?, dongList: String) {
        baseServer?.storedong(dongList)?.enqueue(object : Callback<List<StoreData>> {
            override fun onResponse(call: Call<List<StoreData>>, response: Response<List<StoreData>>) {
                var storeList: List<StoreData> = response.body()!!
                StoreAdapter = AreaStoreRecyclerViewAdapter(context!!, storeList)
                StoreAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
                recyclerView?.adapter = StoreAdapter
            }

            override fun onFailure(call: Call<List<StoreData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

}