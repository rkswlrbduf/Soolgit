package blackstone.com.soolgit.Fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import blackstone.com.soolgit.MyFragmentActivities.HistoryActivity
import blackstone.com.soolgit.MyFragmentActivities.NoticeActivity
import blackstone.com.soolgit.MyFragmentActivities.SettingActivity
import blackstone.com.soolgit.R
import blackstone.com.soolgit.Util.MyUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kakao.plusfriend.PlusFriendService
import kotlinx.android.synthetic.main.fragment_my.view.*
import q.rorbin.badgeview.QBadgeView

class MYFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my, container, false)
        val mUtil = MyUtil(context)
        view.main_my_content_etc_notice_container.setOnClickListener(this)
        view.main_my_content_etc_history_textview.setOnClickListener(this)
        view.main_my_content_etc_service_center_container.setOnClickListener(this)
        view.main_my_content_etc_setting_container.setOnClickListener(this)
        view.main_my_content_info_name_textview.text = mUtil.NM + "님"
        Glide.with(context!!)
                .load(mUtil.IMG)
                .apply(RequestOptions.placeholderOf(ColorDrawable(ContextCompat.getColor(context!!, R.color.very_light_pink))))
                .apply(RequestOptions.overrideOf(view.main_my_content_info_profile_imageview.width, view.main_my_content_info_profile_imageview.height))
                .into(view.main_my_content_info_profile_imageview)
        QBadgeView(context).bindTarget(view.main_my_content_etc_service_center_textview)
                .setBadgeText("")
                .setBadgeGravity(Gravity.END or Gravity.TOP)
                .setBadgeTextColor(Color.parseColor("#ffbd00"))
                .setBadgePadding(convertDpToPixel(0.9f, context!!), true)
                .setBadgeTextSize(convertDpToPixel(2f, context!!), true)
                .setBadgeBackgroundColor(Color.parseColor("#ffbd00"))
                .setGravityOffset(convertDpToPixel(7f, context!!), convertDpToPixel(3f, context!!), true)
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.main_my_content_etc_notice_container -> startActivity(Intent(context, NoticeActivity::class.java))
            R.id.main_my_content_etc_history_textview -> startActivity(Intent(context, HistoryActivity::class.java))
            R.id.main_my_content_etc_service_center_container -> PlusFriendService.getInstance().chat(context, "_VQxnDC")
            R.id.main_my_content_etc_setting_container -> startActivity(Intent(context, SettingActivity::class.java))
        }
    }
}