package blackstone.com.soolgit.MyFragmentActivities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import blackstone.com.soolgit.*
import blackstone.com.soolgit.DataClass.LogoutTypeData
import blackstone.com.soolgit.Util.BaseActivity
import blackstone.com.soolgit.Util.MyUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.nhn.android.naverlogin.OAuthLogin
import kotlinx.android.synthetic.main.activity_setting.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUtil: MyUtil
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var logoutTextView: TextView
    private lateinit var locationPrivacyTextView: TextView
    private lateinit var privatePrivacyTextView: TextView
    private lateinit var openLicenseTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        mUtil = MyUtil(this)
        logoutTextView = setting_logout_textView
        locationPrivacyTextView = setting_gps_textView
        privatePrivacyTextView = setting_privacy_textView
        openLicenseTextView = setting_license_textView
        logoutTextView.setOnClickListener(this)
        locationPrivacyTextView.setOnClickListener(this)
        privatePrivacyTextView.setOnClickListener(this)
        openLicenseTextView.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.setting_logout_textView -> {
                logout()
            }
            R.id.setting_gps_textView -> {
                startActivity(Intent(this@SettingActivity, LocationPrivacyActivity::class.java))
            }
            R.id.setting_privacy_textView -> {
                startActivity(Intent(this@SettingActivity, PrivatePrivacyActivity::class.java))
            }
            R.id.setting_license_textView -> {
                startActivity(Intent(this@SettingActivity, OpenLicenseActivity::class.java))
            }
        }
    }

    private fun logout() {
        baseServer?.logout(mUtil.ID)?.enqueue(object: Callback<LogoutTypeData> {
            override fun onResponse(call: Call<LogoutTypeData>, response: Response<LogoutTypeData>) {
                when(response.body()?.USER_TYPE) {
                    "Naver" -> {
                        finishAffinity()
                        startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
                    }
                    "Kakao" -> {
                        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
                            override fun onCompleteLogout() {
                                finishAffinity()
                                startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
                            }
                        })
                    }
                    "Google" -> {
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(this@SettingActivity.resources.getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build()
                        mGoogleSignInClient = GoogleSignIn.getClient(this@SettingActivity, gso)
                        mAuth = FirebaseAuth.getInstance()
                        mAuth.signOut()
                        mGoogleSignInClient.signOut()
                        finishAffinity()
                        startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
                    }
                }

            }

            override fun onFailure(call: Call<LogoutTypeData>, t: Throwable) {
                log(t.localizedMessage)
                t.printStackTrace()
                log(t.message.toString())
            }
        })
    }

}