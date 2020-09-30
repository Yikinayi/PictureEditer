package yzx.app.editer.pages

import android.Manifest
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_main_edit.view.*
import kotlinx.android.synthetic.main.item_main_edit_nomore.view.*
import yzx.app.editer.R
import yzx.app.editer.dta.AppConfig
import yzx.app.editer.dta.EditAbility
import yzx.app.editer.util.bmp.BitmapAlmighty
import yzx.app.editer.util.dp2px
import yzx.app.editer.util.inflate
import yzx.app.editer.util.permission.PermissionRequester
import yzx.app.editer.util.sysResource.SystemPhotograph
import yzx.app.editer.widget.NoMoreAnimationView
import yzx.app.editer.widget.toast.toast


class MainEditFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeItems()
    }


    private fun makeItems() {
        val recyclerView = view as? RecyclerView ?: return
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.left = dp2px(18)
                outRect.right = outRect.left
                outRect.bottom = dp2px(15)
            }
        })
        val noMoreHolder = object : RecyclerView.ViewHolder(recyclerView.inflate(R.layout.item_main_edit_nomore)) {}
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun getItemViewType(position: Int) = if (position < EditAbility.list.size) 1 else 2
            override fun getItemCount(): Int = EditAbility.list.size + 1
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return if (viewType == 1)
                    object : RecyclerView.ViewHolder(parent.inflate(R.layout.item_main_edit)) {}
                else noMoreHolder
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                if (holder.itemView.id == R.id.nomoreLayout) {
                    this@MainEditFragment.nomoreAnimator = holder.itemView.anim
                    holder.itemView.anim.start()
                } else {
                    val data = EditAbility.list[position]
                    holder.itemView.icon.setImageResource(data.image)
                    holder.itemView.text.text = data.nameText
                    holder.itemView.clickView.setOnClickListener { onItemClick(data) }
                }
            }
        }
    }


    private fun onItemClick(data: EditAbility) {
        when (data) {
            EditAbility.Pure -> PureColorPage2.launch()
            EditAbility.Rotate -> getPicture { RotationPage.launch(it) }
            EditAbility.Flip -> getPicture { FlipPage.launch(it) }
            EditAbility.Gif -> getPicture(false) { GifPage.launch(it) }
            EditAbility.Absorb -> getPicture { AbsorbPage.launch(it) }
            EditAbility.TextToImage -> TextToImagePage.launch()
        }
    }

    private fun getPicture(limitWidthHeight: Boolean = true, block: (String) -> Unit) {
        val activity = activity ?: return
        PermissionRequester.request(activity, Manifest.permission.READ_EXTERNAL_STORAGE) { result ->
            if (result) {
                SystemPhotograph.request(activity) { path ->
                    if (!path.isBlank()) {
                        val wh = BitmapAlmighty.getImageWidthHeight(path)
                        val width = wh[0]
                        val height = wh[1]
                        if (width <= 0 || height <= 0) {
                            toast("图片有问题,请重选")
                        } else if (width < AppConfig.minEditSupportImageSize || height < AppConfig.minEditSupportImageSize) {
                            if (limitWidthHeight)
                                toast("图片太小啦")
                            else
                                block.invoke(path)
                        } else
                            block.invoke(path)
                    }
                }
            } else {
                toast("没有权限啊")
            }
        }
    }


    private var nomoreAnimator: NoMoreAnimationView? = null

    override fun onDestroyView() {
        super.onDestroyView()
        nomoreAnimator?.stop()
    }

}