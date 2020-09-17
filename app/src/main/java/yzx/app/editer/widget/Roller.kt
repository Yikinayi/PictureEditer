package yzx.app.editer.widget

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs


class Roller(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    class SimpleIndicatorLine(context: Context) : View(context) {
        var itemCount = 0
            set(value) {
                check(value > 2 && value % 2 != 0)
                field = value
                invalidate()
            }
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = context.resources.displayMetrics.density
            color = Color.parseColor("#EEEEEE")
        }

        override fun onDraw(canvas: Canvas) {
            if (itemCount <= 0) return
            val itemHeight = height / itemCount
            val y1 = itemHeight * (itemCount / 2f).toInt().toFloat()
            val y2 = y1 + itemHeight
            canvas.drawLine(0f, y1, width.toFloat(), y1, paint)
            canvas.drawLine(0f, y2, width.toFloat(), y2, paint)
        }
    }

    val currentSelectedIndex: Int
        get() {
            if (itemInfo == null || list.isNullOrEmpty() || viewCenterY <= 0) return -1
            if (specifiedIndex >= 0) return specifiedIndex
            var minDistanceChild: View? = null
            var minDistance: Int = Int.MAX_VALUE
            repeat(recyclerView.childCount) {
                val child = recyclerView.getChildAt(it)
                if (child is FrameLayout) {
                    val childCenter = child.bottom - child.height / 2
                    val distance = abs(childCenter - viewCenterY)
                    if (distance < minDistance) {
                        minDistance = distance
                        minDistanceChild = child
                    }
                }
            }
            return if (minDistanceChild == null) -1 else minDistanceChild!!.tag as Int
        }


    private val recyclerView: RecyclerView = RecyclerView(context)
    private val adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun getItemCount(): Int {
            return if (list.isNullOrEmpty()) 0 else list!!.size + (itemInfo!!.showingItemCount - 1)
        }

        override fun getItemViewType(position: Int): Int {
            if (itemInfo!!.showingItemCount == 3) {
                return if (position == 0 || position == (list!!.size + 1)) 1 else 2
            } else if (itemInfo!!.showingItemCount == 5) {
                return if (position == 0 || position == 1 || position == (list!!.size + 3) || position == (list!!.size + 2)) 1 else 2
            } else {
                throw UnknownError("unknown View type")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == 1) {
                return object : RecyclerView.ViewHolder(View(parent.context).apply {
                    this.layoutParams = ViewGroup.LayoutParams(-1, perItemHeight)
                }) {}
            } else if (viewType == 2) {
                val tv = TextView(parent.context).apply {
                    this.layoutParams = ViewGroup.LayoutParams(-1, -1)
                    gravity = Gravity.CENTER
                    textSize = itemInfo!!.maxTextSize
                    setSingleLine()
                    ellipsize = android.text.TextUtils.TruncateAt.END
                }
                return object : RecyclerView.ViewHolder(FrameLayout(tv.context).apply {
                    this.layoutParams = ViewGroup.LayoutParams(-1, perItemHeight)
                    addView(tv)
                }) {}
            } else {
                throw  UnknownError("unknown view type")
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder.itemView is FrameLayout) {
                if (itemInfo!!.showingItemCount == 3) {
                    ((holder.itemView as FrameLayout).getChildAt(0) as TextView).text = list!![position - 1]
                    holder.itemView.tag = position - 1
                } else if (itemInfo!!.showingItemCount == 5) {
                    ((holder.itemView as FrameLayout).getChildAt(0) as TextView).text = list!![position - 2]
                    holder.itemView.tag = position - 2
                } else {
                    throw UnknownError("")
                }
            }
        }
    }


    init {
        addView(recyclerView, LayoutParams(-1, -1))
        addView(SimpleIndicatorLine(context), LayoutParams(-1, -1))
        recyclerView.hasFixedSize()
        recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) = makeItemViewUI()
        })
        LinearSnapHelper().attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter
    }

    private val eva = ArgbEvaluator()

    private fun makeItemViewUI() {
        if (viewCenterY <= 0) return
        val info = itemInfo ?: return
        repeat(recyclerView.childCount) { index ->
            val child = recyclerView.getChildAt(index)
            if (child is FrameLayout) {
                val tv = child.getChildAt(0) as TextView
                val childCenter = child.bottom - child.height / 2
                val progress = abs(viewCenterY - childCenter).toFloat() / viewCenterY
                tv.scaleX = textMinScale + (1 - textMinScale) * (1 - progress)
                tv.scaleY = tv.scaleX
                val color = eva.evaluate((1 - progress), info.minTextColor, info.maxTextColor)
                tv.setTextColor(color as Int)
            }
        }
    }

    private var list: List<String>? = null
    private var itemInfo: ItemInfo? = null

    class ItemInfo {
        var showingItemCount = 5
            set(value) {
                check(value == 3 || value == 5) { "showingItemCount can only be 3 or 5" }
                field = value
            }
        var givenViewHeight = 0
        var maxTextSize = 17f
        var minTextSize = 13f
        var maxTextColor = Color.parseColor("#FF444444")
        var minTextColor = Color.parseColor("#66E0E0E0")
        var lineColor = Color.parseColor("#eeeeee")
    }


    private var viewCenterY = 0
    private var textMinScale = 0f
    private var perItemHeight = 0


    fun set(list: List<String>, itemInfo: ItemInfo, selectedIndex: Int = 0) {
        check(list.isNotEmpty())
        check(selectedIndex in list.indices)

        this.list = list
        this.itemInfo = itemInfo

        viewCenterY = itemInfo.givenViewHeight / 2
        textMinScale = itemInfo.minTextSize / itemInfo.maxTextSize
        perItemHeight = itemInfo.givenViewHeight / itemInfo.showingItemCount

        (getChildAt(1) as SimpleIndicatorLine).paint.color = itemInfo.lineColor
        (getChildAt(1) as SimpleIndicatorLine).itemCount = itemInfo.showingItemCount

        recyclerView.removeCallbacks(layoutPost)
        adapter.notifyDataSetChanged()
        specifiedIndex = selectedIndex
        recyclerView.postDelayed(layoutPost, 0)
    }


    private val layoutPost = Runnable {
        if (specifiedIndex >= 0)
            recyclerView.scrollToPosition(specifiedIndex)
        specifiedIndex = -1
    }
    private var specifiedIndex = -1

}