package yzx.app.editer.util.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import yzx.app.editer.R

class DialogInfo {
    lateinit var contentView: View
    var width: Int = -2
    var height: Int = -2
    var cancelAble = true
    var cancelTouchOutside = false
    var gravity = Gravity.CENTER
    var dim = true
    var dimAmount = 0.4f
}

fun Activity.showCommonDialog(info: DialogInfo): Dialog {
    AlertDialog.Builder(this, if (info.dim) R.style.DialogDimEnable else R.style.DialogDimDisable)
        .setCancelable(info.cancelAble).create().also { dialog ->
            dialog.setCancelable(info.cancelAble)
            dialog.setCanceledOnTouchOutside(info.cancelTouchOutside)
            dialog.show()
            dialog.window?.attributes?.width = info.width
            dialog.window?.attributes?.height = info.height
            dialog.window?.attributes = dialog.window?.attributes
            if (info.dim)
                dialog?.window?.attributes?.dimAmount = info.dimAmount
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setGravity(info.gravity)
            dialog.setContentView(info.contentView)
            return dialog
        }
}