package yzx.app.editer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import kotlinx.android.synthetic.main.activity_main.*
import yzx.app.editer.pages.MainEditFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BarUtils.setStatusBarLightMode(window, true)

        setContentView(R.layout.activity_main)

        if (!isFinishing)
            supportFragmentManager.beginTransaction().replace(R.id.container, MainEditFragment()).commitAllowingStateLoss()

        cacheView.setOnClickListener {

        }
    }

}