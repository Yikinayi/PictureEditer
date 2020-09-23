package yzx.app.editer.widget.a

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.view_rotation_ring.view.*
import yzx.app.editer.R
import yzx.app.editer.util.U
import yzx.app.editer.util.dp2px
import kotlin.math.min


class RotationRing(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {


    init {
        setWillNotDraw(false)
        LayoutInflater.from(context).inflate(R.layout.view_rotation_ring, this, true)
        degree.text = "0°"
    }


    private val buttonRadius = dp2px(10).toFloat()
    private val ringLen = dp2px(4).toFloat()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = ringLen
        color = Color.WHITE
    }
    private val paint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = dp2px(1).toFloat()
        color = Color.WHITE
    }


    override fun onDraw(canvas: Canvas) {
        val w2 = width / 2f
        val h2 = height / 2f
        canvas.drawCircle(w2, h2, min(w2, h2) - buttonRadius, paint)

        canvas.save()
        canvas.translate(w2, h2)
        val toY = -h2 + buttonRadius + ringLen / 2f + dp2px(4)
        repeat(36) {
            canvas.drawLine(0f, -h2 + buttonRadius, 0f, toY, paint2)
            canvas.rotate(10f)
        }
        canvas.restore()
    }

}