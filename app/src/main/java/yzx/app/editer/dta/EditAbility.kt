package yzx.app.editer.dta

import yzx.app.editer.R


enum class EditAbility {

    Draw, Text, Clip, Absorb, Gif, Pure, Rotate, Scale

    ;

    lateinit var nameText: String
        private set
    var image: Int = 0
        private set


    companion object {
        val list: List<EditAbility> = ArrayList()

        init {
            (list as ArrayList).apply {
                add(Draw.apply {
                    nameText = "画个图"
                    image = R.drawable.ic_main_edit_canvas
                })
                add(Text.apply {
                    nameText = "添加点文字"
                    image = R.drawable.ic_main_edit_text
                })
                add(Clip.apply {
                    nameText = "裁剪"
                    image = R.drawable.ic_main_edit_clip
                })
                add(Absorb.apply {
                    nameText = "吸取图片上颜色"
                    image = R.drawable.ic_main_edit_absorb
                })
                add(Gif.apply {
                    nameText = "GIF图片单帧率截图"
                    image = R.drawable.ic_main_edit_git
                })
                add(Pure.apply {
                    nameText = "单色图创建"
                    image = R.drawable.ic_main_edit_pure
                })
                add(Rotate.apply {
                    nameText = "旋转"
                    image = R.drawable.ic_main_edit_rotation
                })
                add(Scale.apply {
                    nameText = "拉伸"
                    image = R.drawable.ic_main_edit_scale
                })
            }
        }
    }

}