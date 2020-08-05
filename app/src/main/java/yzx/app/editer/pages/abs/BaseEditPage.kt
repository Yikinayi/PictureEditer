package yzx.app.editer.pages.abs

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import yzx.app.editer.R


open class BaseEditPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarLightMode(window, true)
        findViewById<View>(R.id.back)?.setOnClickListener { finish() }

    }

}