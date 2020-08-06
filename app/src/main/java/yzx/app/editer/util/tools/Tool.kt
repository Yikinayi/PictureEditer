package yzx.app.editer.util.tools

import android.graphics.Color


fun inverseColor(c: Int): Int {
    return Color.rgb(255 - Color.red(c), 255 - Color.green(c), 255 - Color.blue(c))
}


fun replaceColorAlpha(color: Int, alpha: Float): Int {
    return Color.argb((alpha * 255f).toInt(), Color.red(color), Color.green(color), Color.blue(color))
}