package yzx.app.editer.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import kotlinx.android.synthetic.main.page_absorb.*
import yzx.app.editer.R
import yzx.app.editer.pages.ability.ColorPicker
import yzx.app.editer.util.U
import yzx.app.editer.util.bmp.BitmapAlmighty
import yzx.app.editer.util.dp2px
import yzx.app.editer.util.toHexColorString
import yzx.app.editer.widget.toast.toast
import kotlin.math.min


class AbsorbPage : AppCompatActivity() {

    companion object {
        fun launch(path: String) {
            U.app.startActivity(Intent(U.app, AbsorbPage::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("path", path)
            })
        }
    }

    private val path by lazy { intent.getStringExtra("path") ?: "" }
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (path.isBlank()) {
            finish(); return
        }

        savedInstanceState?.clear()
        BarUtils.setStatusBarLightMode(window, false)
        window.statusBarColor = Color.BLACK

        BitmapAlmighty.getBitmapUnderMaxSupport(path,
            complete = {
                if (isFinishing) return@getBitmapUnderMaxSupport
                this.bitmap = it; start()
            },
            error = {
                if (isFinishing) return@getBitmapUnderMaxSupport
                toast("图片有问题, 请重试")
                finish()
            })
    }

    private fun start() {
        val bitmap = bitmap!!
        setContentView(R.layout.page_absorb)
        back.setOnClickListener { finish() }
        bgButton.setOnClickListener {
            ColorPicker.start {
                findViewById<View>(R.id.container).setBackgroundColor(it)
                window.statusBarColor = it
            }
        }
        setScaleCircle(bitmap)
        setColorCircle(.5f, .5f, bitmap)
        setColorText(.5f, .5f, bitmap)
        setTouchLayout(bitmap)
    }

    private fun setScaleCircle(bitmap: Bitmap) {
        val minLen = dp2px(60 * 3)
        var realBitmap = bitmap
        if (bitmap.width + bitmap.height < minLen * 2) {
            val scale = minLen / kotlin.math.min(bitmap.width, bitmap.height).toFloat()
            realBitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale + 0.5f).toInt(), (bitmap.height * scale + 0.5f).toInt(), false)
        }
        scaleCircle.setBitmap(realBitmap)
        scaleCircle.setCenter(.5f, .5f)
    }

    @SuppressLint("SetTextI18n")
    private fun setColorCircle(xPoi: Float, yPoi: Float, bitmap: Bitmap) {
        val xp = min((bitmap.width * xPoi + 0.5f).toInt(), bitmap.width - 1)
        val yp = min((bitmap.height * yPoi + 0.5f).toInt(), bitmap.height - 1)
        colorCircle.color = bitmap.getPixel(xp, yp)
    }

    @SuppressLint("SetTextI18n")
    private fun setColorText(xPoi: Float, yPoi: Float, bitmap: Bitmap) {
        val xp = min((bitmap.width * xPoi + 0.5f).toInt(), bitmap.width - 1)
        val yp = min((bitmap.height * yPoi + 0.5f).toInt(), bitmap.height - 1)
        val p = bitmap.getPixel(xp, yp)
        colorText1.text = "当前颜色 : #${p.toHexColorString()}"
        colorText2.text = "ARGB : ${Color.alpha(p)},${Color.red(p)},${Color.green(p)},${Color.blue(p)}"
    }

    private fun setTouchLayout(bitmap: Bitmap) {
        (image.parent as View).let { parent ->
            parent.post {
                val shouldWidth: Int
                val shouldHeight: Int
                if (bitmap.width > bitmap.height) {
                    if (bitmap.height / bitmap.width.toFloat() > parent.height / parent.width.toFloat()) {
                        shouldHeight = parent.height
                        shouldWidth = bitmap.width * shouldHeight / bitmap.height
                    } else {
                        shouldWidth = resources.displayMetrics.widthPixels - dp2px(20 * 2)
                        shouldHeight = bitmap.height * shouldWidth / bitmap.width
                    }
                } else {
                    if (bitmap.width / bitmap.height.toFloat() > parent.width / parent.height.toFloat()) {
                        shouldWidth = resources.displayMetrics.widthPixels - dp2px(20 * 2)
                        shouldHeight = bitmap.height * shouldWidth / bitmap.width
                    } else {
                        shouldHeight = parent.height
                        shouldWidth = bitmap.width * shouldHeight / bitmap.height
                    }
                }
                image.setImageBitmap(bitmap)
                image.layoutParams.width = shouldWidth
                image.layoutParams.height = shouldHeight
                image.requestLayout()
                touchLine.layoutParams.width = shouldWidth
                touchLine.layoutParams.height = shouldHeight
                touchLine.requestLayout()
                touchLine.given(.5f, .5f)
                touchLine.touchPercentCallback = { xPoi, yPoi ->
                    setColorCircle(xPoi, yPoi, bitmap)
                    setColorText(xPoi, yPoi, bitmap)
                    scaleCircle.setCenter(xPoi, yPoi)
                }
            }
        }
    }

    override fun onDestroy() {
        bitmap?.recycle()
        scaleCircle?.bmp?.recycle()
        super.onDestroy()
    }

}