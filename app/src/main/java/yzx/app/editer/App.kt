package yzx.app.editer

import android.app.Application
import yzx.app.editer.util.U


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        U.app = this

    }

}