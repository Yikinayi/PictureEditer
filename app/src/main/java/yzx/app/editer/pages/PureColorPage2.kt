package yzx.app.editer.pages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.page_pure_color2.*
import yzx.app.editer.R
import yzx.app.editer.util.U
import yzx.app.editer.util.dp2px


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
        setContentView(R.layout.page_pure_color2)
        back.setOnClickListener { finish() }
        viewPager.offscreenPageLimit = fs.size
        viewPager.pageMargin = -dp2px(60)
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment = fs[position]
            override fun getCount(): Int = fs.size
        }
    }


    //
    //
    //


    class WidthHeightFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_pure_color_wh, container, false)
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
