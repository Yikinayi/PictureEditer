package yzx.app.editer.util

import android.view.MotionEvent


class RevolutionGesture {


    fun handleTouchEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {

            }
        }
    }


    var onDegreeChangedListener: ((Float) -> Unit)? = null

}