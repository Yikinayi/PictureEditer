package yzx.app.editer.widget.toast

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.lw_layout_toast.view.*
import yzx.app.editer.R
import yzx.app.editer.util.U
import yzx.app.editer.util.runOnMain


fun toast(str: String) = runOnMain { t(str, Toast.LENGTH_SHORT) }
fun toast(res: Int) = toast(U.app.resources.getString(res))
fun longToast(str: String) = runOnMain { t(str, Toast.LENGTH_LONG) }
fun longToast(res: Int) = longToast(U.app.resources.getString(res))


private var canToast = true

fun enableToast() = run { canToast = true }
fun disableToast() = run { canToast = false }

@SuppressLint("StaticFieldLeak")
private val p = FrameLayout(U.app)
private var toast: Toast? = null

private fun t(str: String, duration: Int) {
    if (!canToast)
        return
    toast?.cancel()
    val view = LayoutInflater.from(U.app).inflate(R.layout.lw_layout_toast, p, false)
    view.text.text = str
    toast = Toast(U.app)
    toast!!.view = view
    toast!!.duration = duration
    toast!!.show()
}