package yzx.app.editer.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import yzx.app.editer.util.dp2px
import kotlin.math.min


class ColorRect(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var color = 0
        set(value) {
            field = value
            invalidate()
        }


    private val bgBlockColor1 = Color.rgb(166, 166, 166)
    private val bgBlockColor2 = Color.rgb(211, 211, 211)
    private val bgBlockRect = RectF()
    private val blockLen = dp2px(10).toFloat()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        val xCount = width / blockLen + 1
        val yCount = height / blockLen + 1
        repeat(xCount.toInt()) { x ->
            repeat(yCount.toInt()) { y ->
                bgPaint.color = getBgPaintColor(x, y)
                bgBlockRect.left = x * blockLen
                bgBlockRect.right = bgBlockRect.left + blockLen
                bgBlockRect.top = y * blockLen
                bgBlockRect.bottom = bgBlockRect.top + blockLen
                canvas.drawRect(bgBlockRect, bgPaint)
            }
        }
        paint.color = color
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }


    private fun getBgPaintColor(x: Int, y: Int): Int {
        return if (x % 2 == 0) {
            if (y % 2 == 0) {
                bgBlockColor1
            } else {
                bgBlockColor2
            }
        } else {
            if (y % 2 == 0) {
                bgBlockColor2
            } else {
                bgBlockColor1
            }
        }
    }

}