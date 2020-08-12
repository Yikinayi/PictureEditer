package yzx.app.editer.pages.ability

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import yzx.app.editer.pages.ColorPickerFragment
import yzx.app.editer.util.U

object ColorPicker {
    val callbacks = HashMap<String, (Int) -> Unit>()
    fun start(initColor: Int? = null, callback: (Int) -> Unit) {
        val sign = SystemClock.uptimeMillis().toString()
        callbacks[sign] = callback
        U.app.startActivity(Intent(U.app, ColorPickerActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            initColor?.apply { putExtra("color", this) }
            putExtra("sign", sign)
        })
    }
}


class ColorPickerActivity : AppCompatActivity() {

    private val initColor: Int by lazy { intent.getIntExtra("color", 0) }
    private val sign by lazy { intent.getStringExtra("sign") ?: "" }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (sign.isBlank()) {
            finish()
        }

        //
        else {

            val id = View.generateViewId()
            setContentView(FrameLayout(this).apply {
                layoutParams = ViewGroup.LayoutParams(-1, -1)
                this.id = id
            })

            val f = ColorPickerFragment()
            if (initColor != 0)
                f.initColor = initColor
            f.onComplete = { color ->
                finish()
                ColorPicker.callbacks.remove(sign)?.invoke(color)
            }
            supportFragmentManager.beginTransaction().replace(id, f).commitAllowingStateLoss()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        ColorPicker.callbacks.remove(sign)
    }

}