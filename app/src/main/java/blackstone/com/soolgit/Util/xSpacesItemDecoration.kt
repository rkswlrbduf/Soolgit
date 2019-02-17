package blackstone.com.soolgit.Util

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


class xSpacesItemDecoration(val spanCount: Int, val spacing: Int, val includeEdge: Boolean) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {


        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column

        /*outRect.left = column * spacing // column * ((1f / spanCount) * spacing)
        outRect.right = spacing - (column + 1) * spacing // spacing - (column + 1) * ((1f /    spanCount) * spacing)
        */
        //if (position >= spanCount) {
//        outRect.left = spacing // item top
//        outRect.right = spacing // item top
        //}
        if (includeEdge) {
            if (position >= spanCount) {
                outRect.left = spacing // spacing - column * ((1f / spanCount) * spacing)
            }
        } else {
            outRect.left = spacing // item top
        }

    }
}