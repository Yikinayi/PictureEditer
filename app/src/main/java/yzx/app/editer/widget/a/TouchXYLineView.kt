package yzx.app.editer.widget.a

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import yzx.app.editer.util.dp2px


class TouchXYLineView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {


    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        strokeWidth = dp2px(1).toFloat() / 2f
        style = Paint.Style.STROKE
    }
    private val dimColor = Color.parseColor("#44000000")


    override fun onDraw(canvas: Canvas) {
        if (xPoi < 0 || yPoi < 0)
            return
        canvas.drawColor(dimColor)
        val halfLineWidth = linePaint.strokeWidth / 2
        val endY = height.toFloat() * yPoi - halfLineWidth
        val endX = width.toFloat() * xPoi - halfLineWidth
        canvas.drawLine(0f, endY, width.toFloat(), endY, linePaint)
        canvas.drawLine(endX, 0f, endX, height.toFloat(), linePaint)
    }

    var touchPercentCallback: ((xPoi: Float, yPoi: Float) -> Unit)? = null

    fun given(xPoi: Float, yPoi: Float) {
        this.xPoi = xPoi
        this.yPoi = yPoi
        invalidate()
    }


    private var xPoi: Float = -1f
    private var yPoi: Float = -1f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                var touchX = event.x
                var touchY = event.y
                if (touchX < 0f) touchX = 0f
                else if (touchX > width) touchX = width.toFloat()
                if (touchY < 0f) touchY = 0f
                else if (touchY > height) touchY = height.toFloat()
                this.xPoi = touchX / width
                this.yPoi = touchY / height
                invalidate()
                touchPercentCallback?.invoke(xPoi, yPoi)
            }
        }
        return true
    }

}