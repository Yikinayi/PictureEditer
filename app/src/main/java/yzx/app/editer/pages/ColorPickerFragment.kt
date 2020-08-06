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
import yzx.app.editer.util.tools.inverseColor


class ColorPickerFragment : Fragment() {

    lateinit var onComplete: (Int) -> Unit
    var initColor: Int? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_color_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.statusBarColor = Color.parseColor("#f5f5f5")

        panel.given(Color.YELLOW)
        colorBar.given(Color.YELLOW)
        alphaBar.set(0.5f, Color.YELLOW)


        hexText.text = "FFAABBCC"

    }

}