package yzx.app.editer.util.dialog

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import yzx.app.editer.R
import yzx.app.editer.util.U
import java.util.*


private val map = WeakHashMap<Activity, Dialog>()


private var initialized = false

private fun init() {
    if (initialized) return
    initialized = true
    U.app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity?) = Unit
        override fun onActivityResumed(activity: Activity?) = Unit
        override fun onActivityStarted(activity: Activity?) = Unit
        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) = Unit
        override fun onActivityStopped(activity: Activity?) = Unit
        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) = Unit
        override fun onActivityDestroyed(activity: Activity?) = { map.remove(activity);Unit }.invoke()
    })
}


fun Activity.showLoading() {
    init()
    val originDialog = map[this]
    if (originDialog == null) {
        val view = LayoutInflater.from(this).inflate(R.layout.layout_loading, window.decorView as ViewGroup, false)
        val dialog = showCommonDialog(DialogInfo().apply {
            contentView = view
            cancelAble = false
            dimAmount = 0.2f
        })
        map[this] = dialog
    } else {
        if (!originDialog.isShowing)
            originDialog.show()
    }
}

fun Activity.dismissLoading() {
    kotlin.runCatching { map[this]?.dismiss() }
}