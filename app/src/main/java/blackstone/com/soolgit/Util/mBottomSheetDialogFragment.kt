package blackstone.com.soolgit.Util


import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import blackstone.com.soolgit.Adapter.StoreServiceRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.StoreServiceData
import blackstone.com.soolgit.R
import blackstone.com.soolgit.StoreActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.support.v4.os.HandlerCompat.postDelayed


class mBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var storeServiceList: ArrayList<StoreServiceData>? = ArrayList()

    private lateinit var storeServiceRecyclerView: RecyclerView
    private lateinit var storeServiceRecyclerViewAdapter: StoreServiceRecyclerViewAdapter
    private lateinit var behavior: BottomSheetBehavior<View>
    private lateinit var toast: Toast
    private lateinit var mUtil: MyUtil
    private lateinit var serviceDialog: ServiceDialog
    private lateinit var clickListener: StoreServiceRecyclerViewAdapter.ClickListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_dialog, container, false)
        toast = Toast.makeText(context, "CLICKED", Toast.LENGTH_SHORT)

        mUtil = MyUtil(context)

        storeServiceRecyclerView = view.store_service_recyclerview
        storeServiceRecyclerView.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        storeServiceRecyclerView.addItemDecoration(ySpacesItemDecoration(1, mUtil.convertDpToPixel(24f, context!!).toInt(), false))
        storeServiceRecyclerView.setHasFixedSize(true)
        storeServiceRecyclerView.isNestedScrollingEnabled = true
        storeServiceRecyclerViewAdapter = StoreServiceRecyclerViewAdapter(context!!, storeServiceList)

        storeServiceRecyclerViewAdapter.setClickListener(object: StoreServiceRecyclerViewAdapter.ClickListener{
            override fun onClick() {
                dismiss()
                serviceDialog = ServiceDialog(context as StoreActivity)
                serviceDialog.show()
            }
        })
        storeServiceRecyclerView.adapter = storeServiceRecyclerViewAdapter
        serviceServer("1")
        return view
    }

    private fun serviceServer(storeID: String) {
        BaseActivity.baseServer?.storeservice("1")?.enqueue(object : Callback<ArrayList<StoreServiceData>> {
            override fun onResponse(call: Call<ArrayList<StoreServiceData>>, response: Response<ArrayList<StoreServiceData>>) {
                Log.d("TAG", response.body()!!.size.toString())
                storeServiceRecyclerViewAdapter.updateList(response.body()!!)
            }

            override fun onFailure(call: Call<ArrayList<StoreServiceData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }

}
