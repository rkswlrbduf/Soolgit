package blackstone.com.soolgit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import blackstone.com.soolgit.Adapter.MainTabPagerAdapter
import blackstone.com.soolgit.Util.ActivityResultEvent
import blackstone.com.soolgit.Util.BaseActivity
import blackstone.com.soolgit.Util.BusProvider
import blackstone.com.soolgit.Util.TabEntity
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val tabList = arrayOf("홈", "지역・종류", "내주변", /*"테마",*/ "MY")
    private var mTabEntities: ArrayList<CustomTabEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewPager()
        initTab()
    }

    val mIconUnselectIds = arrayOf(R.drawable.home_inactive,
            R.drawable.location_inactive,
            R.drawable.map_inactive,
//            R.drawable.theme_inactive,
            R.drawable.my_inactive)

    val mIconSelectIds = arrayOf(R.drawable.home_active,
            R.drawable.location_active,
            R.drawable.map_active,
//            R.drawable.theme_active,
            R.drawable.my_active)

    fun initViewPager() {
        val adapter = MainTabPagerAdapter(supportFragmentManager)
        main_viewpager.adapter = adapter
        main_viewpager.offscreenPageLimit = adapter.count
    }

    fun initTab() {
        for (i in 0..3) {
            mTabEntities.add(TabEntity(tabList[i], mIconSelectIds[i], mIconUnselectIds[i]))
        }
        main_tablayout.setTabData(mTabEntities)
        main_tablayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                main_viewpager.setCurrentItem(position, false)
            }

            override fun onTabReselect(position: Int) {
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            BusProvider().getInstance().post(ActivityResultEvent(requestCode, resultCode, data))
        }
    }
}