package yzx.app.editer.util.bmp

import android.graphics.Bitmap
import android.graphics.Canvas
import yzx.app.editer.dta.PureColorShape


object BitmapAlmighty {

    fun makePureColor(shape: PureColorShape, color: Int, w: Int, h: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)


        return bitmap
    }

}