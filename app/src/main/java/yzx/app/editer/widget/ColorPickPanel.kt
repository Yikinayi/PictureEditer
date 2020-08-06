package yzx.app.editer.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import yzx.app.editer.util.U
import yzx.app.editer.util.dp2px


class ColorPickPanel(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var currentColor: Int = 0

    // 写死bitmap的宽高, 与外部的控件声明宽高一直
    private val holderBitmap = Bitmap.createBitmap(
        U.app.resources.displayMetrics.widthPixels - dp2px(40),
        dp2px(300), Bitmap.Config.RGB_565
    )
    private val bmpCanvas = Canvas(holderBitmap)


    fun given(color: Int) {
        if (currentColor == color)
            return
        currentColor = color
        val hsv = FloatArray(3)
        Color.colorToHSV(currentColor, hsv)
        val hsv_h = hsv[0]
        drawBmp(hsv_h)
        invalidate()
    }

    private val rect = RectF()
    private val hsvArray = FloatArray(3)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private fun drawBmp(hsv_h: Float) {
        bmpCanvas.let { canvas ->
            paint.style = Paint.Style.FILL
            val width = holderBitmap.width
            val height = holderBitmap.height
            val xCount = 200f
            val yCount = 200f
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
        }
    }


    override fun onDraw(c: Canvas) {
        if (currentColor == 0) {
            c.drawColor(Color.TRANSPARENT)
        } else
            c.drawBitmap(holderBitmap, 0f, 0f, null)
    }

}