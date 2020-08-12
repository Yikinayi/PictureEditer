package yzx.app.editer.pages

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.page_pure_color.*
import yzx.app.editer.R
import yzx.app.editer.pages.abs.BaseEditPage
import yzx.app.editer.util.U


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
        colorCircle.color = Color.parseColor("#99FF0000")
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

}