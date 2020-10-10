package yzx.app.editer.pages

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_color_picker.*
import yzx.app.editer.R
import yzx.app.editer.util.toHexColorString
import yzx.app.editer.util.tools.replaceColorAlpha
import java.util.*


class ColorPickerFragment : Fragment() {

    var onComplete: ((Int) -> Unit)? = null
    var initColor: Int = Color.HSVToColor(FloatArray(3).apply {
        set(0, 180f); set(1, 0.5f); set(2, 0.5f)
    })
    var title: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_color_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.statusBarColor = Color.parseColor("#f5f5f5")
        if (title.isNotBlank())
            titleText.text = title
        acceptColor(initColor)
        panel.colorCallback = { byUser ->
            if (byUser) {
                val panelColor = panel.currentColor
                val c = replaceColorAlpha(panelColor, alphaBar.currentAlpha)
                setRGBAInput(c)
                colorCircle.color = c
                setHexText(c)
                alphaBar.set(alphaBar.currentAlpha, panelColor)
            }
        }
        colorBar.colorCallback = { byUser ->
            if (byUser) {
                panel.given(colorBar.currentColor, false)
                val panelColor = panel.currentColor
                val c = replaceColorAlpha(panelColor, alphaBar.currentAlpha)
                setRGBAInput(c)
                colorCircle.color = c
                alphaBar.set(alphaBar.currentAlpha, panelColor)
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
        confirm.setOnClickListener {
            val c = replaceColorAlpha(panel.currentColor, alphaBar.currentAlpha)
            onComplete?.invoke(c)
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = onInputChanged(this)
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    }

    private fun onInputChanged(w: TextWatcher) {
        redInput.removeTextChangedListener(w)
        greenInput.removeTextChangedListener(w)
        blueInput.removeTextChangedListener(w)
        alphaInput.removeTextChangedListener(w)
        var redStr = redInput.text.toString()
        var greenStr = greenInput.text.toString()
        var blueStr = blueInput.text.toString()
        var alphaStr = alphaInput.text.toString()
        if (redStr.isBlank()) redStr = "0"
        if (greenStr.isBlank()) greenStr = "0"
        if (blueStr.isBlank()) blueStr = "0"
        if (alphaStr.isBlank()) alphaStr = "0"
        val red = redStr.toInt().coerceAtMost(255)
        val green = greenStr.toInt().coerceAtMost(255)
        val blue = blueStr.toInt().coerceAtMost(255)
        val alpha = alphaStr.toInt().coerceAtMost(255)
        redInput.setText(red.toString())
        redInput.setSelection(redInput.text.length)
        greenInput.setText(green.toString())
        greenInput.setSelection(greenInput.text.length)
        blueInput.setText(blue.toString())
        blueInput.setSelection(blueInput.text.length)
        alphaInput.setText(alpha.toString())
        alphaInput.setSelection(alphaInput.text.length)
        val color = Color.argb(alpha, red, green, blue)
        panel.given(color, true)
        colorCircle.color = color
        colorBar.given(color)
        alphaBar.set(alpha / 255f, Color.rgb(red, green, blue))
        setHexText(color)
        redInput.addTextChangedListener(w)
        greenInput.addTextChangedListener(w)
        blueInput.addTextChangedListener(w)
        alphaInput.addTextChangedListener(w)
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
        redInput.removeTextChangedListener(textWatcher)
        greenInput.removeTextChangedListener(textWatcher)
        blueInput.removeTextChangedListener(textWatcher)
        alphaInput.removeTextChangedListener(textWatcher)
        redInput.setText(Color.red(color).toString())
        greenInput.setText(Color.green(color).toString())
        blueInput.setText(Color.blue(color).toString())
        alphaInput.setText(Color.alpha(color).toString())
        redInput.setSelection(redInput.text.length)
        greenInput.setSelection(greenInput.text.length)
        blueInput.setSelection(blueInput.text.length)
        alphaInput.setSelection(alphaInput.text.length)
        redInput.addTextChangedListener(textWatcher)
        greenInput.addTextChangedListener(textWatcher)
        blueInput.addTextChangedListener(textWatcher)
        alphaInput.addTextChangedListener(textWatcher)
    }

    private fun setHexText(color: Int) {
        hexText.text = color.toHexColorString()
    }

}