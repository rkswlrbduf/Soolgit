package blackstone.com.soolgit

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import blackstone.com.soolgit.Adapter.SearchContentRecyclerViewAdapter
import blackstone.com.soolgit.Adapter.SearchRecentRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.StoreData
import blackstone.com.soolgit.Util.BaseActivity
import blackstone.com.soolgit.Util.ySpacesItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : BaseActivity() {

    private lateinit var recentAdapter: SearchRecentRecyclerViewAdapter
    private lateinit var contentAdapter: SearchContentRecyclerViewAdapter
    private var recentArrayList: ArrayList<String>? = ArrayList()
    private var contentArrayList: ArrayList<StoreData>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initContentRecyclerView()
        initRecentRecyclerView()

        search_header_edittext.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(s?.length == 0) {
                    contentArrayList?.clear()
                    contentAdapter.notifyDataSetChanged()
                    showRecentContainer()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        search_header_edittext.setOnKeyListener { view, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                search_header_icon_imagebutton.performClick()
                true
            }
            false
        }
        search_header_icon_imagebutton.setOnClickListener {
            if(search_header_edittext.text != null) {
                hideRecentContainer()
                serverContentRecyclerview(search_header_edittext.text.toString())
            }
        }
        search_header_back_imagebutton.setOnClickListener { finish() }
    }

    private fun initRecentRecyclerView() {
        recentArrayList = arrayListOf("TEST1", "TEST2")
        recentAdapter = SearchRecentRecyclerViewAdapter(recentArrayList)
        search_recent_recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        search_recent_recyclerview.addItemDecoration(ySpacesItemDecoration(1, convertDpToPixel(15f, this).toInt(), false))
        search_recent_recyclerview.adapter = recentAdapter
        recentAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.search_row_delete_imagebutton -> {
                    recentArrayList?.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
                R.id.search_row_recent_textview -> {
                    search_header_edittext.setText(recentArrayList!![position])
                }
            }
        }
    }

    private fun serverContentRecyclerview(text: String) {
        baseServer?.search(text)?.enqueue(object : Callback<ArrayList<StoreData>> {
            override fun onResponse(call: Call<ArrayList<StoreData>>, response: Response<ArrayList<StoreData>>) {
                contentAdapter.updateList(response.body()!!)
            }
            override fun onFailure(call: Call<ArrayList<StoreData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

    private fun initContentRecyclerView() {
        contentAdapter = SearchContentRecyclerViewAdapter(this, contentArrayList)
        search_content_recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        search_content_recyclerview.addItemDecoration(ySpacesItemDecoration(this, 1, convertDpToPixel(25f, this).toInt(), false))
        search_content_recyclerview.adapter = contentAdapter
    }

    private fun showRecentContainer() {
        search_recent_container.visibility = View.VISIBLE
    }

    private fun hideRecentContainer() {
        search_recent_container.visibility = View.INVISIBLE
    }

}