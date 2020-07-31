package yzx.app.editer.util.permission


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class PermissionHolderActivity : AppCompatActivity() {

    companion object {
        fun launch(act: Activity, sign: String, p: String) =
            act.startActivity(Intent(act, PermissionHolderActivity::class.java).apply {
                putExtra("s", sign).putExtra("p", p)
            })
    }


    private val sign: String? by lazy { intent?.getStringExtra("s") }
    private val permission: String? by lazy { intent?.getStringExtra("p") }
    private val mRequestCode = 7788


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when {
            (sign.isNullOrEmpty() || permission.isNullOrEmpty()) -> doOver(false)
            else -> ActivityCompat.requestPermissions(this, arrayOf(permission), mRequestCode)
        }
    }


    private fun doOver(result: Boolean) {
        PermissionRequester.callbacks.remove(sign)?.invoke(result)
        finish()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            mRequestCode -> doOver(ActivityCompat.checkSelfPermission(this, permission!!) == PackageManager.PERMISSION_GRANTED)
            else -> doOver(false)
        }
    }

    override fun onBackPressed() = Unit

}