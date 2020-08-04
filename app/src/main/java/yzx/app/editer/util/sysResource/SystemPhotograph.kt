package yzx.app.editer.util.sysResource

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.UriUtils


object SystemPhotograph {
    val results = HashMap<String, (String) -> Unit>()
    fun request(context: Context, result: (String) -> Unit) {
        val sign = SystemClock.uptimeMillis().toString()
        context.startActivity(Intent(context, SystemPhotographHolderActivity::class.java).apply { putExtra("sign", sign) })
        results[sign] = result
    }
}


class SystemPhotographHolderActivity : AppCompatActivity() {

    private val sign: String
        get() = intent.getStringExtra("sign") ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kotlin.runCatching {
            requestSystemPhotograph()
        }.onFailure {
            complete(null)
        }
    }


    private fun complete(path: String?) {
        SystemPhotograph.results.remove(sign)?.invoke(path ?: "")
        finish()
    }

    override fun onBackPressed() = Unit

    override fun onDestroy() {
        super.onDestroy()
        kotlin.runCatching { SystemPhotograph.results.remove(sign) }
    }


    private val requestCode = 9988

    private fun requestSystemPhotograph() {
        val chooser = Intent(Intent.ACTION_CHOOSER)
        chooser.putExtra(Intent.EXTRA_TITLE, "请选择图片")
        chooser.putExtra(Intent.EXTRA_INTENT, Intent(Intent.ACTION_PICK, null).apply {
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        })
        startActivityForResult(chooser, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (this.requestCode == requestCode) {
            val uri = data?.data
            var result: String? = null
            kotlin.runCatching {
                result = if (uri == null) null else UriUtils.uri2File(uri).absolutePath
            }.onFailure {
                result = null
            }
            complete(result)
        }
    }


}