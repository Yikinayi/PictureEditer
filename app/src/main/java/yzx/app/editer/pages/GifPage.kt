package yzx.app.editer.pages

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import yzx.app.editer.util.U


class GifPage : AppCompatActivity() {

    companion object {
        fun launch(path: String) {
            U.app.startActivity(Intent(U.app, GifPage::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("path", path)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


}