package yzx.app.editer.pages.lateral

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.blankj.utilcode.util.BarUtils
import kotlinx.android.synthetic.main.page_pure_color_preview.*
import yzx.app.editer.R
import yzx.app.editer.dta.PureColorShape
import yzx.app.editer.util.U
import yzx.app.editer.util.dialog.showLoading
import yzx.app.editer.util.tools.setOnClickListenerPreventFast


class PureColorPreviewPage : AppCompatActivity() {

    companion object {
        fun launch(shape: PureColorShape, color: Int, w: Int, h: Int) {
            U.app.startActivity(Intent(U.app, PureColorPreviewPage::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("shape", shape)
                putExtra("color", color)
                putExtra("w", w)
                putExtra("h", h)
            })
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"))
        BarUtils.setStatusBarLightMode(this, true)
        setContentView(R.layout.page_pure_color_preview)
        back.setOnClickListener { finish() }

        val min = 100
        val w = intent.getIntExtra("w", 0)
        val h = intent.getIntExtra("h", 0)
        val shape = intent.getSerializableExtra("shape") as? PureColorShape
        val color = intent.getIntExtra("color", 0)

        noticeLayout.isVisible = (w < min || h < min)
        cacheButton.setOnClickListenerPreventFast {
            if (w > 0 && h > 0 && shape != null) {

            }
        }

    }

}