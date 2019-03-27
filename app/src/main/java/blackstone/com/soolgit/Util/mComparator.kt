package blackstone.com.soolgit.Util

import android.location.Location
import blackstone.com.soolgit.DataClass.StoreData
import com.google.android.gms.maps.model.LatLng

class mComparator(mUtil: MyUtil) {

    private var distanceComparator: Comparator<StoreData>

    init {
        distanceComparator = object: Comparator<StoreData> {
            override fun compare(o1: StoreData?, o2: StoreData?): Int {
                val locationA = Location("")
                val pointA = LatLng(o1?.StorePointY!!, o1?.StorePointX!!)
                locationA.latitude = pointA.latitude
                locationA.longitude = pointA.longitude
                val locationB = Location("")
                val pointB = LatLng(o2?.StorePointY!!, o2?.StorePointX!!)
                locationB.latitude = pointB.latitude
                locationB.longitude = pointB.longitude
                val distanceA = mUtil.getCurrentLocation().distanceTo(locationA)
                val distanceB = mUtil.getCurrentLocation().distanceTo(locationB)
                return Integer.valueOf(distanceA.toInt()).compareTo(distanceB.toInt())
            }
        }
    }

    fun getDistanceComparator(): Comparator<StoreData> {
        return distanceComparator
    }


}