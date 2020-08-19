package yzx.app.editer.util.bmp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import yzx.app.editer.dta.PureColorShape
import kotlin.math.min


object BitmapAlmighty {

    fun makePureColor(shape: PureColorShape, color: Int, w: Int, h: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        when (shape) {
            PureColorShape.Rect -> drawPureColor_Rect(canvas, color)
            PureColorShape.Triangle -> drawPureColor_Triangle(canvas, color)
            PureColorShape.Circle -> drawPureColor_Circle(canvas, color)
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
        path.lineTo(canvas.width / 2f, 0f)
        path.moveTo(canvas.width.toFloat(), canvas.height.toFloat())
        path.moveTo(0f, canvas.height.toFloat())
        path.moveTo(canvas.width / 2f, 0f)
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

}