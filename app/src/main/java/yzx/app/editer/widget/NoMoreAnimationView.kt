package yzx.app.editer.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.layout_nomore_animation.view.*
import yzx.app.editer.R
import yzx.app.editer.util.dp2px
import yzx.app.editer.util.inflate


class NoMoreAnimationView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    init {
        this.inflate(R.layout.layout_nomore_animation, true)
    }


    @Volatile
    private var isRunning = false
    private var anim: ValueAnimator? = null
    private var treeAnim: ValueAnimator? = null

    fun start() {
        stop()
        isRunning = true
        anim = ValueAnimator.ofFloat(0f, -dp2px(6).toFloat()).setDuration(600).apply {
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener { plane.translationY = it.animatedValue as Float }
            start()
        }
        val roadWidth = dp2px(200)
        treeLayout.scrollTo(-roadWidth, 0)
        treeAnim = ValueAnimator.ofInt(-roadWidth, roadWidth * 2).setDuration(9000).apply {
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            addUpdateListener { treeLayout.scrollTo(it.animatedValue as Int, 0) }
            start()
        }
    }


    fun stop() {
        isRunning = false
        anim?.removeAllListeners()
        anim?.removeAllUpdateListeners()
        anim?.cancel()
        anim = null
        treeAnim?.removeAllListeners()
        treeAnim?.removeAllUpdateListeners()
        treeAnim?.cancel()
        treeAnim = null
    }

}