package yzx.app.editer.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import yzx.app.editer.util.dp2px
import kotlin.math.min


class ColorCircle(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var color = 0
        set(value) {
            field = value
            invalidate()
        }
    var stroke = 0
        set(value) {
            field = value
            invalidate()
        }


    private val path = Path()
    private val bgBlockColor1 = Color.rgb(166, 166, 166)
    private val bgBlockColor2 = Color.rgb(211, 211, 211)
    private val bgBlockRect = RectF()
    private val blockLen = dp2px(8).toFloat()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = dp2px(1).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        val hw = width / 2f
        val hh = height / 2f
        val r = min(hw, hh)

        canvas.save()
        path.reset()
        path.addCircle(hw, hh, r, Path.Direction.CCW)
        canvas.clipPath(path)
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
        canvas.restore()

        paint.color = color
        canvas.drawCircle(hw, hh, r, paint)

        if (stroke != 0) {
            strokePaint.color = stroke
            canvas.drawCircle(hw, hh, r - strokePaint.strokeWidth / 2f, strokePaint)
        }
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