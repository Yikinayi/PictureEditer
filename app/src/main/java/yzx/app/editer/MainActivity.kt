package yzx.app.editer

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_main.*
import yzx.app.editer.pages.MainEditFragment
import yzx.app.editer.pages.MainPictureCacheFragment
import yzx.app.editer.util.dp2px
import yzx.app.editer.widget.SimpleFragmentTab


class MainActivity : AppCompatActivity() {

    private val fragment_key_edit = "edit"
    private val fragment_key_cache = "cache"
    private val fragment_edit = MainEditFragment()
    private val fragment_cache = MainPictureCacheFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 23)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(R.layout.activity_main)
        initBottomFragmentTab()
    }


    private fun initBottomFragmentTab() {
        fragmentTab.set(supportFragmentManager, R.id.container, dp2px(52), ArrayList<SimpleFragmentTab.Tab>().apply {
            add(SimpleFragmentTab.Tab().apply {
                this.colorNormal = Color.parseColor("#bfbfbf")
                this.colorSelected = ResourcesCompat.getColor(resources, R.color.colorAccent, null)
                this.text = "编辑"
                this.textSizeDP = 11f
                this.imageSelected = R.drawable.main_tab_edit_selected
                this.imageNormal = R.drawable.main_tab_edit_normal
                this.imageWidth = dp2px(26)
                this.imageHeight = dp2px(26)
                this.key = fragment_key_edit
                this.fragment = fragment_edit
                this.imageAndTextGap = dp2px(3)
            })
            add(SimpleFragmentTab.Tab().apply {
                this.colorNormal = Color.parseColor("#bfbfbf")
                this.colorSelected = ResourcesCompat.getColor(resources, R.color.colorAccent, null)
                this.text = "临存图"
                this.textSizeDP = 11f
                this.imageSelected = R.drawable.main_tab_cache_selected
                this.imageNormal = R.drawable.main_tab_cache_normal
                this.imageWidth = dp2px(26)
                this.imageHeight = dp2px(26)
                this.key = fragment_key_cache
                this.fragment = fragment_cache
                this.imageAndTextGap = dp2px(3)
            })
        }, fragment_key_edit)
    }

}