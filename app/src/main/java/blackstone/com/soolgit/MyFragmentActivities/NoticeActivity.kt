package blackstone.com.soolgit.MyFragmentActivities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import blackstone.com.soolgit.Adapter.NoticeRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.NoticeData
import blackstone.com.soolgit.R
import blackstone.com.soolgit.Util.BaseActivity
import blackstone.com.soolgit.Util.ySpacesItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_notice.*
import kotlinx.android.synthetic.main.notice_recyclerview_row.view.*
import net.cachapa.expandablelayout.ExpandableLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeActivity: BaseActivity(), View.OnClickListener {

    private var noticeList: ArrayList<NoticeData> = ArrayList()

    private lateinit var noticeRecyclerView: RecyclerView
    private lateinit var noticeRecyclerViewAdapter: NoticeRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        noticeRecyclerView = notice_recyclerview
//        noticeRecyclerView.addItemDecoration(ySpacesItemDecoration(this, 1, convertDpToPixel(21f, this).toInt(), false))
        noticeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        noticeRecyclerViewAdapter = NoticeRecyclerViewAdapter(this, noticeList)
        noticeRecyclerView.adapter = noticeRecyclerViewAdapter
        noticeRecyclerViewAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            (view.notice_row_expandable_container as ExpandableLayout).toggle()
        }

        baseServer?.notice()?.enqueue(object : Callback<ArrayList<NoticeData>> {
            override fun onResponse(call: Call<ArrayList<NoticeData>>, response: Response<ArrayList<NoticeData>>) {
                noticeRecyclerViewAdapter.updateList(response.body()!!)
            }

            override fun onFailure(call: Call<ArrayList<NoticeData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })

        notice_close_imagebutton.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        finish()
    }

}