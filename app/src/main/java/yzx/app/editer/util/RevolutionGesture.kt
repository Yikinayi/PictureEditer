package yzx.app.editer.util

import android.graphics.PointF
import android.view.MotionEvent
import kotlin.math.abs
import kotlin.math.atan


class RevolutionGesture {

    fun handleTouchEvent(centerPoint: PointF, event: MotionEvent) {
        val cx = centerPoint.x
        val cy = centerPoint.y
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                val nowX = event.rawX
                val nowY = event.rawY
                when {
                    //↑
                    nowX == cx && nowY < cy -> onDegreeChangedListener?.invoke(0f)
                    // →
                    nowX > cx && nowY == cy -> onDegreeChangedListener?.invoke(90f)
                    // ↓
                    nowX == cx && nowY > cy -> onDegreeChangedListener?.invoke(180f)
                    // ←
                    nowX < cx && nowY == cy -> onDegreeChangedListener?.invoke(270f)
                    // →↑
                    nowX > cx && nowY < cy -> {
                        val b1 = nowX - cx
                        val b2 = abs(nowY - cy)
                        onDegreeChangedListener?.invoke(Math.toDegrees(atan(b1 / b2).toDouble()).toFloat())
                    }
                    // →↓
                    nowX > cx && nowY > cy -> {
                        val b1 = nowY - cy
                        val b2 = nowX - cx
                        onDegreeChangedListener?.invoke(Math.toDegrees(atan(b1 / b2).toDouble()).toFloat() + 90f)
                    }
                    // ←↓
                    nowX < cx && nowY > cy -> {
                        val b1 = abs(nowX - cx)
                        val b2 = nowY - cy
                        onDegreeChangedListener?.invoke(Math.toDegrees(atan(b1 / b2).toDouble()).toFloat() + 180f)
                    }
                    // ←↑
                    nowX < cx && nowY < cy -> {
                        val b1 = abs(nowY - cy)
                        val b2 = abs(nowX - cx)
                        onDegreeChangedListener?.invoke(Math.toDegrees(atan(b1 / b2).toDouble()).toFloat() + 270f)
                    }
                }
            }
        }
    }


    var onDegreeChangedListener: ((Float) -> Unit)? = null

}