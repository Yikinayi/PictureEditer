package yzx.app.editer.util.tools

import android.graphics.Color


fun inverseColor(c: Int): Int {
    return Color.rgb(255 - Color.red(c), 255 - Color.green(c), 255 - Color.blue(c))
}