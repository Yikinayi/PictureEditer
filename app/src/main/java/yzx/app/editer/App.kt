package yzx.app.editer

import android.app.Application
import android.os.Handler
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yzx.app.editer.util.U
import kotlin.system.exitProcess


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        U.app = this

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Log.e("FUCK_SHIT_ERROR", "", e)
            Thread.sleep(3000)
            exitProcess(-1)
        }
    }

}