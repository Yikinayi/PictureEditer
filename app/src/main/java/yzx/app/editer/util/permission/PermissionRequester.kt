package yzx.app.editer.util.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.os.SystemClock
import androidx.core.app.ActivityCompat


typealias PermissionCallback = (Boolean) -> Unit


object PermissionRequester {

    internal val callbacks = HashMap<String, PermissionCallback>()


    fun request(activity: Activity, p: String, callback: PermissionCallback) {
        if (ActivityCompat.checkSelfPermission(activity, p) == PackageManager.PERMISSION_GRANTED) {
            callback.invoke(true)
        } else {
            val signal = SystemClock.uptimeMillis().toString()
            callbacks[signal] = callback
            PermissionHolderActivity.launch(activity, signal, p)
        }
    }


}