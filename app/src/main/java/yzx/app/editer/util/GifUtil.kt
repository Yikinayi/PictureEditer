package com.bumptech.glide.load.resource.gif

import android.annotation.SuppressLint
import android.graphics.Bitmap
import com.bumptech.glide.gifdecoder.GifDecoder
import com.bumptech.glide.gifdecoder.StandardGifDecoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import yzx.app.editer.util.tools.reflectDeclaredField

object GifUtil {

    class CancelTag {
        @Volatile
        var running: Boolean = true
    }

    @SuppressLint("VisibleForTests")
    fun loadAllFrame(drawable: GifDrawable, cancel: CancelTag, callback: (HashMap<Int, Bitmap>) -> Unit) {
        if (drawable.isRunning) error("drawable is running")
        val decoder = (drawable.constantState as GifDrawable.GifState).frameLoader.reflectDeclaredField("gifDecoder") as StandardGifDecoder
        GlobalScope.launch {
            drawable.start()
            val frames = HashMap<Int, Bitmap>(drawable.frameCount)
            repeat@ while (true) {
                if (!cancel.running)
                    break@repeat
                if (frames.size >= drawable.frameCount) {
                    withContext(Dispatchers.Main) { callback.invoke(frames) }
                    break@repeat
                } else {
                    if (!frames.containsKey(decoder.currentFrameIndex)) {
                        val frame = decoder.nextFrame
                        if (frame != null) frames[decoder.currentFrameIndex] = frame
                    }
                }
            }
        }
    }

}

