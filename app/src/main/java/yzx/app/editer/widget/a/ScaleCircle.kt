package yzx.app.editer.widget.a

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View


class ScaleCircle(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val scale = 3
    private var bmp: Bitmap? = null
    private var xp: Float = -1f
    private var yp: Float = -1f

    fun setBitmap(bitmap: Bitmap) {
        this.bmp = bitmap
        invalidate()
    }

    fun setCenter(x: Float, y: Float) {
        this.xp = x
        this.yp = y
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (bmp == null || xp < 0 || yp < 0)
            return


    }


}