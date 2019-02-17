package blackstone.com.soolgit.Util

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import blackstone.com.soolgit.LoginActivity
import blackstone.com.soolgit.R
import blackstone.com.soolgit.SplashActivity
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.layout_permission_dialog.*
import com.gun0912.tedpermission.PermissionListener

class PermissionDialog(private val mContext: Context, private val permission: String) : Dialog(mContext, R.style.permissionDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.layout_permission_dialog)
        service_permission_confirm_textview.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                this@PermissionDialog.dismiss()
                TedPermission.with(mContext)
                        .setPermissionListener(object : PermissionListener {
                            override fun onPermissionGranted() {
                                val i = Intent(mContext as SplashActivity, LoginActivity::class.java)
                                mContext.startActivity(i)
                                mContext.finish()
                            }
                            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                                (mContext as SplashActivity).finish()
                            }
                        })
                        .setDeniedMessage("접근 권한이 필요합니다.\n 휴대폰 설정 > 앱 > 술깃한제안에서 권한은 허용해주세요.")
                        .setDeniedTitle("[술깃한제안] 권한이 거부되었습니다.")
                        .setPermissions(permission)
                        .check()
            }
        })
    }
}