package yzx.app.editer.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import yzx.app.editer.util.U
import yzx.app.editer.util.dp2px


class ColorPickPanel(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var currentColor: Int = 0

    // 写死bitmap的宽高, 与外部的控件声明宽高一直
    private val holderBitmap = Bitmap.createBitmap(
        dp2px(200), dp2px(200), Bitmap.Config.ARGB_8888
    )
    private val bmpCanvas = Canvas(holderBitmap)


    fun given(color: Int) {
        if (currentColor == color)
            return
        currentColor = color
        val hsv = FloatArray(3)
        Color.colorToHSV(currentColor, hsv)
        val hsv_h = hsv[0]
        indicatorPaint.color = inverseColor(hsv_h)
        drawBmp(hsv_h)
        invalidate()
    }

    private val rect = RectF()
    private val hsvArray = FloatArray(3)
    private val paint = Paint(Paint.DITHER_FLAG.or(Paint.ANTI_ALIAS_FLAG))

    private fun drawBmp(hsv_h: Float) {
        bmpCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        bmpCanvas.let { canvas ->
            paint.style = Paint.Style.FILL
            val width = holderBitmap.width
            val height = holderBitmap.height
            val xCount = 70f
            val yCount = 70f
            val blockWidth = width / xCount
            val blockHeight = height / yCount
            repeat(xCount.toInt()) { x ->
                repeat(yCount.toInt()) { y ->
                    rect.left = blockWidth * x
                    rect.right = rect.right + blockWidth
                    rect.top = blockHeight * y
                    rect.bottom = rect.top + blockHeight
                    val s = x.toFloat() / xCount
                    val v = 1f - y.toFloat() / yCount
                    hsvArray[0] = hsv_h
                    hsvArray[1] = s
                    hsvArray[2] = v
                    val color = Color.HSVToColor(hsvArray)
                    paint.color = color
                    canvas.drawRect(rect, paint)
                }
            }
            canvas.drawLine(0f, 0f, holderBitmap.width.toFloat(), 0f, strokePaint)
            canvas.drawLine(holderBitmap.width.toFloat(), 0f, holderBitmap.width.toFloat(), holderBitmap.height.toFloat(), strokePaint)
            canvas.drawLine(0f, 0f, 0f, holderBitmap.height.toFloat(), strokePaint)
            canvas.drawLine(0f, holderBitmap.height.toFloat(), holderBitmap.width.toFloat(), holderBitmap.height.toFloat(), strokePaint)
        }
    }

    private val strokePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = Color.parseColor("#aaaaaa")
    }
    private val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.strokeCap = Paint.Cap.ROUND
    }
    private val indicatorRadius = dp2px(2).toFloat()
    private val indicatorLineHalfLen = dp2px(6).toFloat()
    private val indicatorStrokeWidth = dp2px(1).toFloat()

    override fun onDraw(c: Canvas) {
        if (currentColor == 0) {
            c.drawColor(Color.TRANSPARENT)
        } else {
            c.drawBitmap(holderBitmap, 0f, 0f, null)
            indicatorPaint.style = Paint.Style.FILL
            c.drawCircle(selectedX, selectedY, indicatorRadius, indicatorPaint)
            indicatorPaint.style = Paint.Style.STROKE
            indicatorPaint.strokeWidth = indicatorStrokeWidth
            c.drawLine(selectedX, selectedY - indicatorLineHalfLen, selectedX, selectedY + indicatorLineHalfLen, indicatorPaint)
            c.drawLine(selectedX - indicatorLineHalfLen, selectedY, selectedX + indicatorLineHalfLen, selectedY, indicatorPaint)
        }
    }


    private var selectedX = holderBitmap.width.toFloat() / 2f
    private var selectedY = holderBitmap.height.toFloat() / 2f


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                selectedX = event.x
                if (selectedX < 0) selectedX = 0f
                else if (selectedX > holderBitmap.width) selectedX = holderBitmap.width.toFloat()
                selectedY = event.y
                if (selectedY < 0f) selectedY = 0f
                if (selectedY > holderBitmap.height) selectedY = holderBitmap.height.toFloat()
                invalidate()
            }
        }
        return true
    }

    private fun inverseColor(h: Float): Int {
        val maxColor = Color.HSVToColor(floatArrayOf(h, 1f, 1f))
        return Color.rgb(255 - Color.red(maxColor), 255 - Color.green(maxColor), 255 - Color.blue(maxColor))
    }

}