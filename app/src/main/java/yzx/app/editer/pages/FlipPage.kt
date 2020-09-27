package yzx.app.editer.pages

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import yzx.app.editer.R
import yzx.app.editer.util.U
import yzx.app.editer.util.bmp.BitmapAlmighty
import yzx.app.editer.widget.toast.toast


class FlipPage : AppCompatActivity() {

    companion object {
        fun launch(path: String) {
            U.app.startActivity(Intent(U.app, FlipPage::class.java).apply {
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
        setContentView(R.layout.page_flip)

    }

}