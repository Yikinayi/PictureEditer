package yzx.app.editer.pages

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_color_picker.*
import yzx.app.editer.R
import yzx.app.editer.util.tools.Inte
import yzx.app.editer.util.tools.inverseColor
import yzx.app.editer.util.tools.replaceColorAlpha
import java.math.BigInteger
import java.util.*


class ColorPickerFragment : Fragment() {

    var onComplete: ((Int) -> Unit)? = null
    var initColor: Int = Color.WHITE


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_color_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.statusBarColor = Color.parseColor("#f5f5f5")
        acceptColor(initColor)
        panel.colorCallback = { byUser ->
            if (byUser) {
                val c = replaceColorAlpha(panel.currentColor, alphaBar.currentAlpha)
                setRGBAInput(c)
                colorCircle.color = c
                setHexText(c)
                alphaBar.set(alphaBar.currentAlpha, panel.currentColor)
            }
        }
        colorBar.colorCallback = { byUser ->
            if (byUser) {
                panel.given(colorBar.currentColor, false)
                val c = replaceColorAlpha(panel.currentColor, alphaBar.currentAlpha)
                setRGBAInput(c)
                colorCircle.color = c
                alphaBar.set(alphaBar.currentAlpha, panel.currentColor)
                setHexText(c)
            }
        }
        alphaBar.alphaCallback = { byUser ->
            if (byUser) {
                val c = replaceColorAlpha(panel.currentColor, alphaBar.currentAlpha)
                setRGBAInput(c)
                setHexText(c)
                colorCircle.color = c
            }
        }
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        }
        redInput.addTextChangedListener(textWatcher)
        greenInput.addTextChangedListener(textWatcher)
        blueInput.addTextChangedListener(textWatcher)
        confirm.setOnClickListener {

        }
    }


    private fun acceptColor(color: Int) {
        panel.given(color, true)
        setRGBAInput(color)
        colorBar.given(color)
        alphaBar.set(Color.alpha(color) / 255f, color)
        setHexText(color)
        colorCircle.color = color
    }

    private fun setRGBAInput(color: Int) {
        redInput.setText(Color.red(color).toString())
        greenInput.setText(Color.green(color).toString())
        blueInput.setText(Color.blue(color).toString())
        alphaInput.setText(Color.alpha(color).toString())
    }

    @SuppressLint("SetTextI18n")
    private fun setHexText(color: Int) {
        val a = Color.alpha(color)
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        var aStr = Inte.toHex(a).toUpperCase(Locale.ROOT)
        if (aStr.length < 2) aStr = "0${aStr}"
        var rStr = Inte.toHex(r).toUpperCase(Locale.ROOT)
        if (rStr.length < 2) rStr = "0${rStr}"
        var gStr = Inte.toHex(g).toUpperCase(Locale.ROOT)
        if (gStr.length < 2) gStr = "0${gStr}"
        var bStr = Inte.toHex(b).toUpperCase(Locale.ROOT)
        if (bStr.length < 2) bStr = "0${bStr}"
        hexText.text = "${aStr}${rStr}${gStr}${bStr}"
    }

}