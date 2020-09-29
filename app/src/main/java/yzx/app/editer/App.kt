package yzx.app.editer

import android.app.Application
import android.util.Log
import yzx.app.editer.util.U


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        U.app = this

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Log.e("FUCK_SHIT_ERROR", "", e)
        }
    }

}