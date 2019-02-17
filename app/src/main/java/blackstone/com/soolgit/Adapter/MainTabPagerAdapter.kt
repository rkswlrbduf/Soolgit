package blackstone.com.soolgit.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import blackstone.com.soolgit.Fragment.*

class MainTabPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position : Int): Fragment? {
        when(position) {
            0 -> return HomeFragment()
            1 -> return AreaFragment()
            2 -> return MapFragment()
            //3 -> return ThemeFragment()
            3 -> return MYFragment()
            else -> return null
        }
    }

    override fun getCount(): Int = 4

}