package blackstone.com.soolgit

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import blackstone.com.soolgit.Adapter.NoticeRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.NoticeData
import blackstone.com.soolgit.Util.BaseActivity
import blackstone.com.soolgit.Util.ySpacesItemDecoration
import kotlinx.android.synthetic.main.activity_notice.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeActivity: BaseActivity() {

    private var noticeList: ArrayList<NoticeData>? = ArrayList()

    private lateinit var noticeRecyclerView: RecyclerView
    private lateinit var noticeRecyclerViewAdapter: NoticeRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        noticeRecyclerView = notice_recyclerview
        noticeRecyclerView.addItemDecoration(ySpacesItemDecoration(this, 1, convertDpToPixel(21f, this).toInt(), false))
        noticeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        noticeRecyclerViewAdapter = NoticeRecyclerViewAdapter(this, noticeList)
        noticeRecyclerView.adapter = noticeRecyclerViewAdapter

        baseServer?.notice()?.enqueue(object : Callback<ArrayList<NoticeData>> {
            override fun onResponse(call: Call<ArrayList<NoticeData>>, response: Response<ArrayList<NoticeData>>) {
                noticeRecyclerViewAdapter.updateList(response.body()!!)
            }

            override fun onFailure(call: Call<ArrayList<NoticeData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })


    }



}