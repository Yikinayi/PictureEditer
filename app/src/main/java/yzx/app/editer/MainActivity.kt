package yzx.app.editer

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import yzx.app.editer.pages.MainEditFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 23)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_main)

        if (!isFinishing)
            supportFragmentManager.beginTransaction().replace(R.id.container, MainEditFragment()).commitAllowingStateLoss()

        cacheView.setOnClickListener {

        }
    }


}