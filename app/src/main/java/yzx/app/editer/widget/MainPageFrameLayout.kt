package yzx.app.editer.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper
import yzx.app.editer.R
import yzx.app.editer.util.U
import kotlin.math.min


class MainPageFrameLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {


    private val dragger: ViewDragHelper


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        findViewById<View>(R.id.cacheView)?.apply {
            val y = YStorage.get()
            layout(this.left, y, this.right, y + this.measuredHeight)
        }
    }

    init {
        dragger = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean = (child.id == R.id.cacheView)
            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int = left
            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int = top
            override fun getViewHorizontalDragRange(child: View): Int = resources.displayMetrics.widthPixels
            override fun getViewVerticalDragRange(child: View): Int = resources.displayMetrics.heightPixels
            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) = onRelease(releasedChild)
        })
    }

    private fun onRelease(child: View) {
        val minY = 0
        val maxY = resources.displayMetrics.heightPixels - child.height - child.height / 2
        val y = if (child.top < minY) minY else if (child.top > maxY) maxY else child.top
        dragger.smoothSlideViewTo(child, resources.displayMetrics.widthPixels - child.width, y)
        YStorage.save(y)
        invalidate()
    }

    override fun computeScroll() {
        super.computeScroll()
        if (dragger.continueSettling(true))
            invalidate()
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return dragger.shouldInterceptTouchEvent(ev)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragger.processTouchEvent(event)
        return true
    }


    private object YStorage {
        fun save(y: Int) =
            U.app.getSharedPreferences("main_page_frame_y", Context.MODE_PRIVATE).edit().putInt("y", y).apply()

        fun get() = U.app.getSharedPreferences("main_page_frame_y", Context.MODE_PRIVATE).getInt(
            "y",
            (U.app.resources.displayMetrics.heightPixels / 4f * 3f).toInt()
        )
    }

}