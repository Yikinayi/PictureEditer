package yzx.app.editer.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import yzx.app.editer.util.dp2px


class ColorBar(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var colorCallback: (() -> Unit)? = null

    val currentColor: Int
        get() {
            return when (indicatorProgress) {
                in positionArray[0]..positionArray[1] ->
                    Color.rgb(255, (255f * (indicatorProgress / positionArray[1])).toInt(), 0)
                in positionArray[1]..positionArray[2] ->
                    Color.rgb((255f * (positionArray[2] - indicatorProgress) / (positionArray[2] - positionArray[1])).toInt(), 255, 0)
                in positionArray[2]..positionArray[3] ->
                    Color.rgb(0, 255, ((indicatorProgress - positionArray[2]) / (positionArray[3] - positionArray[2]) * 255f).toInt())
                in positionArray[3]..positionArray[4] ->
                    Color.rgb(0, ((1 - (indicatorProgress - positionArray[3]) / (positionArray[4] - positionArray[3])) * 255f).toInt(), 255)
                in positionArray[4]..positionArray[5] ->
                    Color.rgb(((indicatorProgress - positionArray[4]) / (positionArray[5] - positionArray[4]) * 255f).toInt(), 0, 255)
                in positionArray[5]..positionArray[6] ->
                    Color.rgb(255, 0, ((1 - (indicatorProgress - positionArray[5]) / (positionArray[6] - positionArray[5])) * 255f).toInt())
                else -> 0
            }
        }

    fun given(color: Int) {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        indicatorProgress = hsv[0] / 360f
        invalidate()
    }

    private val colorArray = IntArray(7).apply {
        set(0, Color.rgb(255, 0, 0))
        set(1, Color.rgb(255, 255, 0))
        set(2, Color.rgb(0, 255, 0))
        set(3, Color.rgb(0, 255, 255))
        set(4, Color.rgb(0, 0, 255))
        set(5, Color.rgb(255, 0, 255))
        set(6, Color.rgb(255, 0, 0))
    }

    private val positionArray = FloatArray(7).apply {
        set(0, 0f)
        set(1, 0.166f)
        set(2, 0.333f)
        set(3, 0.5f)
        set(4, 0.666f)
        set(5, 0.833f)
        set(6, 1f)
    }

    private var shader: LinearGradient? = null
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val indicatorPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val indicatorTopBottomGap = dp2px(3).toFloat()
    private val indicatorWidth = dp2px(6).toFloat()
    private val indicatorRect = RectF()
    private var indicatorProgress = 0f


    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) = Unit

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        shader = LinearGradient(
            0f, 0f, width.toFloat() - indicatorWidth, height.toFloat() - indicatorTopBottomGap * 2,
            colorArray, positionArray, Shader.TileMode.CLAMP
        )
    }


    override fun onDraw(canvas: Canvas) {
        paint.shader = shader
        paint.style = Paint.Style.FILL
        canvas.drawRect(
            indicatorWidth / 2, indicatorTopBottomGap, width.toFloat() - indicatorWidth / 2, height.toFloat() - indicatorTopBottomGap, paint
        )

        val halfIndicatorWidth = indicatorWidth / 2f
        val halfStrokeWidth = resources.displayMetrics.density / 2f

        val indicatorCenterX = halfIndicatorWidth + (width - indicatorWidth) * indicatorProgress
        indicatorRect.left = indicatorCenterX - halfIndicatorWidth
        indicatorRect.right = indicatorRect.left + indicatorWidth
        indicatorRect.top = 0f
        indicatorRect.bottom = height.toFloat()

        indicatorPaint.style = Paint.Style.FILL
        indicatorPaint.color = Color.WHITE
        canvas.drawRoundRect(indicatorRect, halfIndicatorWidth, halfIndicatorWidth, indicatorPaint)

        indicatorPaint.style = Paint.Style.STROKE
        indicatorPaint.strokeWidth = halfStrokeWidth * 2f
        indicatorRect.top += halfStrokeWidth
        indicatorRect.bottom -= halfStrokeWidth
        indicatorRect.left += halfStrokeWidth
        indicatorRect.right -= halfStrokeWidth
        indicatorPaint.color = Color.BLACK
        canvas.drawRoundRect(indicatorRect, halfIndicatorWidth, halfIndicatorWidth, indicatorPaint)

        colorCallback?.invoke()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (width <= 0) return true
        when (event.action) {
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val p = (x - indicatorWidth / 2) / (width - indicatorWidth)
                indicatorProgress = if (p > 1f) 1f else if (p < 0f) 0f else p
                invalidate()
            }
        }
        return true
    }
}