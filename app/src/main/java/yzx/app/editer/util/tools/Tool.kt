package yzx.app.editer.util.tools

import android.graphics.Color
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import yzx.app.editer.R
import yzx.app.editer.util.U


fun inverseColor(c: Int): Int {
    return Color.rgb(255 - Color.red(c), 255 - Color.green(c), 255 - Color.blue(c))
}


fun replaceColorAlpha(color: Int, alpha: Float): Int {
    return Color.argb((alpha * 255f).toInt(), Color.red(color), Color.green(color), Color.blue(color))
}

fun View.setOnClickListenerPreventFast(gap: Int = 500, block: (View) -> Unit) {
    this.setOnClickListener {
        val now = SystemClock.uptimeMillis()
        val last = this.getTag(R.id.prevent_fast_click_tag) as? Long ?: 0L
        if (now - last > gap) {
            this.setTag(R.id.prevent_fast_click_tag, now)
            block.invoke(this)
        }
    }
}