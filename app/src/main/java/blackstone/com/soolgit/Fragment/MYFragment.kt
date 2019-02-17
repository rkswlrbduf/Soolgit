package blackstone.com.soolgit.Fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import blackstone.com.soolgit.NoticeActivity
import blackstone.com.soolgit.R
import kotlinx.android.synthetic.main.fragment_my.view.*

class MYFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my, container, false)

        view.main_my_content_etc_notice_textview.setOnClickListener {
            startActivity(Intent(context, NoticeActivity::class.java))
        }

        return view

    }

}