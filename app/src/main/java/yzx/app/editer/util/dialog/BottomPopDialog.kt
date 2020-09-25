package yzx.app.editer.util.dialog

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.blankj.utilcode.util.BarUtils
import yzx.app.editer.util.tools.setOnClickListenerPreventFast


object BottomPopDialog {


    fun show(view: View, activity: Activity): Dialog? {
        if (activity.isFinishing)
            return null

        val container = FrameLayout(view.context).apply { layoutParams = ViewGroup.LayoutParams(-1, -1) }
        container.addView(view, FrameLayout.LayoutParams(-1, -2).apply {
            this.gravity = Gravity.BOTTOM
        })

        val dialog = activity.showCommonDialog(DialogInfo().apply {
            this.width = -1
            this.height = view.resources.displayMetrics.heightPixels - BarUtils.getStatusBarHeight()
            this.cancelAble = true
            this.contentView = container
            this.gravity = Gravity.BOTTOM
        })

        val originAlpha = view.alpha
        view.alpha = 0f
        view.isClickable = true
        view.post {
            view.translationY = view.height.toFloat()
            view.alpha = originAlpha
            view.animate().translationY(0f).setDuration(200).start()
            container.setOnClickListenerPreventFast {
                if (view.translationY == 0f)
                    kotlin.runCatching { dialog?.dismiss() }
            }
        }

        return dialog
    }

}