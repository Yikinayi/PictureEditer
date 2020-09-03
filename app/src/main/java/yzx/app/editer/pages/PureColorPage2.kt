package yzx.app.editer.pages

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import yzx.app.editer.R
import yzx.app.editer.util.U


class PureColorPage2 : AppCompatActivity() {

    companion object {
        fun launch() = U.app.startActivity(Intent(U.app, PureColorPage2::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_pure_color2)

    }

}