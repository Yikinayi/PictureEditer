package yzx.app.editer.pages

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.getSpans
import com.blankj.utilcode.util.BarUtils
import kotlinx.android.synthetic.main.page_text_to_image.*
import yzx.app.editer.R
import yzx.app.editer.pages.ability.ColorPicker
import yzx.app.editer.pages.ability.ImageProcessCallback
import yzx.app.editer.pages.ability.startImageCacheProcess
import yzx.app.editer.pages.ability.startImageSaveProcess
import yzx.app.editer.util.U
import yzx.app.editer.util.bmp.BitmapAlmighty
import yzx.app.editer.util.dialog.IntRangeSelectAlert
import yzx.app.editer.util.dialog.SimpleConfirmAlert
import yzx.app.editer.util.dp2px
import yzx.app.editer.util.px2dp
import yzx.app.editer.util.sysResource.SystemPhotograph
import yzx.app.editer.util.tools.setOnClickListenerPreventFast
import yzx.app.editer.widget.toast.toast
import kotlin.math.max
import kotlin.math.min


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

        back.setOnClickListenerPreventFast {
            onBackPressed()
        }
        bgButton.setOnClickListenerPreventFast {
            ColorPicker.start(title = "选择背景颜色") { input.setBackgroundColor(it) }
        }
        textColorButton.setOnClickListenerPreventFast {
            ColorPicker.start(title = "选择文字颜色") { onTextColorSelected(it) }
        }
        clearButton.setOnClickListenerPreventFast {
            if (input.text.isNotEmpty()) SimpleConfirmAlert.show(this, "清空?", "手误", "确定") { input.setText("") }
        }
        textSizeButton.setOnClickListenerPreventFast {
            showSelectedSizeMenu { onTextSizeSelected(it) }
        }
        imageBgButton.setOnClickListener {
            SystemPhotograph.request(this) {
                BitmapAlmighty.getBitmapUnderMaxSupport(it,
                    { bmp -> input.background = BitmapDrawable(resources, bmp) },
                    { toast("图片有问题, 请重试") })
            }
        }

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

    private fun onTextColorSelected(c: Int) {
        val start = min(input.selectionStart, input.selectionEnd)
        val end = max(input.selectionStart, input.selectionEnd)
        if (start == end) {
            input.text.getSpans<ForegroundColorSpan>().forEach { input.text.removeSpan(it) }
            input.setTextColor(c)
        } else {
            val target = input.text.subSequence(start, end)
            val newChars = SpannableString(target)
            newChars.setSpan(ForegroundColorSpan(c), 0, target.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            input.text.replace(start, end, newChars)
        }
    }

    private fun onTextSizeSelected(size: Int) {
        val start = min(input.selectionStart, input.selectionEnd)
        val end = max(input.selectionStart, input.selectionEnd)
        if (start == end) {
            input.text.getSpans<AbsoluteSizeSpan>().forEach { input.text.removeSpan(it) }
            input.textSize = size.toFloat()
        } else {
            val target = input.text.subSequence(start, end)
            val newChars = SpannableString(target)
            newChars.setSpan(AbsoluteSizeSpan(dp2px(size)), 0, target.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            input.text.replace(start, end, newChars)
        }
    }

    private fun startSave() {
        input.isCursorVisible = false
        input.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_LOW
        input.buildDrawingCache()
        val bmp = input.drawingCache
        startImageSaveProcess(this, object : ImageProcessCallback {
            override fun getBitmap(): Bitmap? = bmp
            override fun recycleBitmap(): Boolean = false
            override fun onComplete(result: Boolean) {
                input?.destroyDrawingCache()
                input.isCursorVisible = true
            }
        })
    }

    private fun startCache() {
        SimpleConfirmAlert.showByCache(this) {
            input.isCursorVisible = false
            input.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_LOW
            input.buildDrawingCache()
            val bmp = input.drawingCache
            startImageCacheProcess(this, object : ImageProcessCallback {
                override fun getBitmap(): Bitmap? = bmp
                override fun recycleBitmap(): Boolean = false
                override fun onComplete(result: Boolean) {
                    input?.destroyDrawingCache()
                    input.isCursorVisible = true
                }
            })
        }
    }


    private fun showSelectedSizeMenu(onSelected: (Int) -> Unit) {
        val min = 8
        val max = 60
        IntRangeSelectAlert.show(this, px2dp(input.textSize.toInt()), min, max, "选择字体大小") {
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