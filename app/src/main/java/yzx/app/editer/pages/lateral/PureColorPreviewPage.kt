package yzx.app.editer.pages.lateral

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import yzx.app.editer.R
import yzx.app.editer.dta.PureColorShape
import yzx.app.editer.util.U


class PureColorPreviewPage : AppCompatActivity() {

    companion object {
        fun launch(shape: PureColorShape, color: Int, w: Int, h: Int) {
            U.app.startActivity(Intent(U.app, PureColorPreviewPage::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("shape", shape)
                putExtra("color", color)
                putExtra("w", w)
                putExtra("h", h)
            })
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_pure_color_preview)

    }

}