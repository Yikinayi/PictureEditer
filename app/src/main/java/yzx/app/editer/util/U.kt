package yzx.app.editer.util

import android.app.Application
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_color_picker.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

object U {

    lateinit var app: Application

}


fun dp2px(dp: Int): Int {
    return (U.app.resources.displayMetrics.density * dp + 0.5f).toInt()
}


fun ViewGroup.inflate(id: Int, attach: Boolean = false): View {
    return LayoutInflater.from(context).inflate(id, this, attach)
}

fun Int.toHexColorString(): String {
    val a = Color.alpha(this)
    val r = Color.red(this)
    val g = Color.green(this)
    val b = Color.blue(this)
    var aStr = Integer.toHexString(a).toUpperCase(Locale.ROOT)
    if (aStr.length < 2) aStr = "0${aStr}"
    var rStr = Integer.toHexString(r).toUpperCase(Locale.ROOT)
    if (rStr.length < 2) rStr = "0${rStr}"
    var gStr = Integer.toHexString(g).toUpperCase(Locale.ROOT)
    if (gStr.length < 2) gStr = "0${gStr}"
    var bStr = Integer.toHexString(b).toUpperCase(Locale.ROOT)
    if (bStr.length < 2) bStr = "0${bStr}"
    return "${aStr}${rStr}${gStr}${bStr}"
}


fun runOnMain(block: () -> Unit) {
    GlobalScope.launch(Dispatchers.Main) { block() }
}