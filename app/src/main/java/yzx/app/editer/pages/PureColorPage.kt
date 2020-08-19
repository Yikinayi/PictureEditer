package yzx.app.editer.pages

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.VibrateUtils
import kotlinx.android.synthetic.main.item_main_edit_nomore.view.*
import kotlinx.android.synthetic.main.page_pure_color.*
import kotlinx.coroutines.*
import yzx.app.editer.R
import yzx.app.editer.dta.PureColorShape
import yzx.app.editer.dta.Storage
import yzx.app.editer.pages.ability.ColorPicker
import yzx.app.editer.pages.abs.BaseEditPage
import yzx.app.editer.pages.lateral.PureColorPreviewPage
import yzx.app.editer.util.U
import yzx.app.editer.util.bmp.BitmapAlmighty
import yzx.app.editer.util.dialog.dismissLoading
import yzx.app.editer.util.dialog.showLoading
import yzx.app.editer.util.dp2px
import yzx.app.editer.util.tools.toast


class PureColorPage : BaseEditPage() {

    companion object {
        fun launch() = U.app.startActivity(Intent(U.app, PureColorPage::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_pure_color)
        BarUtils.setStatusBarColor(this, Color.parseColor("#f4f4f4"))
        makeShapeCheckbox()
        widthHeightInputLogic()
        colorLogic()
        confirmLogic()
        previewLogic()
        widthInput.postDelayed({ KeyboardUtils.showSoftInput(widthInput) }, 200)
    }

    private fun colorLogic() {
        colorCircle.color = getLastColor()
        colorCircle.setOnClickListener {
            ColorPicker.start(colorCircle.color) {
                colorCircle.color = it
            }
        }
    }

    private fun confirmLogic() {
        confirm.setOnClickListener {
            val widthStr = widthInput.text.toString().trim()
            val heightStr = heightInput.text.toString().trim()
            if (widthStr.isBlank()) {
                noticeAnim(widthInput)
                return@setOnClickListener
            }
            if (heightStr.isBlank()) {
                noticeAnim(heightInput)
                return@setOnClickListener
            }
            val width = widthStr.toInt()
            val height = heightStr.toInt()
            if (width < 200 || width > 2000) {
                noticeAnim(px1)
                return@setOnClickListener
            }
            if (height < 200 || height > 2000) {
                noticeAnim(px2)
                return@setOnClickListener
            }
            showLoading()
            BitmapAlmighty.makePureColorAsync(shape, colorCircle.color, width, height,
                success = { bitmap ->
                    Storage.saveAsyncWithPermission(this, bitmap,
                        success = {
                            dismissLoading()
                            toast("图片已保存到系统相册")
                        },
                        failed = {
                            dismissLoading()
                            bitmap.recycle()
                            toast("操作失败, 请检查手机空间或权限")
                        })
                },
                failed = {
                    dismissLoading()
                    toast("操作失败, 内存不足")
                })
        }
    }

    private fun noticeAnim(view: View) {
        view.animate().cancel()
        view.scaleX = 1f
        view.scaleY = 1f
        VibrateUtils.vibrate(300)
        scope.launch {
            repeat(6) {
                if (it % 2 == 0) view.animate().scaleY(1.3f).scaleX(1.3f).setDuration(60).start()
                else view.animate().scaleY(1f).scaleX(1f).setDuration(60).start()
                delay(60)
            }
        }
    }

    private fun previewLogic() {
        preview.setOnClickListener {
            val widthStr = widthInput.text.toString().trim()
            val heightStr = heightInput.text.toString().trim()
            if (widthStr.isNotBlank() && heightStr.isNotBlank()) {
                val width = widthStr.toInt()
                val height = heightStr.toInt()
                if (width < 200 || width > 2000) {
                    noticeAnim(px1)
                    return@setOnClickListener
                }
                if (height < 200 || height > 2000) {
                    noticeAnim(px2)
                    return@setOnClickListener
                }
                PureColorPreviewPage.launch(shape, colorCircle.color, width, height)
            }
        }
    }

    private fun widthHeightInputLogic() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                val wStr = widthInput.text.toString().trim()
                val hStr = heightInput.text.toString().trim()
                controlPreviewBtn(wStr.isNotBlank() && hStr.isNotBlank())
            }
        }
        widthInput.addTextChangedListener(watcher)
        heightInput.addTextChangedListener(watcher)
        widthInput.setOnEditorActionListener { v, actionId, event ->
            heightInput.requestFocus()
            true
        }
    }


    private val shapeSelectedTag = "fuckyourmother_"
    private val shapeViews: Array<TextView> by lazy {
        arrayOf(shapeRect, shapeCircle, shapeTriangle, shapeOval)
    }

    private var shape: PureColorShape = PureColorShape.Rect


    private fun makeShapeCheckbox() {
        shapeViews.forEach { setShapeCheckBoxBG(it, it == shapeRect) }
        shapeRect.setOnClickListener { shapeViews.forEach { setShapeCheckBoxBG(it, it == shapeRect) }; shape = PureColorShape.Rect }
        shapeCircle.setOnClickListener { shapeViews.forEach { setShapeCheckBoxBG(it, it == shapeCircle) };shape = PureColorShape.Circle }
        shapeTriangle.setOnClickListener { shapeViews.forEach { setShapeCheckBoxBG(it, it == shapeTriangle) }; shape = PureColorShape.Triangle }
        shapeOval.setOnClickListener { shapeViews.forEach { setShapeCheckBoxBG(it, it == shapeOval) }; shape = PureColorShape.Oval }
    }

    private fun setShapeCheckBoxBG(view: TextView, selected: Boolean) {
        view.tag = if (selected) shapeSelectedTag else null
        view.setTextColor(if (selected) Color.parseColor("#89c3fd") else Color.parseColor("#999999"))
        view.setBackgroundResource(if (selected) R.drawable.bg_checkbox_single_selected else R.drawable.bg_checkbox_single_normal)
    }


    private var previewShown = false

    private fun controlPreviewBtn(show: Boolean) {
        if (previewShown == show)
            return
        previewShown = show
        if (show) {
            line.animate().cancel()
            line.animate().translationX(dp2px(70).toFloat()).start()
            preview.animate().cancel()
            preview.animate().translationX(dp2px(70).toFloat()).start()
        } else {
            line.animate().cancel()
            line.animate().translationX(0f).start()
            preview.animate().cancel()
            preview.animate().translationX(0f).start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onStop() {
        super.onStop()
        saveLastColor(colorCircle.color)
    }


    private fun saveLastColor(c: Int) {
        getPreferences(Context.MODE_PRIVATE).edit().putInt("c", c).apply()
    }

    private fun getLastColor(): Int {
        return getPreferences(Context.MODE_PRIVATE).getInt("c", Color.WHITE)
    }


}