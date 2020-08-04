package yzx.app.editer.util

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

object U {

    lateinit var app: Application

}


fun dp2px(dp: Int): Int {
    return (U.app.resources.displayMetrics.density * dp + 0.5f).toInt()
}


fun ViewGroup.inflate(id: Int, attach: Boolean = false): View {
    return LayoutInflater.from(context).inflate(id, this, attach)
}