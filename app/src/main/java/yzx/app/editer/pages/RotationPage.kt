package yzx.app.editer.pages

import android.content.Intent
import android.graphics.Bitmap
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
        BarUtils.setStatusBarLightMode(window, true)

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
        setContentView(R.layout.page_rotation)

        back.setOnClickListener { finish() }
        bgButton.setOnClickListener {
            ColorPicker.start {
                findViewById<View>(R.id.container).setBackgroundColor(it)
                window.statusBarColor = it
            }
        }

        imageContainer.layoutParams.width = resources.displayMetrics.widthPixels
        imageContainer.layoutParams.height = resources.displayMetrics.widthPixels
        imageContainer.requestLayout()


    }


    override fun onDestroy() {
        super.onDestroy()
        bitmap?.recycle()
    }

}