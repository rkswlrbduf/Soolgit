package blackstone.com.soolgit.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import blackstone.com.soolgit.R
import kotlinx.android.synthetic.main.util_age_recyclerview_row.view.*

class AgeAdapter(val context: Context, val list: Array<Int>) : RecyclerView.Adapter<AgeAdapter.mYear>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): mYear {
        return mYear(LayoutInflater.from(context).inflate(R.layout.util_age_recyclerview_row, p0, false))
    }

    override fun onBindViewHolder(p0: mYear, p1: Int) {
        p0.ageRecyclerViewRowTextview.text = list[p1].toString()
    }

    override fun getItemCount(): Int = list.size

    inner class mYear(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val ageRecyclerViewRowTextview = itemview.util_age_recyclerview_row_textview
    }

}