package blackstone.com.soolgit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import blackstone.com.soolgit.Adapter.StoreServiceRecyclerViewAdapter
import blackstone.com.soolgit.Util.BaseActivity
import blackstone.com.soolgit.Util.MyUtil
import blackstone.com.soolgit.Util.PermissionDialog

const val SPLASH_TIME_OUT = 2000

class SplashActivity : BaseActivity() {

    private var mUtil: MyUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mUtil = MyUtil(this)

        if (mUtil?.ID.isNullOrEmpty()) {
            Handler().postDelayed({
                val i = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(i)
                finish()
            }, SPLASH_TIME_OUT.toLong())
        } else {
            Handler().postDelayed({
                mUtil?.FILTERCATEGORY = ArrayList()
                mUtil?.FILTERTHEME = ArrayList()
                val i = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            }, SPLASH_TIME_OUT.toLong())
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val permission = PermissionDialog(this, Manifest.permission.ACCESS_FINE_LOCATION)
            permission.show()
            return false
        }
        return true
    }

}