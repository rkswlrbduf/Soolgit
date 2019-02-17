package blackstone.com.soolgit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import blackstone.com.soolgit.Util.BaseActivity
import blackstone.com.soolgit.Util.MyUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_current_location.*


class CurrentLocationActivity : BaseActivity(), OnMapReadyCallback {

    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002
    private var locationTextView: TextView? = null

    private lateinit var map: GoogleMap
    private lateinit var mUtil: MyUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_location)

        initSetting()
        mUtil = MyUtil(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.current_location_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mUtil.callPermission(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        mUtil.setLocationTextView(locationTextView)

        current_location_confirm.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("RESULT", locationTextView?.text)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    fun initSetting() {
        locationTextView = current_location_textview
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        Log.d("TAG", mUtil?.getPermissionGranted().toString())
        if(mUtil.getPermissionGranted()) {
            mUtil?.setMap(map)
            mUtil?.updateLocationUI()
            mUtil?.getDeviceLocation()
        }
        mUtil.setPassiveMapListener()
    }



}