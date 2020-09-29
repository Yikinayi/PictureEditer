package yzx.app.editer.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.blankj.utilcode.util.BarUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.load.resource.gif.GifUtil
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.page_gif.*
import yzx.app.editer.R
import yzx.app.editer.pages.ability.ColorPicker
import yzx.app.editer.pages.ability.ImageProcessCallback
import yzx.app.editer.pages.ability.startImageSaveProcess
import yzx.app.editer.util.U
import yzx.app.editer.util.dp2px
import yzx.app.editer.util.tools.runMinimumInterval
import yzx.app.editer.widget.toast.toast


class GifPage : AppCompatActivity() {

    companion object {
        fun launch(path: String) {
            U.app.startActivity(Intent(U.app, GifPage::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("path", path)
            })
        }
    }

    private val path by lazy { intent.getStringExtra("path") ?: "" }
    private var drawable: GifDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (path.isBlank()) {
            finish(); return
        }
        savedInstanceState?.clear()

        Glide.with(this).asGif().load(path).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(object : SimpleTarget<GifDrawable>() {
                override fun onResourceReady(resource: GifDrawable, transition: Transition<in GifDrawable>?) {
                    if (resource.frameCount <= 0) {
                        finish()
                        toast("图片有问题, 请重试")
                    } else {
                        this@GifPage.drawable = resource
                        start()
                    }
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    toast("图片有误 , 得选择GIF格式")
                    finish()
                }
            })
    }

    private val loadingCancelTag = GifUtil.CancelTag()
    private var allFrames: HashMap<Int, Bitmap>? = null

    private fun start() {
        BarUtils.setStatusBarLightMode(window, false)
        window.statusBarColor = Color.BLACK
        val drawable = drawable!!
        setContentView(R.layout.page_gif)
        loadingLayout.isVisible = true
        dataLayout.isVisible = false
        loadingImage.setImageDrawable(drawable)

        bgButton.setOnClickListener {
            ColorPicker.start {
                findViewById<View>(R.id.container).setBackgroundColor(it)
                window.statusBarColor = it
            }
        }

        val imageLen = resources.displayMetrics.widthPixels - dp2px(40)
        image.layoutParams.width = imageLen
        image.layoutParams.height = imageLen
        image.requestLayout()

        val start = SystemClock.uptimeMillis()
        GifUtil.setFrameDelay(10, drawable)
        GifUtil.loadAllFrame(drawable, loadingCancelTag) { frames ->
            allFrames = frames
            runMinimumInterval(start, 2000) {
                drawable.stop()
                loadingLayout.isVisible = false
                dataLayout.isVisible = true
                makeDataLayout(frames)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setTargetFrameUI(index: Int, frames: HashMap<Int, Bitmap>) {
        countText.text = "共包含${frames.size}帧图片"
        currentText.text = "当前第 ${index + 1} 帧"
        image.setImageBitmap(frames[index])
    }


    private fun makeDataLayout(frames: HashMap<Int, Bitmap>) {
        var currentIndex = 0
        setTargetFrameUI(currentIndex, frames)
        previous.setOnClickListener {
            currentIndex--
            if (currentIndex < 0) currentIndex = frames.size - 1
            setTargetFrameUI(currentIndex, frames)
        }
        next.setOnClickListener {
            currentIndex++
            if (currentIndex >= frames.size) currentIndex = 0
            setTargetFrameUI(currentIndex, frames)
        }
        confirm.setOnClickListener {
            startImageSaveProcess(this, object : ImageProcessCallback {
                override fun getBitmap(): Bitmap? = frames[currentIndex]
                override fun onComplete(result: Boolean) = Unit
                override fun recycleBitmap(): Boolean = false
            })
        }
    }


    override fun onDestroy() {
        loadingCancelTag.running = false
        drawable?.stop()
        allFrames?.values?.forEach { it.recycle() }
        allFrames?.clear()
        super.onDestroy()
    }


}