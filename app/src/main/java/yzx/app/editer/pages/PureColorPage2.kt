package yzx.app.editer.pages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.blankj.utilcode.util.BarUtils
import kotlinx.android.synthetic.main.fragment_pure_color_wh.*
import kotlinx.android.synthetic.main.page_pure_color2.*
import yzx.app.editer.R
import yzx.app.editer.util.U
import yzx.app.editer.util.dp2px
import yzx.app.editer.util.tools.replaceColorAlpha
import yzx.app.editer.widget.Roller


class PureColorPage2 : AppCompatActivity() {

    companion object {
        fun launch() = U.app.startActivity(Intent(U.app, PureColorPage2::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    private val f1 = WidthHeightFragment()
    private val f2 = ShapeFragment()
    private val f3 = ColorFragment()
    private val f4 = PreviewFragment()
    private val fs = arrayOf(f1, f2, f3, f4)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarLightMode(window, true)
        setContentView(R.layout.page_pure_color2)
        back.setOnClickListener { finish() }
        viewPager.offscreenPageLimit = fs.size
        viewPager.pageMargin = -dp2px(60)
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment = fs[position]
            override fun getCount(): Int = fs.size
        }
        confirm.setOnClickListener {

        }
    }


    //
    //
    //


    class WidthHeightFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_pure_color_wh, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            initRoller()
        }

        private fun initRoller() {
            val numberList = ArrayList<String>(181)
            repeat(181) { number ->
                val current = number * 10 + 200
                numberList.add(current.toString())
            }
            val height = dp2px(160)
            val textSize = 16f
            val color = ResourcesCompat.getColor(resources, R.color.pure_color_wh, null)
            val defaultIndex = 84
            widthRoller.set(numberList, Roller.ItemInfo().apply {
                this.givenViewHeight = height
                this.maxTextSize = textSize
                this.lineColor = replaceColorAlpha(color, 0.3f)
            }, defaultIndex)
            heightRoller.set(numberList, Roller.ItemInfo().apply {
                this.givenViewHeight = height
                this.maxTextSize = textSize
                this.lineColor = replaceColorAlpha(color, 0.3f)
            }, defaultIndex)
        }
    }

    class ShapeFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_pure_color_shape, container, false)
        }
    }

    class ColorFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_pure_color_color, container, false)
        }
    }

    class PreviewFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_pure_color_preview, container, false)
        }
    }

}
