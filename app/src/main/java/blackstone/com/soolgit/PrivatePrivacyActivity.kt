package blackstone.com.soolgit

import android.os.Bundle
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import blackstone.com.soolgit.Util.BaseActivity
import com.amazonaws.util.IOUtils
import kotlinx.android.synthetic.main.activity_private_privacy.*

class PrivatePrivacyActivity : BaseActivity() {

    private lateinit var privatePrivacyContentTextView: TextView
    private lateinit var privatePrivacyBackImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_privacy)

        privatePrivacyContentTextView = private_privacy_content_textView
        privatePrivacyBackImageView = private_privacy_title_back_imageView

        privatePrivacyContentTextView.text = Html.fromHtml(IOUtils.toString(assets.open("private_policy.html")))
        privatePrivacyBackImageView.setOnClickListener {
            finish()
        }
    }
}