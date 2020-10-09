package yzx.app.editer.pages

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import kotlinx.android.synthetic.main.page_text_to_image.*
import yzx.app.editer.R
import yzx.app.editer.pages.ability.ColorPicker
import yzx.app.editer.util.U
import yzx.app.editer.util.dialog.SimpleConfirmAlert
import yzx.app.editer.util.dp2px


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
        setContentView(R.layout.page_text_to_image)
        window.statusBarColor = Color.BLACK

        (input.parent as View).let { parent ->
            parent.post { input.maxHeight = parent.height }
        }

        back.setOnClickListener { onBackPressed() }
        bgButton.setOnClickListener { ColorPicker.start { input.setBackgroundColor(it) } }
        textColorButton.setOnClickListener { ColorPicker.start { input.setTextColor(it) } }
        clearButton.setOnClickListener { if (input.text.isNotEmpty()) SimpleConfirmAlert.show(this, "清空?", "手误", "确定") { input.setText("") } }

    }


    override fun onBackPressed() {
        if (input.text.isEmpty()) {
            super.onBackPressed()
        } else {
            SimpleConfirmAlert.show(this, "有内容未保存, 要退出吗?", "取消", "确定") { super.onBackPressed() }
        }
    }


}