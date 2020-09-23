package yzx.app.editer.pages

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import kotlinx.android.synthetic.main.page_rotation.*
import yzx.app.editer.R
import yzx.app.editer.pages.ability.ColorPicker
import yzx.app.editer.util.U
import yzx.app.editer.util.bmp.BitmapAlmighty
import yzx.app.editer.widget.toast.toast
import kotlin.math.sqrt


class RotationPage : AppCompatActivity() {

    companion object {
        fun launch(path: String) {
            U.app.startActivity(Intent(U.app, RotationPage::class.java).apply {
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
        setContentView(R.layout.page_rotation)

        back.setOnClickListener { finish() }
        bgButton.setOnClickListener {
            ColorPicker.start {
                findViewById<View>(R.id.container).setBackgroundColor(it)
                window.statusBarColor = it
            }
        }

        val len = resources.displayMetrics.widthPixels
        imageContainer.layoutParams.width = len
        imageContainer.layoutParams.height = len
        imageContainer.requestLayout()

        val scale = bitmap.width.toFloat() / bitmap.height
        val shouldWidth = sqrt((len * len) / (1 + (1 / (scale * scale))))
        val shouldHeight = shouldWidth / scale
        image.layoutParams.width = shouldWidth.toInt()
        image.layoutParams.height = shouldHeight.toInt()
        image.requestLayout()
        image.setImageBitmap(bitmap)

        ring.onDegreeChangedListener = { image.rotation = it }
    }


    override fun onDestroy() {
        super.onDestroy()
        bitmap?.recycle()
    }

}