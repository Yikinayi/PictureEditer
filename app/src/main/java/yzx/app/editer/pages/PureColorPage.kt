package yzx.app.editer.pages

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import kotlinx.android.synthetic.main.page_pure_color.*
import yzx.app.editer.R
import yzx.app.editer.pages.ability.ColorPicker
import yzx.app.editer.pages.abs.BaseEditPage
import yzx.app.editer.util.U
import yzx.app.editer.util.dp2px


class PureColorPage : BaseEditPage() {

    companion object {
        fun launch() = U.app.startActivity(Intent(U.app, PureColorPage::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_pure_color)
        makeShapeCheckbox()
        widthHeightInputLogic()
        colorLogic()
    }

    private fun colorLogic() {
        colorCircle.color = Color.WHITE
        colorCircle.setOnClickListener {
            ColorPicker.start(colorCircle.color) {
                colorCircle.color = it
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
        arrayOf(shapeRect, shapeCircle, shapeTriangle)
    }

    private fun makeShapeCheckbox() {
        shapeViews.forEach { setShapeCheckBoxBG(it, it == shapeRect) }
        shapeRect.setOnClickListener { shapeViews.forEach { setShapeCheckBoxBG(it, it == shapeRect) } }
        shapeCircle.setOnClickListener { shapeViews.forEach { setShapeCheckBoxBG(it, it == shapeCircle) } }
        shapeTriangle.setOnClickListener { shapeViews.forEach { setShapeCheckBoxBG(it, it == shapeTriangle) } }
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

}