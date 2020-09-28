package yzx.app.editer.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View


class SimpleLoading(context: Context?, attrs: AttributeSet?) : View(context, attrs) {


    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.RED)
    }

}