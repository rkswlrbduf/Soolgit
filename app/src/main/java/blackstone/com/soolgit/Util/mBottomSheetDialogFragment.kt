package blackstone.com.soolgit.Util


import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import blackstone.com.soolgit.Adapter.StoreServiceRecyclerViewAdapter
import blackstone.com.soolgit.DataClass.ServiceCompleteData
import blackstone.com.soolgit.DataClass.StoreDetailData
import blackstone.com.soolgit.DataClass.StoreServiceData
import blackstone.com.soolgit.R
import blackstone.com.soolgit.StoreActivity
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class mBottomSheetDialogFragment() : BottomSheetDialogFragment() {

    private lateinit var serviceCompleteData: ServiceCompleteData
    private var storeServiceList: ArrayList<StoreServiceData>? = ArrayList()

    private lateinit var storeServiceRecyclerView: RecyclerView
    private lateinit var storeServiceRecyclerViewAdapter: StoreServiceRecyclerViewAdapter
    private lateinit var mUtil: MyUtil
    private lateinit var serviceDialog: ServiceDialog
    private var storeData: StoreDetailData? = StoreDetailData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_dialog, container, false)
        mUtil = MyUtil(context)
        storeData = arguments?.getParcelable("item")

        storeServiceRecyclerView = view.store_service_recyclerview
        storeServiceRecyclerView.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        storeServiceRecyclerView.addItemDecoration(ySpacesItemDecoration(1, mUtil.convertDpToPixel(24f, context!!).toInt(), false))
        storeServiceRecyclerView.setHasFixedSize(true)
        storeServiceRecyclerView.isNestedScrollingEnabled = true
        storeServiceRecyclerViewAdapter = StoreServiceRecyclerViewAdapter(context!!, storeServiceList)

        storeServiceRecyclerViewAdapter.setClickListener(object: StoreServiceRecyclerViewAdapter.ClickListener{
            override fun onClick(position: Int) {
                dismiss()
                serviceCompleteData = ServiceCompleteData(
                        storeData?.STORE_ID!!,
                        mUtil.ID,
                        storeData?.STORE_NM!!,
                        storeData?.Store_M_LCN!!,
                        storeServiceList!![position].SERVICE_NM!!,
                        storeServiceList!![position].SERVICE_COST!!,
                        storeServiceList!![position].SERVICE_IMG!!,
                        storeData?.STORE_CODE!!
                )
                serviceDialog = ServiceDialog(context as StoreActivity, serviceCompleteData)
                serviceDialog.show()
            }
        })
        storeServiceRecyclerView.adapter = storeServiceRecyclerViewAdapter
        serviceServer(storeData?.STORE_ID)
        return view
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    private fun serviceServer(storeID: String?) {
        BaseActivity.baseServer?.storeservice(storeID)?.enqueue(object : Callback<ArrayList<StoreServiceData>> {
            override fun onResponse(call: Call<ArrayList<StoreServiceData>>, response: Response<ArrayList<StoreServiceData>>) {
                storeServiceRecyclerViewAdapter.updateList(response.body()!!)
            }

            override fun onFailure(call: Call<ArrayList<StoreServiceData>>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })
    }
}
