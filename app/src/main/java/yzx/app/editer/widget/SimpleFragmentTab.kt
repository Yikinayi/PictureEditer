package yzx.app.editer.widget

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


class SimpleFragmentTab(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    class Tab {
        var colorSelected = 0
        var colorNormal = 0
        var text: String = ""
        var textSizeDP = 0f

        var imageSelected = 0
        var imageNormal = 0
        var imageWidth = 0
        var imageHeight = 0

        var key = ""
        lateinit var fragment: Fragment

        var imageAndTextGap = 0
    }

    private lateinit var manager: FragmentManager
    private lateinit var tabList: List<Tab>
    private var containerID: Int = 0

    fun set(fm: FragmentManager, containerID: Int, givenHeight: Int, list: List<Tab>, showingTabKey: String) {
        removeAllViews()
        this.manager = fm
        this.tabList = list
        this.containerID = containerID
        list.forEach { tab ->
            this.addView(newTabView(givenHeight, tab, tab.key == showingTabKey) { _, _ ->
                switch(tab.key)
            })
        }
        switch(showingTabKey)
    }

    private var lateSwitchTime = 0L

    fun switch(key: String): Boolean {
        val now = SystemClock.uptimeMillis()
        val timeInterval = now - lateSwitchTime
        if (timeInterval < 250) return false
        lateSwitchTime = now
        kotlin.runCatching {
            tabList.forEach {
                if (key == it.key) {

                    val showingF = getShowingFragment()
                    val targetF = it.fragment
                    if (showingF == targetF)
                        return true
                    if (showingF == null) {
                        if (targetF.isAdded) {
                            manager.beginTransaction().show(targetF).commitAllowingStateLoss()
                        } else
                            manager.beginTransaction().add(containerID, targetF).commitAllowingStateLoss()
                    } else {
                        if (targetF.isAdded) {
                            manager.beginTransaction().hide(showingF).show(targetF).commitAllowingStateLoss()
                        } else
                            manager.beginTransaction().hide(showingF).add(containerID, targetF).commitAllowingStateLoss()
                    }

                    repeat(this.childCount) { index ->
                        val child = getChildAt(index)
                        val tab = child.tag as Tab
                        val iv = child.findViewWithTag<ImageView>("image")
                        val tv = child.findViewWithTag<TextView>("text")
                        val selected = tab.key == key
                        iv.setImageResource(if (selected) tab.imageSelected else tab.imageNormal)
                        tv.setTextColor(if (selected) tab.colorSelected else tab.colorNormal)
                    }

                    return true
                }
            }
        }
        return false
    }


    private fun getShowingFragment(): Fragment? {
        manager.fragments.forEach {
            val view = it.view
            if (view?.parent != null && view.visibility == View.VISIBLE)
                return it
        }
        return null
    }


    private fun newTabView(height: Int, tab: Tab, selected: Boolean, onClick: (ImageView, TextView) -> Unit) = LinearLayout(context).apply {
        this.layoutParams = LayoutParams(0, height, 1f)
        this.gravity = Gravity.CENTER
        this.orientation = VERTICAL
        this.tag = tab

        val imageView = ImageView(context)
        imageView.layoutParams = LayoutParams(tab.imageWidth, tab.imageHeight)
        imageView.setImageResource(if (selected) tab.imageSelected else tab.imageNormal)
        imageView.tag = "image"

        val textView = TextView(context)
        textView.setSingleLine()
        textView.textSize = tab.textSizeDP
        textView.setTextColor(if (selected) tab.colorSelected else tab.colorNormal)
        textView.setLineSpacing(0f, 0f)
        textView.text = tab.text
        textView.setPadding(0, tab.imageAndTextGap, 0, 0)
        textView.tag = "text"
        textView.layoutParams = LayoutParams(-2, -2).apply { gravity = Gravity.CENTER }

        this.addView(View(context).apply { layoutParams = LayoutParams(1, 0, 1f) })
        this.addView(imageView)
        this.addView(textView)
        this.addView(View(context).apply { layoutParams = LayoutParams(1, 0, 1f) })

        this.setOnClickListener { onClick.invoke(imageView, textView) }
    }


}