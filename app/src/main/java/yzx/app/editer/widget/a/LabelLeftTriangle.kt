package yzx.app.editer.widget.a

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import yzx.app.editer.util.dp2px


class LabelLeftTriangle(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var color = 0
        set(value) {
            field = value
            invalidate()
        }
    var pointColor = 0
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }


    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        if (color == 0) return
        path.reset()
        path.moveTo(0f, height / 2f)
        path.lineTo(width.toFloat(), 0f)
        path.lineTo(width.toFloat(), height.toFloat())
        path.close()
        paint.color = color
        canvas.drawPath(path, paint)
        paint.color = pointColor
        canvas.drawCircle(dp2px(12).toFloat(), height / 2f, dp2px(2).toFloat(), paint)
    }


}