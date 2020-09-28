package yzx.app.editer.util.tools

import android.graphics.Color
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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


/**
 * @param start 开始计时时间,  需要使用SystemClock.uptimeMillis()
 * @param block 主线程回调
 */
fun runMinimumInterval(start: Long, interval: Long, block: () -> Unit) {
    val now = SystemClock.uptimeMillis()
    val realInterval = now - start
    GlobalScope.launch(Dispatchers.Main) {
        if (realInterval < interval)
            delay(interval - realInterval)
        block.invoke()
    }
}


fun Any.reflectDeclaredField(name: String): Any? {
    kotlin.runCatching {
        val f = this::class.java.getDeclaredField(name)
        f.isAccessible = true
        return f.get(this)
    }
    return null
}


fun Any.reflectSetDeclaredField(name: String, value: Any?) {
    kotlin.runCatching {
        val f = this::class.java.getDeclaredField(name)
        f.isAccessible = true
        f.set(this, value)
    }
}