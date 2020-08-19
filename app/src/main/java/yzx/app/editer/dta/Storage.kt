package yzx.app.editer.dta

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.os.Environment
import com.blankj.utilcode.util.FileUtils
import kotlinx.coroutines.*
import yzx.app.editer.util.U
import yzx.app.editer.util.permission.PermissionRequester
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

object Storage {

    val saveDir = File(Environment.getExternalStorageDirectory(), "PictureEditorImages")
    val cacheDir = File(U.app.filesDir, "bmp_cache")


    fun cache(bmp: Bitmap): Boolean {
        if (!cacheDir.exists()) {
            val dirOK = cacheDir.mkdir()
            if (!dirOK) return false
        }
        var out: OutputStream? = null
        kotlin.runCatching {
            val name = "${System.currentTimeMillis()}.bmpc"
            val targetFile = File(cacheDir, name)
            out = FileOutputStream(targetFile)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
            out!!.flush()
            out!!.close()
            return true
        }.onFailure {
            kotlin.runCatching { out?.close() }
        }
        return false
    }

    fun cacheAsync(bmp: Bitmap, success: () -> Unit, failed: () -> Unit) {
        GlobalScope.launch {
            val result = cache(bmp)
            launch(Dispatchers.Main) { if (result) success.invoke() else failed.invoke() }
        }
    }


    fun save(bmp: Bitmap): Boolean {
        var out: OutputStream? = null
        kotlin.runCatching {
            if (!saveDir.exists()) {
                val dirOK = saveDir.mkdir()
                if (!dirOK) return false
            }
            val name = "${UUID.randomUUID()}.png"
            val targetFile = File(saveDir, name)
            out = FileOutputStream(targetFile)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
            out!!.flush()
            out!!.close()
            FileUtils.notifySystemToScan(targetFile)
            return true
        }.onFailure {
            kotlin.runCatching { out?.close() }
        }
        return false
    }


    fun saveAsyncWithPermission(activity: Activity, bmp: Bitmap, success: () -> Unit, failed: () -> Unit) {
        fun startSaveOnPermissionOK() = GlobalScope.launch {
            val result = save(bmp)
            withContext(Dispatchers.Main) {
                if (result) success.invoke() else failed.invoke()
            }
        }
        PermissionRequester.request(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) { permissionOK1 ->
            if (permissionOK1) {
                PermissionRequester.request(activity, Manifest.permission.READ_EXTERNAL_STORAGE) { permissionOK2 ->
                    if (permissionOK2) startSaveOnPermissionOK() else failed.invoke()
                }
            } else failed.invoke()
        }
    }

}