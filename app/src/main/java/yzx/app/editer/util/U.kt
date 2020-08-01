package yzx.app.editer.util

import android.app.Application

object U {

    lateinit var app: Application

}


fun dp2px(dp: Int): Int {
    return (U.app.resources.displayMetrics.density * dp + 0.5f).toInt()
}