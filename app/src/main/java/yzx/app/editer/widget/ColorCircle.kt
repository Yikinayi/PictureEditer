package yzx.app.editer.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min


class ColorCircle(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var color = 0
        set(value) {
            field = value
            invalidate()
        }


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        canvas.translate(width / 2f, height / 2f)
        paint.color = color
        canvas.drawCircle(0f, 0f, min(width / 2f, height / 2f), paint)
    }

}