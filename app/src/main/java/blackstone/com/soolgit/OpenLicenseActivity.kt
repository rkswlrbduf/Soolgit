package blackstone.com.soolgit

import android.os.Bundle
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import blackstone.com.soolgit.Util.BaseActivity
import com.amazonaws.util.IOUtils
import kotlinx.android.synthetic.main.activity_open_license.*
import kotlinx.android.synthetic.main.activity_private_privacy.*

class OpenLicenseActivity : BaseActivity() {

    private lateinit var openLicenseContentTextView: TextView
    private lateinit var openLicenseBackImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_license)

        openLicenseContentTextView = open_license_content_textView
        openLicenseBackImageView = open_license_title_back_imageView

        openLicenseContentTextView.text = Html.fromHtml(IOUtils.toString(assets.open("open_license.html")))
        openLicenseBackImageView.setOnClickListener {
            finish()
        }
    }
}