package blackstone.com.soolgit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import blackstone.com.soolgit.DataClass.UserData
import blackstone.com.soolgit.Util.BaseActivity
import kotlinx.android.synthetic.main.activity_sex.*

class SexActivity : BaseActivity(), View.OnClickListener {

    companion object {
        lateinit var activity : SexActivity
    }

    private var manActivated = false
    private var womanActivated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sex)
        activity = this@SexActivity

        sex_man_button.setOnClickListener(this)
        sex_woman_button.setOnClickListener(this)

        sex_confirm_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@SexActivity, AgeActivity::class.java))
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
            }
        })

        val userData = intent.extras.getSerializable("userData") as? UserData

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sex_man_button -> {
                if (womanActivated) {
                    sex_man_button.setImageResource(R.drawable.man_active)
                    sex_woman_button.setImageResource(R.drawable.woman)
                    womanActivated = !womanActivated
                }
                if (!manActivated) {
                    manActivated = !manActivated
                    sex_man_button.setImageResource(R.drawable.man_active)
                }
            }
            R.id.sex_woman_button -> {
                if (manActivated) {
                    sex_man_button.setImageResource(R.drawable.man)
                    sex_woman_button.setImageResource(R.drawable.woman_active)
                    manActivated = !manActivated
                }
                if (!womanActivated) {
                    womanActivated = !womanActivated
                    sex_woman_button.setImageResource(R.drawable.woman_active)
                }
            }
            else -> {
                Log.d("TAG", "Woman : " + womanActivated + "Man : " + manActivated)
            }
        }
    }
}