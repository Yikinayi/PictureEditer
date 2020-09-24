package yzx.app.editer.widget.a

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_rotation_ring.view.*
import yzx.app.editer.R
import yzx.app.editer.util.RevolutionGesture
import yzx.app.editer.util.dp2px
import kotlin.math.min


@SuppressLint("ClickableViewAccessibility", "SetTextI18n")
class RotationRing(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {


    fun given(d: Float) {
        (button.parent as? View)?.rotation = d
        onDegreeChangedListener?.invoke(d)
    }


    init {
        setWillNotDraw(false)
        LayoutInflater.from(context).inflate(R.layout.view_rotation_ring, this, true)
        val rotationView = button.parent as View
        val gesture = RevolutionGesture()
        gesture.onDegreeChangedListener = { d ->
            rotationView.rotation = d
            onDegreeChangedListener?.invoke(d)
        }
        rotationView.post {
            val xy = IntArray(2)
            rotationView.getLocationOnScreen(xy)
            val point = PointF(xy[0].toFloat() + rotationView.width / 2f, xy[1].toFloat() + rotationView.height / 2f)
            button.setOnTouchListener { _, event ->
                gesture.handleTouchEvent(point, event)
                true
            }
        }
    }

    var onDegreeChangedListener: ((Float) -> Unit)? = null


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