package yzx.app.editer.pages

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import kotlinx.android.synthetic.main.page_text_to_image.*
import yzx.app.editer.R
import yzx.app.editer.pages.ability.ColorPicker
import yzx.app.editer.pages.ability.ImageProcessCallback
import yzx.app.editer.pages.ability.startImageCacheProcess
import yzx.app.editer.pages.ability.startImageSaveProcess
import yzx.app.editer.util.U
import yzx.app.editer.util.dialog.IntRangeSelectAlert
import yzx.app.editer.util.dialog.SimpleConfirmAlert
import yzx.app.editer.util.dp2px
import yzx.app.editer.util.tools.setOnClickListenerPreventFast
import yzx.app.editer.widget.toast.toast


class TextToImagePage : AppCompatActivity() {

    companion object {
        fun launch() {
            U.app.startActivity(Intent(U.app, TextToImagePage::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.clear()
        BarUtils.setStatusBarLightMode(window, false)
        setContentView(R.layout.page_text_to_image)
        window.statusBarColor = Color.BLACK

        (input.parent as View).let { parent ->
            parent.post { input.maxHeight = parent.height }
        }

        back.setOnClickListenerPreventFast { onBackPressed() }
        bgButton.setOnClickListenerPreventFast { ColorPicker.start { input.setBackgroundColor(it) } }
        textColorButton.setOnClickListenerPreventFast { ColorPicker.start { input.setTextColor(it) } }
        clearButton.setOnClickListenerPreventFast { if (input.text.isNotEmpty()) SimpleConfirmAlert.show(this, "清空?", "手误", "确定") { input.setText("") } }
        textSizeButton.setOnClickListenerPreventFast { showSelectedSizeMenu { input.textSize = it.toFloat() } }
        saveButton.setOnClickListenerPreventFast {
            if (input.text.isEmpty()) {
                toast("先输入内容再操作")
                return@setOnClickListenerPreventFast
            }
            startSave()
        }
        cacheButton.setOnClickListenerPreventFast {
            if (input.text.isEmpty()) {
                toast("先输入内容再操作")
                return@setOnClickListenerPreventFast
            }
            startCache()
        }
    }

    private fun startSave() {
        input.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_LOW
        input.buildDrawingCache()
        val bmp = input.drawingCache
        startImageSaveProcess(this, object : ImageProcessCallback {
            override fun getBitmap(): Bitmap? = bmp
            override fun recycleBitmap(): Boolean = false
            override fun onComplete(result: Boolean) {
                input?.destroyDrawingCache()
            }
        })
    }

    private fun startCache() {
        SimpleConfirmAlert.showByCache(this) {
            input.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_LOW
            input.buildDrawingCache()
            val bmp = input.drawingCache
            startImageCacheProcess(this, object : ImageProcessCallback {
                override fun getBitmap(): Bitmap? = bmp
                override fun recycleBitmap(): Boolean = false
                override fun onComplete(result: Boolean) {
                    input?.destroyDrawingCache()
                }
            })
        }
    }


    private var nowTextSize = 15

    private fun showSelectedSizeMenu(onSelected: (Int) -> Unit) {
        val min = 8
        val max = 60
        IntRangeSelectAlert.show(this, nowTextSize, min, max, "选择字体大小") {
            nowTextSize = it
            onSelected.invoke(it)
        }
    }

    override fun onBackPressed() {
        if (input.text.isEmpty()) {
            super.onBackPressed()
        } else {
            SimpleConfirmAlert.show(this, "有内容未保存, 要退出吗?", "取消", "确定") { super.onBackPressed() }
        }
    }


}