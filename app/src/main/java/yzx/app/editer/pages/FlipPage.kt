package yzx.app.editer.pages

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import kotlinx.android.synthetic.main.page_flip.*
import kotlinx.android.synthetic.main.page_flip.confirm
import yzx.app.editer.R
import yzx.app.editer.pages.ability.ColorPicker
import yzx.app.editer.pages.ability.ImageProcessCallback
import yzx.app.editer.pages.ability.startImageCacheProcess
import yzx.app.editer.pages.ability.startImageSaveProcess
import yzx.app.editer.util.U
import yzx.app.editer.util.bmp.BitmapAlmighty
import yzx.app.editer.util.dialog.SimpleConfirmAlert
import yzx.app.editer.util.dp2px
import yzx.app.editer.util.tools.setOnClickListenerPreventFast
import yzx.app.editer.widget.toast.toast


class FlipPage : AppCompatActivity() {

    companion object {
        fun launch(path: String) {
            U.app.startActivity(Intent(U.app, FlipPage::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("path", path)
            })
        }
    }

    private val path by lazy { intent.getStringExtra("path") ?: "" }
    private var bitmap: Bitmap? = null
    private val flipNone = 0
    private val flipUp = 1
    private val flipLeft = 2
    private var currentFlip = flipNone


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (path.isBlank()) {
            finish(); return
        }

        savedInstanceState?.clear()
        BarUtils.setStatusBarLightMode(window, false)

        BitmapAlmighty.getBitmapUnderMaxSupport(path,
            complete = {
                if (isFinishing) return@getBitmapUnderMaxSupport
                this.bitmap = it; start()
            },
            error = {
                if (isFinishing) return@getBitmapUnderMaxSupport
                toast("图片有问题, 请重试")
                finish()
            })
    }

    private fun start() {
        val bitmap = bitmap!!
        setContentView(R.layout.page_flip)
        window.statusBarColor = Color.parseColor(container.tag.toString())
        back.setOnClickListener { finish() }
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
        image.setImageBitmap(bitmap)
        image.post {
            image.pivotX = image.width / 2f
            image.pivotY = image.height / 2f
        }

        notifyFlipSelectedUI()
        flip1Layout.setOnClickListenerPreventFast { currentFlip = flipNone; notifyFlipSelectedUI(); flipNone() }
        flip2Layout.setOnClickListenerPreventFast { currentFlip = flipLeft; notifyFlipSelectedUI(); flipLeft() }
        flip3Layout.setOnClickListenerPreventFast { currentFlip = flipUp; notifyFlipSelectedUI(); flipUp() }

        cacheButton.setOnClickListenerPreventFast {
            if (currentFlip != flipNone) {
                SimpleConfirmAlert.showByCache(this) {
                    startImageCacheProcess(this, object : ImageProcessCallback {
                        override fun onComplete(result: Boolean) = Unit
                        override fun getBitmap(): Bitmap? = when (currentFlip) {
                            flipUp -> BitmapAlmighty.makeFlipTopBottomBitmap(bitmap)
                            flipLeft -> BitmapAlmighty.makeFlipLeftRightBitmap(bitmap)
                            else -> null
                        }
                    })
                }
            } else {
                toast("图片没改动")
            }
        }
        confirm.setOnClickListenerPreventFast {
            if (currentFlip != flipNone) {
                startImageSaveProcess(this, object : ImageProcessCallback {
                    override fun onComplete(result: Boolean) = Unit
                    override fun getBitmap(): Bitmap? = when (currentFlip) {
                        flipUp -> BitmapAlmighty.makeFlipTopBottomBitmap(bitmap)
                        flipLeft -> BitmapAlmighty.makeFlipLeftRightBitmap(bitmap)
                        else -> null
                    }
                })
            } else {
                toast("图片没改动")
            }
        }
    }

    private var animX: ValueAnimator? = null
    private var animY: ValueAnimator? = null

    private fun cancelAnim() {
        animX?.removeAllListeners()
        animY?.removeAllListeners()
        animX?.removeAllUpdateListeners()
        animY?.removeAllUpdateListeners()
        animX?.cancel()
        animY?.cancel()
    }

    private fun rotationY(to: Float, end: () -> Unit) {
        animY = ValueAnimator.ofFloat(image.rotationY, to).apply {
            duration - 500
            addUpdateListener { image.rotationY = it.animatedValue as Float }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) = end.invoke()
            })
            start()
        }
    }

    private fun rotationX(to: Float, end: () -> Unit) {
        animX = ValueAnimator.ofFloat(image.rotationX, to).apply {
            duration - 500
            addUpdateListener { image.rotationX = it.animatedValue as Float }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) = end.invoke()
            })
            start()
        }
    }

    private fun flipUp() {
        cancelAnim()
        rotationY(0f) {}
        rotationX(180f) {}
    }

    private fun flipLeft() {
        cancelAnim()
        rotationX(0f) {}
        rotationY(180f) {}
    }

    private fun flipNone() {
        cancelAnim()
        rotationX(0f) {}
        rotationY(0f) {}
    }

    private fun notifyFlipSelectedUI() {
        val normalBG = Color.parseColor("#11ffffff")
        val selectedBG = Color.parseColor("#66ffffff")
        flip1Layout.setBackgroundColor(normalBG)
        flip2Layout.setBackgroundColor(normalBG)
        flip3Layout.setBackgroundColor(normalBG)
        when (currentFlip) {
            flipNone -> flip1Layout.setBackgroundColor(selectedBG)
            flipLeft -> flip2Layout.setBackgroundColor(selectedBG)
            flipUp -> flip3Layout.setBackgroundColor(selectedBG)
        }
    }

    override fun onDestroy() {
        cancelAnim()
        super.onDestroy()
    }


}