package yzx.app.editer.pages

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import yzx.app.editer.R
import yzx.app.editer.util.U


class TextToImagePage : AppCompatActivity() {

    companion object {
        fun launch() {
            U.app.startActivity(Intent(U.app, TextToImagePage::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.clear()
        BarUtils.setStatusBarLightMode(window, false)
        window.statusBarColor = Color.BLACK
        setContentView(R.layout.page_text_to_image)

        ;
        ;
        ;

    }

}