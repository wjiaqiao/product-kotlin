package com.example.productkotlin.ui.test

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.productkotlin.base.BaseVAct
import com.example.productkotlin.base.GlideImg
import com.example.productkotlin.databinding.ActivityTestDecorationBinding
import com.example.productkotlin.databinding.ItemTestDecorationBinding
import com.jiaqiao.product.ext.*
import com.jiaqiao.product.widget.RVDecoration
import com.jiaqiao.product.widget.adapter.ProductBaseBindingAdapter


class TestDecorationAc : BaseVAct<ActivityTestDecorationBinding>() {


    private val list = mutableListOf<GlideImg>()

    private val adapter by lazy {
        object : ProductBaseBindingAdapter<GlideImg, ItemTestDecorationBinding>() {
            override fun convert(
                bind: ItemTestDecorationBinding,
                item: GlideImg,
                holder: BaseViewHolder
            ) {
//                Glide.with(context).load(item.url).into(object : SimpleTarget<Drawable?>() {
//                    override fun onResourceReady(
//                        resource: Drawable,
//                        @Nullable transition: Transition<in Drawable?>?
//                    ) {
////                        item.scale = (resource.intrinsicWidth * 1f / resource.intrinsicHeight)
////                        setImageHeight(bind.view1, item.scale)
//                        bind.view1.setImageDrawable(resource)
//                    }
//                })
                bind.view1.loadDrawable(item.url)
//                setImageHeight(
//                    bind.view1,
//                    if (holder.bindingAdapterPosition % 2 == 0) 0.5f else 1.0f
//                )
                bind.view1.setBackgroundColor(
                    if (holder.bindingAdapterPosition % 2 == 0) Color.parseColor(
                        "#777777"
                    ) else Color.parseColor("#EEEEEE")
                )

//                bind.view1.postDelayed({
//                    val posi = holder.bindingAdapterPosition
//                    (bind.view1.right - bind.view1.left).plog("${posi}.width")
//                    (bind.view1.bottom - bind.view1.top).plog("${posi}.height")
//                }, 1000)
            }

            private fun setImageHeight(imageView: ImageView, scale: Float) {
                if (scale > 0) {
                    imageView.setHeight((imageView.measuredWidth / scale).toInt())
                }
            }

        }

    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.rv1.staggeredVertical(3).addItemDecoration(RVDecoration(10.dp))
        launch {
            runIo {
                list.clear()

                list.add(GlideImg("https://img0.baidu.com/it/u=487279264,308051019&fm=253&fmt=auto&app=138&f=PNG?w=500&h=1019"))
                list.add(GlideImg("https://img1.baidu.com/it/u=94904965,608097738&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500"))
                list.add(GlideImg("https://inews.gtimg.com/newsapp_bt/0/14555604931/641"))
                list.add(GlideImg("https://img0.baidu.com/it/u=3032776730,2178451350&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800"))
                list.add(GlideImg("https://img0.baidu.com/it/u=2971860837,688680294&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800"))
                list.add(GlideImg("https://c-ssl.dtstatic.com/uploads/item/201702/05/20170205204925_LKWrd.thumb.1000_0.jpeg"))
                list.add(GlideImg("https://lmg.jj20.com/up/allimg/sj02/2101200U033H37-0-lp.png"))
                list.add(GlideImg("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201503%2F16%2F20150316082631_YZ28f.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692153987&t=8b0a9d73c6b7b4ae6ca835501a25637d"))

                list.addAll(list)


//                for (i in 0..5) {
//                    list.add(i.toString())
//                }
            }.await()
            adapter.setList(list)
            mViewBind.rv1.adapter = adapter
        }
    }

}