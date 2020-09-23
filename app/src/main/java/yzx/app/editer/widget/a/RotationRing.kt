package yzx.app.editer.widget.a

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import yzx.app.editer.util.U
import yzx.app.editer.util.dp2px
import kotlin.math.min


class RotationRing(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {


    init {
        setWillNotDraw(false)
    }


    private val buttonRadius = dp2px(10).toFloat()
    private val ringLen = dp2px(6).toFloat()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = ringLen
        color = Color.WHITE
    }


    override fun onDraw(canvas: Canvas) {
        val w2 = width / 2f
        val h2 = height / 2f
        canvas.drawCircle(w2, h2, min(w2, h2) - buttonRadius, paint)
    }

}