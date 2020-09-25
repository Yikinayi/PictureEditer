package yzx.app.editer.util.dialog

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_simple_confirm.view.*
import yzx.app.editer.R
import yzx.app.editer.util.tools.setOnClickListenerPreventFast


object SimpleConfirmAlert {

    fun show(act: Activity, content: CharSequence, cancelStr: CharSequence, confirmStr: CharSequence, onConfirm: () -> Unit) {
        val contentView = LayoutInflater.from(act).inflate(R.layout.dialog_simple_confirm, act.window.decorView as ViewGroup, false)
        val contentText = contentView.content
        val confirmText = contentView.confirm
        val cancelText = contentView.cancel
        contentText.text = content
        confirmText.text = confirmStr
        cancelText.text = cancelStr
        val dialog = BottomPopDialog.show(contentView, act)
        confirmText.setOnClickListenerPreventFast {
            dialog?.dismiss()
            onConfirm.invoke()
        }
        cancelText.setOnClickListenerPreventFast {
            dialog?.dismiss()
        }
    }

}