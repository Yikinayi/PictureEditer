package yzx.app.editer.util.bmp

import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.BaseTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import yzx.app.editer.dta.AppConfig
import yzx.app.editer.dta.PureColorShape
import yzx.app.editer.util.U
import yzx.app.editer.util.tools.replaceColorAlpha
import kotlin.math.*


object BitmapAlmighty {

    fun makePureColorAsync(shape: PureColorShape, color: Int, w: Int, h: Int, success: (Bitmap) -> Unit, failed: () -> Unit) {
        GlobalScope.launch {
            val bmp = withContext(Dispatchers.Default) {
                var bmp: Bitmap? = null
                kotlin.runCatching { bmp = makePureColor(shape, color, w, h) }
                bmp
            }
            withContext(Dispatchers.Main) {
                if (bmp != null) success.invoke(bmp) else failed.invoke()
            }
        }
    }

    fun makePureColor(shape: PureColorShape, color: Int, w: Int, h: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        when (shape) {
            PureColorShape.Rect -> drawPureColor_Rect(canvas, color)
            PureColorShape.Triangle -> drawPureColor_Triangle(canvas, color)
            PureColorShape.Circle -> drawPureColor_Circle(canvas, color)
            PureColorShape.Oval -> drawPureColor_Oval(canvas, color)
        }
        return bitmap
    }

    fun drawPureColor_Rect(canvas: Canvas, color: Int) {
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.style = Paint.Style.FILL
        p.color = color
        canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), p)
    }

    fun drawPureColor_Triangle(canvas: Canvas, color: Int) {
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.style = Paint.Style.FILL
        p.color = color
        val path = Path()
        path.moveTo(canvas.width / 2f, 0f)
        path.lineTo(canvas.width.toFloat(), canvas.height.toFloat())
        path.lineTo(0f, canvas.height.toFloat())
        path.lineTo(canvas.width / 2f, 0f)
        path.close()
        canvas.drawPath(path, p)
    }

    fun drawPureColor_Circle(canvas: Canvas, color: Int) {
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.style = Paint.Style.FILL
        p.color = color
        val r = min(canvas.width, canvas.height) / 2f
        canvas.drawCircle(canvas.width / 2f, canvas.height / 2f, r, p)
    }

    fun drawPureColor_Oval(canvas: Canvas, color: Int) {
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.style = Paint.Style.FILL
        p.color = color
        canvas.drawOval(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), p)
    }


    fun tintDrawable(d: Drawable, color: Int) {
        DrawableCompat.setTint(d, color)
    }

    fun tintBitmap(b: Bitmap, color: Int) {
        repeat(b.width) { x ->
            repeat(b.height) { y ->
                val originColor = b.getPixel(x, y)
                val alpha = Color.alpha(originColor)
                if (alpha > 0) {
                    val newColor = replaceColorAlpha(color, alpha / 255f)
                    b.setPixel(x, y, newColor)
                }
            }
        }
    }

    fun tintImageView(view: ImageView, color: Int) {
        val d = view.drawable ?: return
        tintDrawable(d, color)
    }


    fun getImageWidthHeight(path: String): IntArray {
        val option = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(path, option)
        return IntArray(2).apply {
            set(0, option.outWidth)
            set(1, option.outHeight)
        }
    }

    fun getBitmapUnderMaxSupport(path: String, complete: (Bitmap) -> Unit, error: () -> Unit) {
        val (w, h) = getImageWidthHeight(path)
        if (w <= 0 || h <= 0) {
            error.invoke()
        } else {
            val shouldWidth = min(w, AppConfig.maxEditImageSize)
            val shouldHeight = min(h, AppConfig.maxEditImageSize)
            Glide.with(U.app).asBitmap().load(path).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                .override(shouldWidth, shouldHeight)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) = Unit
                    override fun onLoadFailed(errorDrawable: Drawable?) = error.invoke()
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) = complete.invoke(resource)
                })
        }
    }


    /**
     * 获取指定角度旋转的bitmap
     */
    fun makeRotatingBitmap(source: Bitmap, degree: Float, useOriginIfNoChange: Boolean = true): Bitmap {
        if (degree < 0 || degree > 359)
            throw IllegalStateException("degree must in 0 - 359")
        if (degree == 0f)
            return if (useOriginIfNoChange) source else Bitmap.createBitmap(source)
        var resultW: Int
        var resultH: Int
        if (degree % 90f == 0f) {
            resultW = if (degree % 180f == 0f) source.width else source.height
            resultH = if (degree % 180f == 0f) source.height else source.width
        } else {
            val realDeg = Math.toRadians(
                if (degree > 180) {
                    (degree - 180).toDouble() % 90
                } else {
                    degree.toDouble() % 90
                }
            )
            val x1 = (source.width / 2) * cos(realDeg) - (-source.height / 2) * sin(realDeg)
            val y1 = (source.width / 2) * sin(realDeg) - (-source.height / 2) * cos(realDeg)
            val x2 = (source.width / 2) * cos(realDeg) - (-source.height / 2) * sin(realDeg)
            val y2 = (source.width / 2) * sin(realDeg) - (-source.height / 2) * cos(realDeg)
            resultW = abs(max(x1 * 2, x2 * 2)).toInt()
            resultH = abs(max(y1 * 2, y2 * 2)).toInt()
            if (degree > 90 && degree < 180 || degree > 270 && degree < 360) {
                val ow = resultW
                resultW = resultH
                resultH = ow
            }
        }
        val result = Bitmap.createBitmap(resultW, resultH, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        canvas.rotate(degree, result.width / 2f, result.height / 2f)
        canvas.drawBitmap(source, (result.width - source.width) / 2f, (result.height - source.height) / 2f, Paint(Paint.ANTI_ALIAS_FLAG))
        return result
    }


    fun makeFlipLeftRightBitmap(source: Bitmap): Bitmap {
        val iv = ImageView(U.app)
        iv.setImageBitmap(source)
        iv.layout(0, 0, source.width, source.height)
        iv.measure(
            View.MeasureSpec.makeMeasureSpec(source.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(source.height, View.MeasureSpec.EXACTLY)
        )
        iv.rotationY = 180f
        val container = FrameLayout(U.app)
        container.addView(iv, FrameLayout.LayoutParams(source.width, source.height))
        val result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        container.draw(Canvas(result))
        return result
    }

    fun makeFlipTopBottomBitmap(source: Bitmap): Bitmap {
        val iv = ImageView(U.app)
        iv.setImageBitmap(source)
        iv.layout(0, 0, source.width, source.height)
        iv.measure(
            View.MeasureSpec.makeMeasureSpec(source.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(source.height, View.MeasureSpec.EXACTLY)
        )
        iv.rotationX = 180f
        val container = FrameLayout(U.app)
        container.addView(iv, FrameLayout.LayoutParams(source.width, source.height))
        val result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        container.draw(Canvas(result))
        return result
    }

}