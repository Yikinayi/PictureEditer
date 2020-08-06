package yzx.app.editer.pages

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_color_picker.*
import yzx.app.editer.R
import yzx.app.editer.util.tools.Inte
import yzx.app.editer.util.tools.inverseColor
import java.math.BigInteger


class ColorPickerFragment : Fragment() {

    lateinit var onComplete: (Int) -> Unit
    var currentColor: Int = Color.WHITE
        private set

    fun setInitColor(color: Int) {
        currentColor = color
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_color_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.statusBarColor = Color.parseColor("#f5f5f5")

        panel.given(currentColor)
        setRGBAInput(currentColor)
        colorBar.given(currentColor)
        alphaBar.set(Color.alpha(currentColor) / 255f, currentColor)
        setHexText(currentColor)
        setConfirmButton(currentColor)

    }


    private fun setRGBAInput(color: Int) {
        redInput.setText(Color.red(color).toString())
        greenInput.setText(Color.green(color).toString())
        blueInput.setText(Color.blue(color).toString())
        alphaInput.setText(Color.alpha(color).toString())
    }

    private fun setHexText(color: Int) {
        hexText.text = Inte.toHex(color).toUpperCase()
    }

    private fun setConfirmButton(color: Int) {
        confirm.supportBackgroundTintList = ColorStateList.valueOf(color)
    }

}