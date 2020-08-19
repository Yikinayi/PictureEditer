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
import yzx.app.editer.util.bmp.BitmapAlmighty
import yzx.app.editer.util.dp2px
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

        val w = intent.getIntExtra("w", 0)
        val h = intent.getIntExtra("h", 0)
        val shape = intent.getSerializableExtra("shape") as? PureColorShape
        val color = intent.getIntExtra("color", 0)

        if (w <= 0 || h <= 0 || shape == null) {
            finish()
            return
        }

        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"))
        BarUtils.setStatusBarLightMode(this, true)

        setContentView(R.layout.page_pure_color_preview)

        back.setOnClickListener { finish() }

        if (w > h) {
            alphaBackground.layoutParams.width = resources.displayMetrics.widthPixels - dp2px(40)
            alphaBackground.layoutParams.height = (alphaBackground.layoutParams.width / w.toFloat() * h).toInt()
            drawView.layoutParams.width = resources.displayMetrics.widthPixels - dp2px(40)
            drawView.layoutParams.height = (drawView.layoutParams.width / w.toFloat() * h).toInt()
            stroke.layoutParams.width = resources.displayMetrics.widthPixels - dp2px(40)
            stroke.layoutParams.height = (stroke.layoutParams.width / w.toFloat() * h).toInt()
        } else if (w < h) {
            alphaBackground.layoutParams.height = resources.displayMetrics.widthPixels - dp2px(40)
            alphaBackground.layoutParams.width = (alphaBackground.layoutParams.height / h.toFloat() * w).toInt()
            drawView.layoutParams.height = resources.displayMetrics.widthPixels - dp2px(40)
            drawView.layoutParams.width = (drawView.layoutParams.height / h.toFloat() * w).toInt()
            stroke.layoutParams.height = resources.displayMetrics.widthPixels - dp2px(40)
            stroke.layoutParams.width = (stroke.layoutParams.height / h.toFloat() * w).toInt()
        }

        drawView.onDraw = { canvas ->
            when (shape) {
                PureColorShape.Rect -> BitmapAlmighty.drawPureColor_Rect(canvas, color)
                PureColorShape.Triangle -> BitmapAlmighty.drawPureColor_Triangle(canvas, color)
                PureColorShape.Circle -> BitmapAlmighty.drawPureColor_Circle(canvas, color)
            }
        }

        cacheButton.setOnClickListenerPreventFast {

        }

    }

}