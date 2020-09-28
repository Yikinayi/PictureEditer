package com.bumptech.glide.load.resource.gif

import android.annotation.SuppressLint
import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object GifUtil {

    class CancelTag {
        @Volatile
        var running: Boolean = true
    }

    @SuppressLint("VisibleForTests")
    fun loadAllFrame(drawable: GifDrawable, cancel: CancelTag, callback: (HashMap<Int, Bitmap>) -> Unit) {
        if (drawable.isRunning) error("drawable is running")
        val state = drawable.constantState as GifDrawable.GifState
        val frames = HashMap<Int, Bitmap>(drawable.frameCount)
        GlobalScope.launch {
            drawable.start()
            repeat@ while (true) {
                if (!cancel.running)
                    break@repeat
                if (frames.size >= drawable.frameCount) {
                    withContext(Dispatchers.Main) { callback.invoke(frames) }
                    break@repeat
                } else {
                    val index = state.frameLoader.currentIndex
                    val frame = state.frameLoader.currentFrame
                    if (index < 0 || frame == null)
                        continue@repeat
                    frames[index] = frame
                }
            }
        }
    }

}

