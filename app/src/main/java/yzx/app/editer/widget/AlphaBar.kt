package yzx.app.editer.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import yzx.app.editer.util.dp2px


class AlphaBar(context: Context?, attrs: AttributeSet?) : View(context, attrs) {


    var alphaCallback: ((byUser: Boolean) -> Unit)? = null
    val currentAlpha: Float
        get() = indicatorProgress


    private val widgetWidth: Float = (yzx.app.editer.util.U.app.resources.displayMetrics.widthPixels - dp2px(60)).toFloat()
    private val widgetHeight = dp2px(26).toFloat()
    private var shaderColor = 0

    fun set(alpha: Float, color: Int) {
        if (this.currentAlpha == alpha && this.color == color)
            return
        indicatorProgress = alpha
        this.color = color
        invalidate()
        alphaCallback?.invoke(false)
    }

    private fun makeShader() {
        if (color == 0) {
            shaderColor = 0
            shader = null
            return
        }
        if (shaderColor == color)
            return
        shader = LinearGradient(
            0f, 0f, widgetWidth - indicatorWidth, widgetHeight - indicatorTopBottomGap * 2, Color.TRANSPARENT, color,
            Shader.TileMode.CLAMP
        )
        shaderColor = color
    }

    private var color = 0
    private var shader: LinearGradient? = null
    private val paint: Paint = Paint()
    private val indicatorPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val indicatorTopBottomGap = dp2px(3).toFloat()
    private val indicatorWidth = dp2px(6).toFloat()
    private val indicatorRect = RectF()
    private var indicatorProgress = 0f


    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) = Unit


    private val bgBlockColor1 = Color.rgb(166, 166, 166)
    private val bgBlockColor2 = Color.rgb(211, 211, 211)
    private val bgBlockRect = RectF()
    private val bgBlockPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        makeShader()

        if (shader == null)
            return

        canvas.save()
        canvas.clipRect(bgBlockRect.apply {
            left = indicatorWidth / 2f
            right = widgetWidth - indicatorWidth / 2f
            top = indicatorTopBottomGap
            bottom = widgetHeight - indicatorTopBottomGap
        })
        val bgBlockLen = (widgetHeight - indicatorTopBottomGap * 2) / 2f
        val xCount = widgetWidth / bgBlockLen + 1
        repeat(xCount.toInt()) { x ->
            repeat(2) { y ->
                bgBlockPaint.color = when (y) {
                    0 -> if (x % 2 == 0) bgBlockColor1 else bgBlockColor2
                    1 -> if (x % 2 == 0) bgBlockColor2 else bgBlockColor1
                    else -> bgBlockColor2
                }
                bgBlockRect.left = x * bgBlockLen + indicatorWidth / 2f
                bgBlockRect.right = bgBlockRect.left + bgBlockLen
                bgBlockRect.top = y * bgBlockLen + indicatorTopBottomGap
                bgBlockRect.bottom = bgBlockRect.top + bgBlockLen
                canvas.drawRect(bgBlockRect, bgBlockPaint)
            }
        }
        canvas.restore()

        paint.shader = shader
        paint.style = Paint.Style.FILL
        canvas.drawRect(
            indicatorWidth / 2, indicatorTopBottomGap, width.toFloat() - indicatorWidth / 2, widgetHeight.toFloat() - indicatorTopBottomGap, paint
        )

        val halfIndicatorWidth = indicatorWidth / 2f
        val halfStrokeWidth = resources.displayMetrics.density / 2f

        val indicatorCenterX = halfIndicatorWidth + (width - indicatorWidth) * indicatorProgress
        indicatorRect.left = indicatorCenterX - halfIndicatorWidth
        indicatorRect.right = indicatorRect.left + indicatorWidth
        indicatorRect.top = 0f
        indicatorRect.bottom = widgetHeight

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
                alphaCallback?.invoke(true)
            }
        }
        return true
    }

}