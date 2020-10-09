package yzx.app.editer.util.dialog

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_int_range_select.view.*
import yzx.app.editer.R
import yzx.app.editer.util.dp2px
import yzx.app.editer.util.tools.setOnClickListenerPreventFast
import yzx.app.editer.widget.Roller

object IntRangeSelectAlert {

    fun show(activity: Activity, origin: Int, start: Int, end: Int, title: String, onSelected: (Int) -> Unit) {
        check(start < end)
        check(origin in start..end)
        val contentView = LayoutInflater.from(activity).inflate(R.layout.dialog_int_range_select, activity.window.decorView as ViewGroup, false)
        contentView.title.text = title
        val dialog = BottomPopDialog.show(contentView, activity)
        dialog ?: return
        val list = ArrayList<String>(end - start + 1)
        for (i in start..end) list.add(i.toString())
        contentView.roller.set(list, Roller.ItemInfo().apply {
            this.givenViewHeight = dp2px(200)
            this.showingItemCount = 5
            this.minTextSize = 14f
            this.maxTextSize = 22f
        }, list.indexOf(origin.toString()))
        contentView.cancel.setOnClickListenerPreventFast { kotlin.runCatching { dialog.dismiss() } }
        contentView.confirm.setOnClickListenerPreventFast {
            kotlin.runCatching { dialog.dismiss() }
            onSelected.invoke(list[contentView.roller.currentSelectedIndex].toInt())
        }
    }

}