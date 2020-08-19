package yzx.app.editer.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class GeneralDrawView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var onDraw: (Canvas) -> Unit = {}


    override fun onDraw(canvas: Canvas) {
        onDraw.invoke(canvas)
    }

}