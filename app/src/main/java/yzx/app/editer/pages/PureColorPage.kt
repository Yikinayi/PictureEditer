package yzx.app.editer.pages

import android.content.Intent
import android.os.Bundle
import yzx.app.editer.pages.abs.BaseEditPage
import yzx.app.editer.util.U


class PureColorPage : BaseEditPage() {

    companion object {
        fun launch() = U.app.startActivity(Intent(U.app, PureColorPage::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}