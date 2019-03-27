package blackstone.com.soolgit

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.util.Linkify
import android.util.Log
import android.widget.Toast
import blackstone.com.soolgit.DataClass.NaverProfileData
import blackstone.com.soolgit.DataClass.NaverWebRequestData
import blackstone.com.soolgit.DataClass.UserData
import blackstone.com.soolgit.Util.BaseActivity
import blackstone.com.soolgit.Util.MyUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.kakao.auth.ErrorCode
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern

const val TYPE_KAKAO = "Kakao"
const val TYPE_NAVER = "Naver"
const val TYPE_GOOGLE = "Google"

class LoginActivity : BaseActivity() {

    val RC_SIGN_IN = 123
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mOAuthLoginModule: OAuthLogin
    private lateinit var mKakaoSessionCallback: KakaoSessionCallback
    private lateinit var userData: UserData
    private lateinit var mUtil: MyUtil
    private lateinit var textTransform: Linkify.TransformFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initGoogleLogin()
        initNaverLogin()

        mUtil = MyUtil(this)
        mKakaoSessionCallback = KakaoSessionCallback()

        login_google_button.setOnClickListener({ v -> googleLogin() })
        login_naver_button.setOnClickListener({ v -> login_naver_real_button.performClick() })
        login_kakao_button.setOnClickListener({ v -> login_kakao_real_button.performClick() })
        /*logout_google_button.setOnClickListener({ v -> googleLogout() })
        logout_naver_button.setOnClickListener({ v -> naverLogout() })
        logout_kakao_button.setOnClickListener({ v -> kakaoLogout() })*/
        Session.getCurrentSession().addCallback(mKakaoSessionCallback)

        textTransform = Linkify.TransformFilter { match, url ->
            ""
        }

        val privatePrivacyPattern = Pattern.compile("개인정보처리방침")
        val locationPrivacyPattern = Pattern.compile("위치기반서비스 이용약관")

        Linkify.addLinks(login_agree_textView, privatePrivacyPattern, "private://", null, textTransform)
        Linkify.addLinks(login_agree_textView, locationPrivacyPattern, "location://", null, textTransform)
        login_agree_textView.setLinkTextColor(ContextCompat.getColor(this@LoginActivity,R.color.marigold))

    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(mKakaoSessionCallback)
    }

    private inner class KakaoSessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {
                override fun onFailure(errorResult: ErrorResult?) {
                    val message = "failed to get user info. msg=" + errorResult!!
                    val result = ErrorCode.valueOf(errorResult.errorCode)
                    if (result === ErrorCode.CLIENT_ERROR_CODE) {
                        finish()
                    } else {
                        //redirectMainActivity();
                    }
                }

                override fun onSessionClosed(errorResult: ErrorResult) {
                    Log.d("TAG", "onSessionClosed")
                }

                override fun onSuccess(res: MeV2Response) {
                    userData = UserData(res.id.toString(), res.nickname, res.thumbnailImagePath, TYPE_KAKAO, res.kakaoAccount.email)
                    redirectSexActivity(userData)
                }
            })
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Log.e("TAG", exception.printStackTrace().toString())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
    }

    fun initNaverLogin() {
        mOAuthLoginModule = OAuthLogin.getInstance()
        mOAuthLoginModule.init(this, getString(R.string.NAVER_OAUTH_CLIENT_ID), getString(R.string.NAVER_OAUTH_CLIENT_SECRET), getString(R.string.NAVER_OAUTH_CLIENT_NAME))
        val mOAuthLoginHandler = object : OAuthLoginHandler() {
            override fun run(success: Boolean) {
                if (success) {
                    val accessToken = mOAuthLoginModule.getAccessToken(this@LoginActivity)
                    val tokenType = mOAuthLoginModule.getTokenType(this@LoginActivity)
                    naverServer?.naveruser(tokenType + " " + accessToken)?.enqueue(object : retrofit2.Callback<NaverWebRequestData> {
                        override fun onResponse(call: Call<NaverWebRequestData>, response: Response<NaverWebRequestData>) {
                            val res = (response.body() as NaverWebRequestData).response as NaverProfileData
                            userData = UserData(res?.id!!, res?.name!!, res?.profile_image!!, TYPE_NAVER, res.email, birthday = res.birthday)
                            redirectSexActivity(userData)
                        }

                        override fun onFailure(call: Call<NaverWebRequestData>, t: Throwable) {
                            Log.e("HPRVAdapter Retro Err", t.toString())
                        }
                    })
                } else {
                    val errorCode = mOAuthLoginModule.getLastErrorCode(this@LoginActivity).getCode()
                    val errorDesc = mOAuthLoginModule.getLastErrorDesc(this@LoginActivity)
                }
            }
        }
        login_naver_real_button.setOAuthLoginHandler(mOAuthLoginHandler)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                userData = UserData(account?.id!!, account.givenName!!, account.photoUrl.toString(), TYPE_GOOGLE, account?.email, idToken = account?.idToken)
                redirectSexActivity(userData)
                //firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
//        showProgressDialog(this)
//
//        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        redirectSexActivity()
//                    } else {
//
//                    }
//                    hideProgressDialog()
//                }
//    }

    private fun initGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this@LoginActivity.resources.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mAuth = FirebaseAuth.getInstance()
    }

    private fun googleLogin() {
        val signInIntent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    /**
    private fun googleLogout() {
    mAuth.signOut()
    mGoogleSignInClient.signOut()
    }

    private fun naverLogout() {
    DeleteTokenTask().execute()
    }

    private fun kakaoLogout() {
    UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
    override fun onCompleteLogout() {
    Toast.makeText(this@LoginActivity, "KAKAO LOGOUT", Toast.LENGTH_SHORT).show()
    }
    })
    }
     */

    private inner class DeleteTokenTask : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            mOAuthLoginModule.logoutAndDeleteToken(this@LoginActivity)
            return null
        }

        override fun onPostExecute(v: Void?) {

        }
    }

    private fun redirectSexActivity(userData: UserData) {
        val intent = Intent(this@LoginActivity, SexActivity::class.java)
        intent.putExtra("userData", userData)
        userServer?.createuser(userData)?.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                mUtil.ID = userData.id
                mUtil.NM = userData.name
                mUtil.IMG = userData.profileUrl
                mUtil.TYPE = userData.type
                startActivity(intent)
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
                finish()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("HPRVAdapter Retro Err", t.toString())
            }
        })

    }

}