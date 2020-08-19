package yzx.app.editer.dta

import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import yzx.app.editer.util.U
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object Storage {

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

}