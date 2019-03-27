package blackstone.com.soolgit

import android.os.Bundle
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import blackstone.com.soolgit.Util.BaseActivity
import com.amazonaws.util.IOUtils
import kotlinx.android.synthetic.main.activity_location_privacy.*

class LocationPrivacyActivity : BaseActivity() {

    private lateinit var locationPrivacyContentTextView: TextView
    private lateinit var locationPrivacyBackImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_privacy)

        locationPrivacyContentTextView = location_privacy_content_textView
        locationPrivacyBackImageView = location_privacy_title_back_imageView

        locationPrivacyContentTextView.text = Html.fromHtml(IOUtils.toString(assets.open("privacy_policy.html")))
        locationPrivacyBackImageView.setOnClickListener {
            finish()
        }
    }
}