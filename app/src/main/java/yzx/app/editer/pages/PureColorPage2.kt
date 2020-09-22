package yzx.app.editer.pages

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.BarUtils
import kotlinx.android.synthetic.main.fragment_pure_color_color.*
import kotlinx.android.synthetic.main.fragment_pure_color_preview.*
import kotlinx.android.synthetic.main.fragment_pure_color_shape.*
import kotlinx.android.synthetic.main.fragment_pure_color_wh.*
import kotlinx.android.synthetic.main.page_pure_color2.*
import yzx.app.editer.R
import yzx.app.editer.dta.PureColorShape
import yzx.app.editer.dta.Storage
import yzx.app.editer.pages.ability.ColorPicker
import yzx.app.editer.util.U
import yzx.app.editer.util.bmp.BitmapAlmighty
import yzx.app.editer.util.dialog.dismissLoading
import yzx.app.editer.util.dialog.showLoading
import yzx.app.editer.util.dp2px
import yzx.app.editer.util.toHexColorString
import yzx.app.editer.util.tools.replaceColorAlpha
import yzx.app.editer.util.tools.runMinimumInterval
import yzx.app.editer.widget.Roller
import yzx.app.editer.widget.toast.longToast
import yzx.app.editer.widget.toast.toast


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
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment = fs[position]
            override fun getCount(): Int = fs.size
        }
        fun getWidth() = whFragment.getWidth()
        fun getHeight() = whFragment.getHeight()
        fun getShape() = shapeFragment.current
        fun getColor() = colorFragment.current
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) = Unit
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
            override fun onPageSelected(position: Int) {
                if (fs[position] != previewFragment) previewFragment.hidePreview()
                else previewFragment.showPreview(getWidth(), getHeight(), getColor(), getShape())
            }
        })
        confirm.setOnClickListener {
            showLoading()
            val startTime = SystemClock.uptimeMillis()
            BitmapAlmighty.makePureColorAsync(getShape(), getColor(), getWidth(), getHeight(),
                success = { bitmap ->
                    Storage.saveAsyncWithPermission(this, bitmap,
                        success = {
                            runMinimumInterval(startTime, 1400) {
                                dismissLoading()
                                bitmap.recycle()
                                toast("OK, 图片已保存到系统相册")
                            }
                        },
                        failed = {
                            runMinimumInterval(startTime, 1200) {
                                dismissLoading()
                                bitmap.recycle()
                                longToast("操作失败, 可能是手机空间不足或没有权限")
                            }
                        })
                },
                failed = {
                    runMinimumInterval(startTime, 1200) {
                        dismissLoading()
                        toast("操作失败, 可能是内存不足")
                    }
                })
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
            val color = ResourcesCompat.getColor(resources, R.color.pure_color_page, null)
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
            setClick()
            setSelectedUIByCurrent()
        }

        var current: PureColorShape = PureColorShape.Rect
            private set

        private fun drawShape() {
            val color = ResourcesCompat.getColor(resources, R.color.pure_color_page, null)
            triangleShapeImage.onDraw = { BitmapAlmighty.drawPureColor_Triangle(it, color) }
            ovalShapeImage.onDraw = { BitmapAlmighty.drawPureColor_Oval(it, color) }
            circleShapeImage.onDraw = { BitmapAlmighty.drawPureColor_Circle(it, color) }
        }

        private fun setClick() {
            rectLayout.setOnClickListener { current = PureColorShape.Rect; setSelectedUIByCurrent() }
            triangleLayout.setOnClickListener { current = PureColorShape.Triangle; setSelectedUIByCurrent() }
            ovalLayout.setOnClickListener { current = PureColorShape.Oval; setSelectedUIByCurrent() }
            circleLayout.setOnClickListener { current = PureColorShape.Circle; setSelectedUIByCurrent() }
        }

        private fun setSelectedUIByCurrent() {
            val layouts = arrayOf(rectLayout, triangleLayout, circleLayout, ovalLayout)
            fun allNormal() = layouts.forEach { it.setBackgroundColor(Color.TRANSPARENT) }
            val selectedColor = replaceColorAlpha(ResourcesCompat.getColor(resources, R.color.pure_color_page, null), 0.1f)
            when (current) {
                PureColorShape.Rect -> {
                    allNormal()
                    rectLayout.setBackgroundColor(selectedColor)
                }
                PureColorShape.Triangle -> {
                    allNormal()
                    triangleLayout.setBackgroundColor(selectedColor)
                }
                PureColorShape.Circle -> {
                    allNormal()
                    circleLayout.setBackgroundColor(selectedColor)
                }
                PureColorShape.Oval -> {
                    allNormal()
                    ovalLayout.setBackgroundColor(selectedColor)
                }
            }
        }

    }

    class ColorFragment : Fragment() {
        private val sp = U.app.getSharedPreferences("pure_color_page", Context.MODE_PRIVATE)
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_pure_color_color, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            invalidate()
            colorCircle.setOnClickListener {
                ColorPicker.start(initColor = current) {
                    current = it
                    invalidate()
                }
            }
        }

        var current = getLastColor()
            private set

        @SuppressLint("SetTextI18n")
        private fun invalidate() {
            colorText.text = "色值 : #${current.toHexColorString()}"
            colorText2.text = "ARGB : ${Color.alpha(current)}, ${Color.red(current)}, ${Color.green(current)}, ${Color.blue(current)}, "
            colorCircle.color = current
            colorCircle.stroke = Color.parseColor("#CCCCCC")
        }

        override fun onDestroyView() {
            super.onDestroyView()
            saveColor()
        }

        private fun saveColor() = sp.edit().putInt("color_3", current).apply()
        private fun getLastColor(): Int = sp.getInt("color_3", Color.WHITE)
    }


    class PreviewFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_pure_color_preview, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            BitmapAlmighty.tintImageView(cachedIcon, Color.parseColor("#BBBBBB"))
            hidePreview()
        }

        fun showPreview(w: Int, h: Int, color: Int, shape: PureColorShape) {
            val len = imageLayout.layoutParams.width
            if (w > h) {
                alphaBackground.layoutParams.width = len
                alphaBackground.layoutParams.height = (alphaBackground.layoutParams.width / w.toFloat() * h).toInt()
                drawView.layoutParams.width = len
                drawView.layoutParams.height = (drawView.layoutParams.width / w.toFloat() * h).toInt()
                stroke.layoutParams.width = len
                stroke.layoutParams.height = (stroke.layoutParams.width / w.toFloat() * h).toInt()
            } else if (w < h) {
                alphaBackground.layoutParams.height = len
                alphaBackground.layoutParams.width = (alphaBackground.layoutParams.height / h.toFloat() * w).toInt()
                drawView.layoutParams.height = len
                drawView.layoutParams.width = (drawView.layoutParams.height / h.toFloat() * w).toInt()
                stroke.layoutParams.height = len
                stroke.layoutParams.width = (stroke.layoutParams.height / h.toFloat() * w).toInt()
            } else {
                alphaBackground.layoutParams.height = len
                alphaBackground.layoutParams.width = len
                drawView.layoutParams.height = len
                drawView.layoutParams.width = len
                stroke.layoutParams.height = len
                stroke.layoutParams.width = len
            }
            drawView.onDraw = { canvas ->
                when (shape) {
                    PureColorShape.Rect -> BitmapAlmighty.drawPureColor_Rect(canvas, color)
                    PureColorShape.Triangle -> BitmapAlmighty.drawPureColor_Triangle(canvas, color)
                    PureColorShape.Circle -> BitmapAlmighty.drawPureColor_Circle(canvas, color)
                    PureColorShape.Oval -> BitmapAlmighty.drawPureColor_Oval(canvas, color)
                }
            }
            drawView.requestLayout()
            imageLayout.isVisible = true
            showCacheLayout(w, h, color, shape)
            toCacheButton.setOnClickListener { startCache(w, h, color, shape) }
        }

        private fun startCache(w: Int, h: Int, color: Int, shape: PureColorShape) {
            activity?.showLoading()
            val startTime = SystemClock.uptimeMillis()
            BitmapAlmighty.makePureColorAsync(shape, color, w, h,
                success = { bmp ->
                    Storage.cacheAsync(bmp,
                        success = {
                            runMinimumInterval(startTime, 1400) {
                                activity?.dismissLoading()
                                bmp.recycle()
                                cachedLayout.isVisible = true
                                noCacheLayout.isVisible = false
                                cachedInfo.add("${w}${h}${color}${shape}")
                            }
                        },
                        failed = {
                            runMinimumInterval(startTime, 1200) {
                                activity?.dismissLoading()
                                bmp.recycle()
                                toast("操作失败, 可能是手机存储空间不足")
                            }
                        })
                },
                failed = {
                    runMinimumInterval(startTime, 1200) {
                        activity?.dismissLoading()
                        toast("操作失败, 估计是内存不足")
                    }
                })
        }

        private val cachedInfo = ArrayList<String>()

        private fun showCacheLayout(w: Int, h: Int, color: Int, shape: PureColorShape) {
            val key = "${w}${h}${color}${shape}"
            val cached = cachedInfo.contains(key)
            cachedLayout.isVisible = cached
            noCacheLayout.isVisible = !cachedLayout.isVisible
        }

        fun hidePreview() {
            imageLayout.isVisible = false
            cachedLayout.isVisible = false
            noCacheLayout.isVisible = false
        }
    }

}
