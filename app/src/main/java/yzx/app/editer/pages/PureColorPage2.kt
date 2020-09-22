package yzx.app.editer.pages

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.blankj.utilcode.util.BarUtils
import kotlinx.android.synthetic.main.fragment_pure_color_shape.*
import kotlinx.android.synthetic.main.fragment_pure_color_wh.*
import kotlinx.android.synthetic.main.page_pure_color2.*
import yzx.app.editer.R
import yzx.app.editer.util.U
import yzx.app.editer.util.bmp.BitmapAlmighty
import yzx.app.editer.util.dp2px
import yzx.app.editer.util.tools.replaceColorAlpha
import yzx.app.editer.widget.Roller


class PureColorPage2 : AppCompatActivity() {

    companion object {
        fun launch() = U.app.startActivity(Intent(U.app, PureColorPage2::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    private val whFragment = WidthHeightFragment()
    private val shapeFragment = ShapeFragment()
    private val colorFragment = ColorFragment()
    private val previewFragment = PreviewFragment()
    private val fs = arrayOf(whFragment, shapeFragment, colorFragment, previewFragment)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.clear()
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
            setText()
            startAnim()
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
            val defaultIndex = 30
            widthRoller.set(numberList, Roller.ItemInfo().apply {
                this.givenViewHeight = height
                this.maxTextSize = textSize
                this.maxTextColor = color
                this.minTextColor = replaceColorAlpha(color, 0.3f)
                this.lineColor = 0
            }, defaultIndex)
            heightRoller.set(numberList, Roller.ItemInfo().apply {
                this.givenViewHeight = height
                this.maxTextSize = textSize
                this.maxTextColor = color
                this.minTextColor = replaceColorAlpha(color, 0.3f)
                this.lineColor = 0
            }, defaultIndex)
        }

        private fun setText() {
            val span = SpannableString(" (px)")
            span.setSpan(AbsoluteSizeSpan(dp2px(12)), 0, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            span.setSpan(ForegroundColorSpan(Color.parseColor("#AAAAAA")), 0, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            widthText.append(span)
            heightText.append(span)
        }

        private var animLeft: ValueAnimator? = null

        private fun startAnim() {
            endAnim()
            val gap = dp2px(5).toFloat()
            animLeft = ValueAnimator.ofFloat(0f, gap).setDuration(1000).apply {
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                addUpdateListener { anim ->
                    arrow1?.translationX = anim.animatedValue as Float
                    arrow3?.translationX = anim.animatedValue as Float
                }
                start()
            }
        }

        private fun endAnim() {
            animLeft?.cancel()
            animLeft = null
        }

        override fun onDestroyView() {
            endAnim()
            super.onDestroyView()
        }

        fun getWidth(): Int {
            val default = 500
            kotlin.runCatching {
                val index = widthRoller.currentSelectedIndex
                if (index < 0 || index > 180) return 500
                return index * 10 + 200
            }
            return default
        }

        fun getHeight(): Int {
            val default = 500
            kotlin.runCatching {
                val index = heightRoller.currentSelectedIndex
                if (index < 0 || index > 180) return 500
                return index * 10 + 200
            }
            return default
        }
    }

    class ShapeFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_pure_color_shape, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            drawShape()
        }

        private fun drawShape() {
            val color = ResourcesCompat.getColor(resources, R.color.pure_color_shape, null)
            triangleShapeImage.onDraw = { BitmapAlmighty.drawPureColor_Triangle(it, color) }
            ovalShapeImage.onDraw = { BitmapAlmighty.drawPureColor_Oval(it, color) }
            circleShapeImage.onDraw = { BitmapAlmighty.drawPureColor_Circle(it, color) }
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
