package yzx.app.editer.widget.a

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import yzx.app.editer.util.dp2px
import kotlin.math.min


class ScaleCircle(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var bmp: Bitmap? = null
        private set
    private var xPoi: Float = -1f
    private var yPoi: Float = -1f

    fun setBitmap(bitmap: Bitmap) {
        this.bmp = bitmap
        invalidate()
    }

    fun setCenter(x: Float, y: Float) {
        this.xPoi = x
        this.yPoi = y
        invalidate()
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        if (bmp == null || xPoi < 0 || yPoi < 0)
            return

        canvas.translate(width / 2f, height / 2f)

        path.reset()
        path.addCircle(0f, 0f, min(width / 2f, height / 2f), Path.Direction.CCW)
        canvas.clipPath(path)

        val bmp = bmp!!
        canvas.drawBitmap(bmp, -bmp.width * xPoi, -bmp.height * yPoi, null)
    }


}