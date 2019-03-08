package blackstone.com.soolgit.MyFragmentActivities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import blackstone.com.soolgit.Adapter.HistoryRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.HistoryData
import blackstone.com.soolgit.R
import blackstone.com.soolgit.Util.BaseActivity
import blackstone.com.soolgit.Util.ySpacesItemDecoration
import kotlinx.android.synthetic.main.activity_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity: BaseActivity() {

    private var historyList: ArrayList<HistoryData>? = ArrayList()

    private lateinit var historyecyclerView: RecyclerView
    private lateinit var historyRecyclerViewAdapter: HistoryRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyecyclerView = history_recyclerview
        historyecyclerView.addItemDecoration(ySpacesItemDecoration(this, 1, convertDpToPixel(21f, this).toInt(), false))
        historyecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        historyRecyclerViewAdapter = HistoryRecyclerViewAdapter(this, historyList)
        historyecyclerView.adapter = historyRecyclerViewAdapter

        baseServer?.history()?.enqueue(object : Callback<ArrayList<HistoryData>> {
            override fun onResponse(call: Call<ArrayList<HistoryData>>, response: Response<ArrayList<HistoryData>>) {
                historyRecyclerViewAdapter.updateList(response.body()!!)
            }
            override fun onFailure(call: Call<ArrayList<HistoryData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }
}