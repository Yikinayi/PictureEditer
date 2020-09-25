package yzx.app.editer.pages.ability

import android.app.Activity
import android.graphics.Bitmap
import android.os.SystemClock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import yzx.app.editer.dta.Storage
import yzx.app.editer.util.dialog.dismissLoading
import yzx.app.editer.util.dialog.showLoading
import yzx.app.editer.util.tools.runMinimumInterval
import yzx.app.editer.widget.toast.longToast
import yzx.app.editer.widget.toast.toast


interface ImageProcessCallback {
    fun getBitmap(): Bitmap?
    fun onComplete(result: Boolean)
}


fun startImageCacheProcess(activity: Activity?, callback: ImageProcessCallback) {
    activity?.showLoading()
    val start = SystemClock.uptimeMillis()
    GlobalScope.launch(Dispatchers.Main) {
        val bmp = withContext(Dispatchers.Default) { callback.getBitmap() }
        if (bmp == null) {
            runMinimumInterval(start, 1200) {
                activity?.dismissLoading()
                toast("操作失败, 可能是内存不够了")
                callback.onComplete(false)
            }
        } else {
            Storage.cacheAsync(bmp,
                {
                    runMinimumInterval(start, 1300) {
                        activity?.dismissLoading()
                        bmp.recycle()
                        toast("已保存")
                        callback.onComplete(true)
                    }
                }, {
                    runMinimumInterval(start, 1200) {
                        activity?.dismissLoading()
                        bmp.recycle()
                        toast("操作失败, 可能是存储空间不足")
                        callback.onComplete(false)
                    }
                })
        }
    }
}


fun startImageSaveProcess(activity: Activity, callback: ImageProcessCallback) {
    activity.showLoading()
    val start = SystemClock.uptimeMillis()
    GlobalScope.launch(Dispatchers.Main) {
        val bmp = withContext(Dispatchers.Default) { callback.getBitmap() }
        if (bmp == null) {
            runMinimumInterval(start, 1200) {
                activity.dismissLoading()
                toast("操作失败, 可能是内存不够了")
                callback.onComplete(false)
            }
        } else {
            Storage.saveAsyncWithPermission(activity, bmp,
                {
                    runMinimumInterval(start, 1300) {
                        activity.dismissLoading()
                        bmp.recycle()
                        toast("已保存到手机相册")
                        callback.onComplete(true)
                    }
                },
                {
                    runMinimumInterval(start, 1200) {
                        activity.dismissLoading()
                        bmp.recycle()
                        longToast("操作失败, 可能是手机空间不足或没有权限")
                        callback.onComplete(false)
                    }
                })
        }
    }
}