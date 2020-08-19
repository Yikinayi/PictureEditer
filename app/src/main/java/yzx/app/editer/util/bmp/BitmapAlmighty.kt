package yzx.app.editer.util.bmp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import yzx.app.editer.dta.PureColorShape
import kotlin.math.min


object BitmapAlmighty {

    fun makePureColorAsync(shape: PureColorShape, color: Int, w: Int, h: Int, success: (Bitmap) -> Unit, failed: () -> Unit) {
        GlobalScope.launch {
            val bmp = withContext(Dispatchers.Default) {
                var bmp: Bitmap? = null
                kotlin.runCatching { bmp = makePureColor(shape, color, w, h) }
                bmp
            }
            withContext(Dispatchers.Main) {
                if (bmp != null) success.invoke(bmp) else failed.invoke()
            }
        }
    }

    fun makePureColor(shape: PureColorShape, color: Int, w: Int, h: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        when (shape) {
            PureColorShape.Rect -> drawPureColor_Rect(canvas, color)
            PureColorShape.Triangle -> drawPureColor_Triangle(canvas, color)
            PureColorShape.Circle -> drawPureColor_Circle(canvas, color)
            PureColorShape.Oval -> drawPureColor_Oval(canvas, color)
        }
        return bitmap
    }

    fun drawPureColor_Rect(canvas: Canvas, color: Int) {
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.style = Paint.Style.FILL
        p.color = color
        canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), p)
    }

    fun drawPureColor_Triangle(canvas: Canvas, color: Int) {
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.style = Paint.Style.FILL
        p.color = color
        val path = Path()
        path.moveTo(canvas.width / 2f, 0f)
        path.lineTo(canvas.width.toFloat(), canvas.height.toFloat())
        path.lineTo(0f, canvas.height.toFloat())
        path.lineTo(canvas.width / 2f, 0f)
        path.close()
        canvas.drawPath(path, p)
    }

    fun drawPureColor_Circle(canvas: Canvas, color: Int) {
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.style = Paint.Style.FILL
        p.color = color
        val r = min(canvas.width, canvas.height) / 2f
        canvas.drawCircle(canvas.width / 2f, canvas.height / 2f, r, p)
    }

    fun drawPureColor_Oval(canvas: Canvas, color: Int) {
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.style = Paint.Style.FILL
        p.color = color
        canvas.drawOval(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), p)
    }

}